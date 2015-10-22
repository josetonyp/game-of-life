(ns game_of_life.core
  (:require [quil.core :as q]
            [game_of_life.cells :as cells]
            [quil.middleware :as m]
            [clojure.tools.namespace.repl :refer [refresh]]))

(def window-box
  { :width 500
    :height 500
    :point-size 2
    :xunits 250
    :yunits 250
    :fps 15 })

; Definir el concepto de celda
; Econtrar los puntos alrededor de una celda
; Hacer un diccionario con las celdas que se han visitado para optimizar el proceso
; convertir todos los calculos en multithread

(def life-grid
  "First Grid"
  (cells/grid (:xunits window-box) (:yunits window-box)))


(defn vector-rand
  "Get n of random 1's"
  [units]
  (into [] (take units (repeatedly #(rand-int 2)))))

(defn initial-life
  "doc-string"
  [grid]
  (let [xs (/ (:width window-box) (:point-size window-box))
         ys (/ (:height window-box) (:point-size window-box))]
    (into [] (take ys (repeatedly #(vector-rand xs))))))

(defn draw-point [x y dot]
  (if (= dot 1)
    (do
      (let [size 2]
        (q/stroke 0)             ;; Set the stroke colour to a random grey
        (q/stroke-weight size)       ;; Set the stroke thickness randomly
        (q/point (* x size) (* y size))))))

(defn setup []
  ; Set frame rate to 30 frames per second.
  (q/frame-rate (:fps window-box))
  ; Set color mode to HSB (HSV) instead of default RGB.
  (q/color-mode :rgb)
  ; setup function returns initial state. It contains
  ; circle color and position.
  {:color 0
   :angle 0}
   {
    :grid (initial-life 0)
    })

(defn update-state [state]
  ; Update sketch state by changing circle color and position.
  (let [grid (:grid state )]
   { :grid (initial-life grid)}))


(defn print-row
  "Printing a row of dots"
  [row-index row]
  (doall
    (map-indexed
      (fn [idx item]
        (draw-point idx row-index item))
      row))

  )

(defn draw-state [state]
  ; Clear the sketch by filling it with light-grey color.
  (q/background 240)
  (let [v (:grid state)]
    (doall
      (map-indexed
        (fn [idx row]
          (print-row idx row))
        v)))
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
