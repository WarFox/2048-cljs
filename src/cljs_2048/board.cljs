(ns cljs-2048.board)

(def rows 4)
(def columns 4)

(def initial-board
  [[0 0 0 0]
   [0 0 0 0]
   [0 0 0 0]
   [0 0 0 0]])

(defn set-tile
  "Set value at given row and column of the board"
  [board r c value]
  (let [row (nth board r)
        row-updater (assoc row c value)]
    (assoc board r row-updater)))

(defn get-tile
  "Get the value from given row and column of the board"
  [board r c]
  (-> board
      (nth r)
      (nth c)))

(defn random-tile
  "Add 2 or 4 to random empty position of tile"
  [board]
  (let [[r c] [(rand-int 4) (rand-int 4)]
        value (get-tile board r c)]
    (if (pos? value)
      (recur board)
      (set-tile board r c (rand-nth [2 4])))))

(defn with-two-random-tiles
  []
  (-> initial-board
      (random-tile)
      (random-tile)))

(defn move-up
  [board]
  board)

(defn move-down
  [board]
  board)

(defn move-right
  [board]
  board)

(defn move-left
  [board]
  board)
