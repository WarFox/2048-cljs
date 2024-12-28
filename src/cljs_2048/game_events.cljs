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
           :high-score high-score
           :score-changed true
           :high-score-changed (> high-score (:high-score db)))))

(defn gameover
  [db [_]]
  (assoc db :gameover true))

(re-frame/reg-event-db
 ::gameover
 gameover)

(re-frame/reg-event-db
 ::clear-score-animations
 (fn [db _]
   (assoc db
          :score-changed false
          :high-score-changed false)))

(re-frame/reg-event-fx
  ::add-score
  (fn [{:keys [db]} [_ new-score]]
    {:db    (add-score db [_ new-score])
     :dispatch-later [{:ms       600
                       :dispatch [::clear-score-animations]}]}))
