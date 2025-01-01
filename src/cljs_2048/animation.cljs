(ns cljs-2048.animation
  (:require [cljs-2048.game :as g]))

(defn get-transition-style
  "Return --move-x and --move-y based on direction and row/col
   Move must be within bounds of the grid with 4 rows and 4 columns.
   Maximum move is 3 rows or columns in any direction.
   If col is 0, and direction is right, we can move 3 more columns to the right.
   If col is 1, and direction is right, we can move 2 more columns to the right.
   If col is 2, and direction is right, we can move 1 more columns to the right.
   If col is 3, and direction is right, we can move 0 more columns to the right.

   If col is 0, and direction is left, we can move 0 more columns to the left.
   If col is 1, and direction is left, we can move 1 more columns to the left.
   If col is 2, and direction is left, we can move 2 more columns to the left.
   If col is 3, and direction is left, we can move 3 more columns to the left.

   If row is 0, and direction is down, we can move 3 more rows down.

   If row is 3, and direction is up, we can move 3 more rows up.

   We multiply by 8em to match the tile size in the css grid."
  [[row col] direction]
  (let [max-moves       3
        [move-x move-y] (case direction
                          ::g/right (if (< col max-moves)
                                      [(- max-moves col) 0]
                                      [0 0])
                          ::g/left  (if (pos? col)
                                     [(- col) 0]
                                     [0 0])
                          ::g/down  (if (< row max-moves)
                                     [0 (- max-moves row)]
                                     [0 0])
                          ::g/up    (if (pos? row)
                                   [0 (- row)]
                                   [0 0]))]

    {"--move-x" (str (* move-x 8) "em")
     "--move-y" (str (* move-y 8) "em")}))
