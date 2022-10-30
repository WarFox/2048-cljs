(ns cljs-2048.db
  (:require
   [cljs-2048.board :as board]
   [cljs-2048.local-storage :as ls]))

(def default-db
  {:name "2048"
   :score 0
   :high-score (ls/get-high-score)
   :board board/initial-board})
