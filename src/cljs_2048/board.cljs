(ns cljs-2048.board)

(def rows-count 4)
(def columns-count 4)
(def random-fill #(rand-nth [2 2 2 4])) ;; Favor 2 for random filling

(def initial-board
  [[[0] [0] [0] [0]]
   [[0] [0] [0] [0]]
   [[0] [0] [0] [0]]
   [[0] [0] [0] [0]]])

(defn set-tile
  "Set value at given row and column of the board"
  [board r c value state]
  (let [row (nth board r)
        updated (assoc row c [value state])]
    (assoc board r updated)))

(defn get-tile
  "Get the value from given row and column of the board"
  [board r c]
  (-> board
      (nth r)
      (nth c)))

(defn empty-tiles
  "Returns list of [r c] where r is row and c is column index of tile with [0]"
  [board]
  (for [r     (range rows-count)
        c     (range columns-count)
        :when (= [0] (get-tile board r c))]
    [r c]))

(defn random-tile
  "Set 2 or 4 to random empty positions of tile"
  [board]
  (if-let [tiles (seq (empty-tiles board))]
    (let [[r c] (rand-nth tiles)]
      (set-tile board r c (random-fill) :random))
    board))

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
  (transpose (rseq board)))

(defn rotate-left
  [board]
  (transpose (map rseq board)))

(defn fill-zeroes
  "If vector v's length is less than n, fill the remaining slots with 0.
   Otherwise returns the v."
  [v n]
   (let [fill-count (- n (count v))]
     (into (vec v) (repeat fill-count [0]))))

(defn merge-left
  "Merge two equal tiles into one in the vector v and fills remaining with zeroes.
  v is a vector of vectors of single integer, for example [[2 :random] [0] [4 :merged] [4]]
  [0] means empty slot and all [0] should be trailing in the result.
  Add state as :merged when combining two tiles
  (combine [[2 :random] [0] [4 :merged] [4]]) ;; => [[2] [8 :merged] [0] [0]]
  "
  [v]
  (let [non-zero-v (remove zero? (map first v)) ;; Remove 0 tiles before reduction, work with value not state
        merged     (reduce (fn [[acc prev] current]
                              (cond
                                ;; If previous is nil, set current as previous
                                (nil? prev)
                                [acc current]

                                ;; If equal, merge and append to accumulator, set nil as previous
                                (= prev current)
                                (let [sum (+ prev current)]
                                  [(conj acc [sum :merged]) nil])

                                ;; If not equal, append previous to accumulator and current element as previous
                                :else
                                [(conj acc [prev]) current]))

                            [[] nil] ;; Start with empty accumulator and nil previous
                            non-zero-v) ;; Reduce over non-zero tiles.
        ;; If last tile is not merged, add it to result
        result       (first merged)
        last         (second merged)
        final-result (if last (conj result [last]) result)]

        (fill-zeroes final-result columns-count)))

(defn reverse-board
  "Reverse the board"
  [board]
  (mapv #(vec (rseq %)) board))

(defn equal?
  "Check if two boards are equal or not.
  Two boards are equals if the values of all tiles are equal,
  we do not care about the state of the tile."
  [board1 board2]
  (every? true? (map =
                     (remove keyword? (flatten board1))
                     (remove keyword? (flatten board2)))))
