(ns game_of_life.core
  (:require [quil.core :as q]
            [game_of_life.cells :as cells]
            [game_of_life.folder :as folder]
            [quil.middleware :as m]
            [clojure.java.io :as io]
            [clojure.tools.namespace.repl :refer [refresh]]))

(def window-width 720)
(def window-height 480)
(def *max-limit* 500)
(def limit (atom 0))

(def window-box
  { :width window-width
    :height window-height
    :point-size 5
    :yunits (inc (/ window-height 5))
    :xunits (inc (/ window-width 5))
    :xoffset 0
    :yoffset 0
    :fps 15 })

(defn draw-point [x y dot]
  (if (= dot 1)
    (do
      (let [size (:point-size window-box)]
        (q/stroke 0)             ;; Set the stroke colour to a random grey
        (q/stroke-weight size)  ;; Set the stroke thickness randomly
        (q/point
          (+ (:xoffset window-box) (* x size))
          (+ (:yoffset window-box)(* y size))
      )))))

(defn setup []
  (q/frame-rate (:fps window-box))
  (q/color-mode :rgb)
  { :grid (cells/grid (:xunits window-box) (:yunits window-box)) })

(defn life [grid]
  "doc-string"
  (into {}
    (doall
      (for [cell grid]
        (if (cells/is-dead cell)
          (cells/revive cell grid)
          (cells/kill cell grid))))))

(defn update-state [state]
  ; Update sketch state by changing circle color and position.
  (let [grid (:grid state )]
   {:grid  (life grid) }))

(defn draw-state [state]
  ; Clear the sketch by filling it with light-grey color.
  (q/background 240)
  (let [grid (:grid state)]
    (doall
      (for [[k cell] grid]
        (draw-point (:x cell) (:y cell) (:life cell) )))))

(q/defsketch game_of_life
  :title "The Game of Life"
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
