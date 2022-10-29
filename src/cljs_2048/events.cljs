(ns cljs-2048.events
  (:require
   [cljs-2048.board :as board]
   [cljs-2048.db :as db]
   [cljs-2048.game :as game]
   [cljs-2048.game-events :as game-events]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [re-frame.core :as re-frame]))

(defn- move
  "Move board and check gameover"
  [board direction]
  (let [new-board (board/move board direction)]
    (when (game/gameover? new-board)
      (re-frame/dispatch [::game-events/gameover]))
    new-board))

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
