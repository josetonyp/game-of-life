(ns game_of_life.cells)

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
  (for [x (range 0 xunits)
          y (range 0 yunits)]
    (cell (rand-int 2) x y)
    )
  )

(defn n-neighborgs
  "doc-string"
  [cell grid]
  (reduce
          (fn [alives id]
            (+ alives (:life (or ((keyword id) grid) {:life 0}))))
          0
          (:neighbors cell)))

(defn has-n-neighborgs
  "doc-string"
  [n cell grid]
  (>= (n-neighborgs cell grid) (dec n)))

(defn is-alive  [cell] (= 1 (:life cell)))
(defn is-dead  [cell] (= 0 (:life cell)))
(defn keep-cell  [oldCell] (cell (:life oldCell) (:x oldCell) (:y oldCell)))

(defn revive  [oldCell grid]
  (if (= 3 (n-neighborgs oldCell grid))
    (cell 1 (:x oldCell) (:y oldCell))
    (keep-cell oldCell)))

(defn kill [oldCell grid]
  (cond
    (< (n-neighborgs oldCell grid) 2)
      (cell 0 (:x oldCell) (:y oldCell))
    (> (n-neighborgs oldCell grid) 3)
      (cell 0 (:x oldCell) (:y oldCell))
    :else
      (cell 1 (:x oldCell) (:y oldCell))
    ))



