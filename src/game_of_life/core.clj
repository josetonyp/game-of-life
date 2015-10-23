(ns game_of_life.core
  (:require [quil.core :as q]
            [game_of_life.cells :as cells]
            [quil.middleware :as m]
            [clojure.tools.namespace.repl :refer [refresh]]))

(def window-box
  { :width 851
    :height 315
    :point-size 5
    :xunits (/ 851 5)
    :yunits (/ 315 5)
    :xoffset 0
    :yoffset 0
    :fps 30 })

; Definir el concepto de celda
; Econtrar los puntos alrededor de una celda
; Hacer un diccionario con las celdas que se han visitado para optimizar el proceso
; convertir todos los calculos en multithread

(def life-grid
  "First Grid"
  (into {} (cells/grid (:xunits window-box) (:yunits window-box))))


(defn draw-point [x y dot]
  (if (= dot 1)
    (do
      (let [size (:point-size window-box)]
        (q/stroke 0)             ;; Set the stroke colour to a random grey
        (q/stroke-weight size)       ;; Set the stroke thickness randomly
        (q/point
          (+ (:xoffset window-box) (* x size))
          (+ (:yoffset window-box)(* y size))
      )))))

(defn setup []
  (q/frame-rate (:fps window-box))

  (q/color-mode :rgb)
   { :grid life-grid })

(defn life
  "doc-string"
  [grid]
  (into {}
    (doall
      (for [[k cell] grid]
        (if (cells/is-dead cell)
          (cells/revive cell grid)
          (cells/kill cell grid)))
      ))
  )

(defn update-state [state]
  ; Update sketch state by changing circle color and position.
  (let [grid (:grid state )]
   { :grid  (life grid) }))

(defn draw-state [state]
  ; Clear the sketch by filling it with light-grey color.
  (q/background 240)
  (let [grid (:grid state)]
    (doall
      (for [[k cell] grid]
        (draw-point (:x cell) (:y cell) (:life cell) )))
    )
  ; (q/save-frame "pretty-pic-####.jpg")
  )

(q/defsketch game_of_life
  :title "You spin my circle right round"
  :size [(:width window-box) (:height window-box)]
  ; setup function called only once, during sketch initialization.
  :setup setup
  ; update-state is called on each iteration before draw-state.
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  ; This sketch uses functional-mode middleware.
  ; Check quil wiki for more info about middlewares and particularly
  ; fun-mode.
  :middleware [m/fun-mode])
