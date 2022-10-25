(ns cljs-2048.events
  (:require
   [re-frame.core :as re-frame]
   [cljs-2048.db :as db]
   [cljs-2048.board :as b]
   [day8.re-frame.tracing :refer-macros [fn-traced]]))

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
      (assoc :board (b/with-two-random-tiles))
      (assoc :score 0))))

(re-frame/reg-event-db
 ::move-up
 (fn-traced
  [db [_ _]]
  (assoc db :board (b/new-board (:board db) ::b/up))))

(re-frame/reg-event-db
 ::move-down
 (fn-traced
  [db [_ _]]
  (assoc db :board (b/new-board (:board db) ::b/down))))

(re-frame/reg-event-db
 ::move-right
 (fn-traced
  [db [_ _]]
  (assoc db :board (b/new-board (:board db) ::b/right))))

(re-frame/reg-event-db
 ::move-left
 (fn-traced
  [db [_ _]]
  (assoc db :board (b/new-board (:board db) ::b/left))))
