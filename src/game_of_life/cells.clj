(ns game_of_life.cells)

(defn neighbors
  [x y]
  [
    (str (dec x) (dec y))
    (str x (dec y))
    (str (inc x) (dec y))

    (str (dec x) y)
    (str (inc x) y)

    (str (dec x) (inc y))
    (str x (inc y))
    (str (inc x) (inc y))
    ])

(defn cell
  "doc-string"
  [alive x y]
  { :life alive
    :id (str x y)
    :x x
    :y y
    :neighbors (neighbors x y)
    })


(defn grid
  "Creates an initial grid"
  [xunits yunits]
  (for [x (range 0 xunits)
          y (range 0 yunits)]
    (cell (rand-int 2) x y)
    )
  )
