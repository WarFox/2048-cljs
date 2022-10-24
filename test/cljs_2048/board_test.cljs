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

(deftest compress-left
  (testing "Move all tiles to left - single column"
    (is (= (sut/compress-left
            [[0 2 4 6]
             [0 4 6 8]
             [0 6 8 10]
             [0 8 10 12]])
           [[2 4 6 0]
            [4 6 8 0]
            [6 8 10 0]
            [8 10 12 0]])))

  (testing "Move all tiles to left - two columns"
    (is (= (sut/compress-left
            [[0 0 4 6]
             [0 0 6 8]
             [0 0 8 10]
             [0 0 10 12]])
           [[4 6 0 0]
            [6 8 0 0]
            [8 10 0 0]
            [10 12 0 0]])))

  (testing "Move all tiles to left - mixed column"
    (is (= (sut/compress-left
            [[0 2 0 6]
             [0 4 0 8]
             [0 6 0 10]
             [0 8 0 12]])
           [[2 6 0 0]
            [4 8 0 0]
            [6 10 0 0]
            [8 12 0 0]]))))

(deftest move-tiles-left
  (testing "Compress array to left - three"
    (is (= (sut/move-tiles-left [0 2 4 6])
           [2 4 6 0])))

  (testing "Compress array to left - two"
    (is (= (sut/move-tiles-left [0 0 4 6])
           [4 6 0 0])))

  (testing "Compress array to left - single "
    (is (= (sut/move-tiles-left [0 0 0 6])
           [6 0 0 0])))

  (testing "Compress array to left - all zeros"
    (is (= (sut/move-tiles-left [0 0 0 0])
           [0 0 0 0])))

  (testing "Compress array to left - all numbers"
    (is (= (sut/move-tiles-left [2 4 6 8])
           [2 4 6 8])))

  (testing "Compress array to left - zero in the middle"
    (is (= (sut/move-tiles-left [2 4 0 8])
           [2 4 8 0])))

  (testing "Compress array to left - zeros in the middle"
    (is (= (sut/move-tiles-left [2 0 0 8])
           [2 8 0 0]))))

(deftest rotate-left-test
  (testing "rotate left"
    (is (= (sut/rotate-left
            [[0 0 0 0]
             [0 4 2 16]
             [0 2 4 8]
             [0 64 128 256]])
           [[0 16 8 256]
            [0 2 4 128]
            [0 4 2 64]
            [0 0 0 0]]))))

(deftest rotate-right-test
  (testing "rotate right"
    (is (= (sut/rotate-right
            [[0 0 0 0]
             [0 4 2 16]
             [0 2 4 8]
             [0 64 128 256]])
           [[0 0 0 0]
            [64 2 4 0]
            [128 4 2 0]
            [256 8 16 0]]))))


(def test-board
  [[0 0 0 0]
   [0 4 2 16]
   [0 2 4 8]
   [0 64 128 256]])

(reverse test-board)

;; https://techvidvan.com/tutorials/python-2048-game-project-with-source-code/

;; (apply map #(into [] %&) board)
