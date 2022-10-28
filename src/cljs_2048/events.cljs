(ns cljs-2048.events
  (:require
   [re-frame.core :as re-frame]
   [cljs-2048.db :as db]
   [cljs-2048.board :as board]
   [cljs-2048.game :as game]
   [day8.re-frame.tracing :refer-macros [fn-traced]]))

(defn check-gameover
  [new-board old-board]
  (when (game/gameover? new-board old-board)
    (re-frame/dispatch [::gameover]))
  new-board)

(defn- move
  "Move board and check gameover"
  [board direction]
  (-> board
      (board/move direction)
      (check-gameover board)))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced
  [_ _]
  db/default-db))

(re-frame/reg-event-db
 ::start-game
 (fn-traced
  [db [_ _]]
  (-> db
      (assoc :board (board/with-two-random-tiles))
      (assoc :gameover false)
      (assoc :score 0))))

(re-frame/reg-event-db
 ::move-up
 (fn-traced
  [db [_ _]]
  (assoc db :board (move (:board db) ::board/up))))

(re-frame/reg-event-db
 ::move-down
 (fn-traced
  [db [_ _]]
  (assoc db :board (move (:board db) ::board/down))))

(re-frame/reg-event-db
 ::move-right
 (fn-traced
  [db [_ _]]
  (assoc db :board (move (:board db) ::board/right))))

(re-frame/reg-event-db
 ::move-left
 (fn-traced
  [db [_ _]]
  (assoc db :board (move (:board db) ::board/left))))

(re-frame/reg-event-db
 ::gameover
 (fn-traced
  [db [_]]
  (assoc db :gameover true)))
