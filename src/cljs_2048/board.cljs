(ns cljs-2048.board)

(def rows 4)
(def columns 4)

(def initial-board
  [[0 0 0 0]
   [0 0 0 0]
   [0 0 0 0]
   [0 0 0 0]])

(defn set-cell
  "Set value at given row and column of the board"
  [board r c value]
  (let [row (nth board r)
        row-updater (assoc row c value)]
    (assoc board r row-updater)))

(defn get-cell
  "Get the value from given row and column of the board"
  [board r c]
  (-> board
      (nth r)
      (nth c)))

(defn random-cell
  "Add 2 or 4 to random position of cell"
  [board]
  (set-cell board (rand-int rows) (rand-int columns) (rand-nth [2 4])))

(defn with-two-random-cells
  []
  (-> initial-board
      (random-cell)
      (random-cell)))
