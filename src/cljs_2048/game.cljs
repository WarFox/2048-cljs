(ns cljs-2048.game
  (:require
   [cljs-2048.board :as board]
   [re-frame.core :as re-frame]))

(defn gameover?
  "Returns true if game over, otherwise false"
  [new-board old-board]
  (and (empty? (board/empty-tiles old-board))
       (= old-board new-board)))

(defn move
  "Make new board based on the direction. Adds random tile if board has changed"
  [board direction]
  (let [moved-board ((direction board/movements) board)
        new-board (if (= moved-board board)
                    moved-board
                    (board/random-tile moved-board))]
    new-board))
