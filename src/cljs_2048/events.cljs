(ns cljs-2048.events
  (:require
   [cljs-2048.board :as b]
   [cljs-2048.db :as db]
   [cljs-2048.game :as g]
   [cljs-2048.local-storage :as ls]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [re-frame.core :as re-frame]))

;; DB events

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced
  [_ _]
  db/default-db))

(defn start-game
  [db [_ _]]
  (assoc db
         :board (b/with-two-random-tiles)
         :gameover false
         :score 0))

(re-frame/reg-event-db ::start-game start-game)

(defn gameover
  [db [_]]
  (assoc db :gameover true))

(re-frame/reg-event-db ::gameover gameover)

(defn clear-score-changes
  [db _]
  (assoc db
         :score-changed false
         :high-score-changed false))

(re-frame/reg-event-db ::clear-score-changes clear-score-changes)

(defn clear-animation
  [db _]
  (assoc db
         :sliding? false))

(re-frame/reg-event-db ::clear-animation clear-animation)

(defn swap-new-board
  [db [_ new-board]]
  (assoc db :board new-board))

(re-frame/reg-event-db ::swap-new-board swap-new-board)

;; FX events

(defn add-score
  [{:keys [db]} [_ new-score]]
  (let [score      (+ new-score (:score db))
        high-score (max score (:high-score db))]
    (ls/set-high-score! high-score) ;; set high-score in local-storage
    {:db (assoc db
                :score score
                :high-score high-score
                :score-changed true
                :high-score-changed (> high-score (:high-score db)))
     :fx [[:dispatch-later {:ms 200 :dispatch [::clear-score-changes]}]]}))

(re-frame/reg-event-fx ::add-score add-score)

(defn move
  [{:keys [db]} [_ direction]]
  (let [board     (:board db)
        new-board (g/move board direction)]
    {:db (assoc db
                :direction direction
                :sliding? (not (b/equal? board new-board)))
     :fx (if (g/gameover? new-board)
           [[:dispatch [::gameover]]]

           [[:dispatch-later {:ms 200 :dispatch [::clear-animation]}]
            [:dispatch-later {:ms 200 :dispatch [::swap-new-board new-board]}]
            (let [score (g/move-score new-board)]
              (when (pos? score)
                [:dispatch [::add-score score]]))])}))

(re-frame/reg-event-fx ::move move)
