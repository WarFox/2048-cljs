(ns cljs-2048.game-events
  (:require
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [re-frame.core :as re-frame]))

(re-frame/reg-event-db
 ::add-score
 (fn-traced
  [db [_ new-score]]
  (-> db
      (assoc :score (+ (:score db) new-score)))))

(re-frame/reg-event-db
 ::gameover
 (fn-traced
  [db [_]]
  (assoc db :gameover true)))
