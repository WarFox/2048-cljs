(ns cljs-2048.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::board
 (fn [db _]
   (:board db)))

(re-frame/reg-sub
 ::score
 (fn [db _]
   (:score db)))

(re-frame/reg-sub
 ::high-score
 (fn [db _]
   (:high-score db)))

(re-frame/reg-sub
 ::gameover
 (fn [db _]
   (:gameover db)))

(re-frame/reg-sub
 ::score-changed
 (fn [db _]
   (:score-changed db)))

(re-frame/reg-sub
 ::high-score-changed
 (fn [db _]
   (:high-score-changed db)))
