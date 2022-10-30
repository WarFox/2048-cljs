(ns cljs-2048.game
  (:require
   [cljs-2048.board :as board]))

(defn can-move-sideways?
  [v]
  (->> (partition-by identity v)
       (map count)
       (some #(> % 1))))

(defn can-move?
  [board]
  (or
   (some can-move-sideways? board)
   (some can-move-sideways? (board/transpose board))))

(defn gameover?
  "Returns true if game over, otherwise false.
   If move left is possible, then move right is also possible
   If move up is possible, then move down is also possible"
  [board]
  (and
   (empty? (board/empty-tiles board))
   (not (can-move? board))))
