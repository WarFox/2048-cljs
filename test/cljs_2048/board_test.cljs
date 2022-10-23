(ns cljs-2048.board-test
  (:require [cljs-2048.board :as sut]
            [cljs.test :as t]
            [cljs.test :refer-macros [deftest testing is]]
            [clojure.set :as set]))

(deftest initial-board-test
  (testing "Initial board must be 4x4"
    (is (= (count sut/initial-board) 4))
    (is (= (count (nth sut/initial-board 0)) 4))
    (is (= (count (nth sut/initial-board 1)) 4))
    (is (= (count (nth sut/initial-board 2)) 4))
    (is (= (count (nth sut/initial-board 3)) 4)))

  (testing "Inital board must be filled with zeros"
    (is (true? (every? zero? (nth sut/initial-board 0))))))

(deftest set-tile
  (let [board sut/initial-board]
    (testing "Set value at given x and y"
      (is (= (sut/set-tile board 1 2 4)
             [[0 0 0 0]
              [0 0 4 0]
              [0 0 0 0]
              [0 0 0 0]])))))

(deftest get-tile
  (let [board (sut/set-tile sut/initial-board 1 3 8)]
    (testing "Get tile value of given x and y"
      (is (= (sut/get-tile board 1 3) 8)))))
