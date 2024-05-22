(ns cljs-2048.game-events
  (:require
   [re-frame.core :as re-frame]
   [cljs-2048.local-storage :as ls]))

(defn add-score
  [db [_ new-score]]
  (let [score      (+ new-score (:score db))
        high-score (max score (:high-score db))]
    (ls/set-high-score! high-score) ;; set high-score in local-storage
    (assoc db
           :score score
           :high-score high-score)))

(defn gameover
  [db [_]]
  (assoc db :gameover true))

(re-frame/reg-event-db
 ::add-score
 add-score)

(re-frame/reg-event-db
 ::gameover
 gameover)
