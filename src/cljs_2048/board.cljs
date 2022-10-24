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

(defn transpose
  "Rows become columns and columns become rows
   https://stackoverflow.com/a/16302220/598444
   https://stackoverflow.com/a/10347404/598444
  "
  [board]
  (apply mapv vector board))

(defn rotate-right
  [board]
  (transpose (reverse board)))

(defn rotate-left
  [board]
  (transpose (map reverse board)))

(defn move-tiles-left
  "Move the tiles to left in array, by shifting value to empty tile"
  ([arr]
   (move-tiles-left arr [] []))
  ([[head & remaining] acc zeros]
   (if (empty? remaining)
     (into (conj acc head) zeros)
     (if (zero? head)
       (recur remaining acc (conj zeros 0))
       (recur remaining (conj acc head) zeros)))))

(defn compress-left
  [board]
  (map move-tiles-left board))

(defn move-left
  [board]
  (-> board
      compress-left))

(defn move-right
  [board]
  (->> board
       (map reverse)
       move-left
       (map reverse)))

(defn move-up
  [board]
  (-> board
      (rotate-left)
      (move-left)
      (rotate-right)))

(defn move-down
  [board]
  (-> board
      (rotate-right)
      (move-left)
      (rotate-left)))

