(ns game_of_life.cells)

(def *fixed-revive* 1)
(def *min-to-live* 2)
(def *min-to-die* 3)

(defn neighbors
  [x y]
  (into [] (distinct [
    (str "x" (max 0 (dec x)) "y" (max 0 (dec y)))
    (str "x" x "y" (max 0 (dec y)))
    (str "x" (inc x) "y" (max 0 (dec y)))

    (str "x" (max 0 (dec x)) "y" y)
    (str "x" (inc x) "y" y)

    (str "x" (max 0 (dec x)) "y" (inc y))
    (str "x" x "y" (inc y))
    (str "x" (inc x) "y" (inc y))
    ])))

(defn cell
  "doc-string"
  [alive x y]
  [(keyword (str "x" x "y" y)) { :life alive
      :id (str "x" x "y" y)
      :x x
      :y y
      :neighbors (neighbors x y)
      }])

(defn grid
  "Creates an initial grid"
  [xunits yunits]
  (into {}
    (for [x (range 0 xunits)
          y (range 0 yunits)]
      (cell (rand-int 2) x y)
      )))

(defn n-neighborgs [cell grid]
  "doc-string"
  (reduce
          (fn [alives id]
            (+ alives (:life (or ((keyword id) grid) {:life 0}))))
          0
          (:neighbors cell)))

(defn has-n-neighborgs
  "doc-string"
  [n cell grid]
  (>= (n-neighborgs cell grid) (dec n)))

(defn is-dead  [[_ cell]] (= 0 (:life cell)))

(defn variable-revive  [oldCell grid]
  (let [valOldCell (val oldCell)
        n (n-neighborgs valOldCell grid)]
    (if
      (and
        (> n *min-to-live*)
        (<= n *min-to-die*))
      (cell 1 (:x valOldCell) (:y valOldCell))
      oldCell)))

(defn fix-revive  [oldCell grid]
  (let [valOldCell (val oldCell)
        n (n-neighborgs valOldCell grid)]
    (if
      (= n *min-to-die*)
      (cell 1 (:x valOldCell) (:y valOldCell))
      oldCell)))

(defn revive  [oldCell grid]
  (if (= 1 *fixed-revive*)
    (fix-revive oldCell grid)
    (variable-revive oldCell grid)))

(defn kill [oldCell grid]
  (let [valOldCell (val oldCell)
        n (n-neighborgs valOldCell grid)]
    (cond
      (< n *min-to-live*)
        (cell 0 (:x valOldCell) (:y valOldCell))
      (> n *min-to-die*)
        (cell 0 (:x valOldCell) (:y valOldCell))
      :else oldCell)))



