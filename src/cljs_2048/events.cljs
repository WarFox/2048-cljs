(ns cljs-2048.events
  (:require
   [cljs-2048.board :as b]
   [cljs-2048.db :as db]
   [cljs-2048.game :as g]
   [cljs-2048.game-events :as game-events]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [re-frame.core :as re-frame]))

(defn check-gameover
  "Dispatch gameover if gameover. Returns the board"
  [board]
  (when (g/gameover? board)
    (re-frame/dispatch [::game-events/gameover]))
  board)

(defn- move
  "Make a move in the direction and check gameover"
  [board direction]
  (-> board
      (g/move direction)
      (check-gameover)))

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

(defn move-up
  [{:keys [board] :as db} [_ _]]
  (assoc db
         :board (move board ::g/up)))

(defn move-down
  [{:keys [board] :as db} [_ _]]
  (assoc db
         :board (move board ::g/down)))

(defn move-right
  [{:keys [board] :as db} [_ _]]
  (assoc db
         :board (move board ::g/right)))

(defn move-left
  [{:keys [board] :as db} [_ _]]
  (assoc db
         :board (move board ::g/left)))

(re-frame/reg-event-db ::start-game start-game)

(re-frame/reg-event-db ::move-up move-up)

(re-frame/reg-event-db ::move-down move-down)

(re-frame/reg-event-db ::move-right move-right)

(re-frame/reg-event-db ::move-left move-left)
