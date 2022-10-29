(ns cljs-2048.game
  (:require
   [cljs-2048.board :as board]
   [re-frame.core :as re-frame]))

(defn gameover?
  "Returns true if game over, otherwise false.
   If move left is possible, then move right is also possible
   If move up is possible, then move down is also possible"
  [board]
  (and
   (-> board board/move-left board/empty-tiles empty?)
   (-> board board/move-up board/empty-tiles empty?)))

