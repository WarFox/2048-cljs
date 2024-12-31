(ns cljs-2048.events
  (:require
   [cljs-2048.board :as b]
   [cljs-2048.db :as db]
   [cljs-2048.game :as g]
   [cljs-2048.game-events :as game-events]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [re-frame.core :as re-frame]))

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

(defn move
  [{:keys [db]} [_ direction]]
  (let [new-board (g/move (:board db) direction)]
    {:db (assoc db
                :board new-board)
     :fx [(when (g/gameover? new-board) [:dispatch [::game-events/gameover]])]}))

(re-frame/reg-event-fx ::move move)
