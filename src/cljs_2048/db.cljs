(ns cljs-2048.db
  (:require [cljs-2048.board :as board]))

(def default-db
  {:name "2048"
   :score 0
   :high-score 0
   :board board/initial-board})
