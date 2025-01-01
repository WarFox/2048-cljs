(ns cljs-2048.game
  (:require
   [cljs-2048.board :as b]))

(defn can-move-sideways?
  [v]
  (or
   ;; if there is a zero, then we can move
   (some #(= [0] %) v)

   ;; if there are two consecutive equal values, then we can move
   (->> (map first v) ;; get the value
        (partition-by identity)
        (map count)
        (some #(> % 1)))))

(defn can-move?
  [board]
  (or
   (some can-move-sideways? board)
   (some can-move-sideways? (b/transpose board))))

(defn gameover?
  "Returns true if game over, otherwise false.
   If move left is possible, then move right is also possible
   If move up is possible, then move down is also possible"
  [board]
  (and
   (empty? (b/empty-tiles board))
   (not (can-move? board))))

(defn move-left
  "Move tiles to left and combine equal tiles at edge"
  [board]
  (mapv b/merge-left board))

(defn move-right
  "Move tiles to right and combine equal tiles at edge"
  [board]
  (-> board
      (b/reverse-board)
      (move-left)
      (b/reverse-board)))

(defn move-up
  "Move tiles to up and combine equal tiles at edge"
  [board]
  (-> board
      (b/rotate-left)
      (move-left)
      (b/rotate-right)))

(defn move-down
  "Move tiles to down and combine equal tiles at edge"
  [board]
  (-> board
      (b/rotate-right)
      (move-left)
      (b/rotate-left)))

(def movements
  {::left  move-left
   ::right move-right
   ::up    move-up
   ::down  move-down})

(defn move-score
  "Returns score after moving. Score is sum of :merged tiles"
  [board]
  (->> board
       (mapcat (fn [row]
                 (filter (fn [tile]
                           (= :merged (second tile))) row)))
       (map first)
       (reduce + 0)))

(defn move
  "Returns new board after moving in the direction. Adds random tile if board has changed"
  [board direction]
  (let [new-board ((get movements direction) board)]
    (if (b/equal? new-board board)
      board
      (b/random-tile new-board))))
