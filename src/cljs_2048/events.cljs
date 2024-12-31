(ns cljs-2048.events
  (:require
   [cljs-2048.board :as b]
   [cljs-2048.db :as db]
   [cljs-2048.game :as g]
   [cljs-2048.local-storage :as ls]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [re-frame.core :as re-frame]
   [cljs-2048.animation :as animation]))

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

(defn clear-tile-moves
  [db [_ _]]
  (assoc db
         :tile-moves {}))

(re-frame/reg-event-db ::clear-tile-moves clear-tile-moves)

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
     :fx [[:dispatch-later {:ms 300 :dispatch [::clear-score-changes]}]]}))

(re-frame/reg-event-fx ::add-score add-score)

(defn move
  [{:keys [db]} [_ direction]]
  (let [board (:board db)
        new-board (g/move (:board db) direction)
        tile-moves (animation/calculate-moves board new-board)]
    {:db (assoc db
                :board new-board
                :tile-moves tile-moves)
    :fx (if (g/gameover? new-board)
          [[:dispatch [::gameover]]]

          [[:dispatch-later {:ms 300 :dispatch [::clear-tile-moves]}]
           (let [score (g/move-score new-board)]
             (when (pos? score)
               [:dispatch [::add-score score]]))])}))

(re-frame/reg-event-fx ::move move)
