(ns cljs-2048.board)

(def rows-count 4)
(def columns-count 4)
(def random-fill #(rand-nth [2 2 2 4])) ;; Favor 2 for random filling

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
  (let [[r c] [(rand-int rows-count) (rand-int columns-count)]
        value (get-tile board r c)]
    (if (pos? value)
      (recur board)
      (set-tile board r c (random-fill)))))

(defn with-two-random-tiles
  []
  (-> initial-board
      (random-tile)
      (random-tile)))

(defn transpose
  "Rows become columns and columns become rows
   https://stackoverflow.com/a/16302220/598444
   https://stackoverflow.com/a/10347404/598444"
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

(defn stack-left
  [board]
  (map move-tiles-left board))

(defn combine
  [[first second & rest]]
  (let [c (into [(+ first second)] rest)]
    (into c (repeat  (- columns-count (count c)) 0))))

(defn move-left
  [board]
  (->> board
      (stack-left)
      (map combine)))

(defn move-right
  [board]
  (->> board
       (map reverse)
       (move-left)
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
