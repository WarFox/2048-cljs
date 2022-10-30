(ns cljs-2048.game-events
  (:require
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [re-frame.core :as re-frame]
   [cljs-2048.local-storage :as ls]))

(re-frame/reg-event-db
 ::add-score
 (fn-traced
  [db [_ new-score]]
  (let [score      (+ new-score (:score db))
        high-score (max score (:high-score db))]
    (ls/set-high-score! high-score) ;; set high-score in local-storage
    (assoc db
           :score score
           :high-score high-score))))

(re-frame/reg-event-db
 ::gameover
 (fn-traced
  [db [_]]
  (assoc db :gameover true)))
