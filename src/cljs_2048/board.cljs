(ns cljs-2048.board
  (:require
   [cljs-2048.game-events :as game-events]
   [re-frame.core :as re-frame]))

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
        updated (assoc row c value)]
    (assoc board r updated)))

(defn get-tile
  "Get the value from given row and column of the board"
  [board r c]
  (-> board
      (nth r)
      (nth c)))

(defn empty-tiles
  "Returns list of [r c] where r is row and c is column index of tile with zero"
  [board]
  (for [r     (range rows-count)
        c     (range columns-count)
        :when (zero? (get-tile board r c))]
    [r c]))

(defn random-tile
  "Set 2 or 4 to random empty position of tile"
  [board]
  (if-let [tiles (seq (empty-tiles board))]
    (let [[r c] (rand-nth tiles)]
      (set-tile board r c (random-fill)))
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
   Otherwise returns the v. Default fill-count is columns-count."
  ([v]
   (fill-zeroes v columns-count))
  ([v n]
   (let [fill-count (- n (count v))]
     (into v (repeat fill-count 0)))))

(defn combine
  "Combines two equal tiles into one in the v and fills remaining with zeroes"
  [v]
  (loop [[head & remaining] v
         acc                []]
    (if (empty? remaining)
      (fill-zeroes (conj acc (if head head 0)))
      (if (= head (first remaining))
        (let [sum (+ head (first remaining))]
          (when (pos? sum)
            (re-frame/dispatch [::game-events/add-score sum]))
          (recur (rest remaining) (conj acc sum)))
        (recur remaining (conj acc head))))))

(defn reverse-board
  "Reverse the board"
  [board]
  (mapv #(vec (rseq %)) board))

(defn move-tiles-left
  "Move the tiles to left in v, by shifting value to empty tile"
  [v]
  (-> (filterv pos? v)
      (fill-zeroes)))

(defn stack-left
  [board]
  (map move-tiles-left board))

(defn move-left
  "Move tiles to left and combine equal tiles at edge"
  [board]
  (->> board
       (stack-left)
       (mapv combine)))

(defn move-right
  "Move tiles to right and combine equal tiles at edge"
  [board]
  (->> board
       (reverse-board)
       (move-left)
       (reverse-board)))

(defn move-up
  "Move tiles to up and combine equal tiles at edge"
  [board]
  (-> board
      (rotate-left)
      (move-left)
      (rotate-right)))

(defn move-down
  "Move tiles to down and combine equal tiles at edge"
  [board]
  (-> board
      (rotate-right)
      (move-left)
      (rotate-left)))

(def movements {::left  move-left
                ::right move-right
                ::up    move-up
                ::down  move-down})

(defn move
  "Returns new board after moving in the direction. Adds random tile if board has changed"
  [board direction]
  (let [new-board ((direction movements) board)]
    (if (= new-board board)
      new-board
      (random-tile new-board))))
