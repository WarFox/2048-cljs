(ns cljs-2048.board-test
  (:require
   [cljs-2048.board :as sut]
   [cljs.test :refer-macros [deftest testing is]]))

(deftest initial-board-test
  (testing "Inital board must be a 4x4 matrix filled with zeroes"
    (is (= sut/initial-board
              [[[0] [0] [0] [0]]
               [[0] [0] [0] [0]]
               [[0] [0] [0] [0]]
               [[0] [0] [0] [0]]]))))

(deftest set-tile
  (let [board sut/initial-board]
    (testing "Set value at given x and y"
      (is (= (sut/set-tile board 1 2 4 :state)
             [[[0] [0] [0]        [0]]
              [[0] [0] [4 :state] [0]]
              [[0] [0] [0]        [0]]
              [[0] [0] [0]        [0]]])))))

(deftest get-tile
  (let [board (sut/set-tile sut/initial-board 1 3 8 :state)]
    (testing "Get tile value of given x and y"
      (is (= (sut/get-tile board 1 3) [8 :state])))))

(deftest empty-tiles
  (is (= (sut/empty-tiles [[[1] [2] [3] [0]]
                           [[0] [6] [7] [0]]
                           [[9] [0] [11] [12]]
                           [[13] [0] [15] [16]]])
         [[0 3] [1 0] [1 3] [2 1] [3 1]])))

(deftest random-tile-test
  (testing "random-tile returns board if board is full"
    (let [board [[[1] [2] [3] [4]]
                 [[5] [6] [7] [8]]
                 [[5] [6] [7] [8]]
                 [[5] [6] [7] [8]]]]
      (is (= (sut/random-tile board)
             board)))))

;; Transpose Test
(def test-board
  [[1 2 3 4]
   [5 6 7 8]
   [9 10 11 12]
   [13 14 15 16]])

(deftest transpose-test
  (testing "flips the matrix diagonally so as row and column indices are switched"
    (is (= (sut/transpose test-board)
           [[1 5 9  13]
            [2 6 10 14]
            [3 7 11 15]
            [4 8 12 16]]))))

(deftest rotate-left-test
  (is (= (sut/rotate-left test-board)
         [[4 8 12 16]
          [3 7 11 15]
          [2 6 10 14]
          [1 5 9 13]])))

(deftest rotate-right-test
  (is (= (sut/rotate-right test-board)
         [[13 9 5 1]
          [14 10 6 2]
          [15 11 7 3]
          [16 12 8 4]])))

;; End of transpose test

(deftest fill-zeroes-test
  (testing "fills empty vector with n zeroes"
    (is (= (sut/fill-zeroes [] 10)
            [[0] [0] [0] [0] [0] [0] [0] [0] [0] [0]])))

  (testing "fills non-empty vector with no zeroes"
    (is (= (sut/fill-zeroes [[2] [4]] 7)
           [[2] [4] [0] [0] [0] [0] [0]])))

  (testing "if vector size is = n, return the vector"
    (is (= (sut/fill-zeroes [[2] [4]] 2)
           [[2] [4]])))

  (testing "if vector size is > n, return the vector"
    (is (= (sut/fill-zeroes [1 2 3 4] 2)
           [1 2 3 4]))))

(deftest combine-test
  (is (= (sut/combine [[2] [2] [0] [0]])
         [[4 :merged] [0] [0] [0]]))

  (is (= (sut/combine [[4] [4] [2] [2]])
         [[8 :merged] [4 :merged] [0] [0]]))

  (is (= (sut/combine [[1] [2] [3] [4]])
         [[1] [2] [3] [4]]))

  (is (= (sut/combine [[4] [4] [4] [4]])
         [[8 :merged] [8 :merged] [0] [0]]))

  (is (= (sut/combine [[2] [0] [4] [4]])
         [[2] [8 :merged] [0] [0]]))

  (is (= (sut/combine [[0] [0] [4] [4]])
         [[8 :merged] [0] [0] [0]]))

  (is (= (sut/combine [[4] [4] [0] [4]])
         [[8 :merged] [4] [0] [0]]))

  (is (= (sut/combine [[4] [4] [8] [4]])
         [[8 :merged] [8] [4] [0]]))

  (is (= (sut/combine [[4] [2] [2] [0]])
         [[4] [4 :merged] [0] [0]]))

  (testing "map combine for a matrix"
    (is (= (map sut/combine
                [[[4] [4] [2] [2]]
                 [[6] [4] [2] [2]]
                 [[8] [8] [2] [0]]
                 [[6] [8] [2] [2]]])
           [[[8 :merged]  [4 :merged] [0]         [0]]
            [[6]          [4]         [4 :merged] [0]]
            [[16 :merged] [2]         [0]         [0]]
            [[6]          [8]         [4 :merged] [0]]]))))

(deftest reverse-test
  (is (= (sut/reverse-board test-board)
         [[4 3 2 1]
          [8 7 6 5]
          [12 11 10 9]
          [16 15 14 13]])))

(deftest stack-left
  (testing "Move all tiles to left - single column"
    (is (= (sut/stack-left
            [[[0] [2] [4] [6]]
             [[0] [4] [6] [8]]
             [[0] [6] [8] [10]]
             [[0] [8] [10] [12]]])
           [[[2] [4] [6] [0]]
            [[4] [6] [8] [0]]
            [[6] [8] [10] [0]]
            [[8] [10] [12] [0]]])))

  (testing "Move all tiles to left - two columns"
    (is (= (sut/stack-left
            [[[0] [0] [4] [6]]
             [[0] [0] [6] [8]]
             [[0] [0] [8] [10]]
             [[0] [0] [10] [12]]])
           [[[4] [6] [0] [0]]
            [[6] [8] [0] [0]]
            [[8] [10] [0] [0]]
            [[10] [12] [0] [0]]])))

  (testing "Move all tiles to left - mixed column"
    (is (= (sut/stack-left
            [[[0] [2] [0] [6]]
             [[0] [4] [0] [8]]
             [[0] [6] [0] [10]]
             [[0] [8] [0] [12]]])
           [[[2] [6] [0] [0]]
            [[4] [8] [0] [0]]
            [[6] [10] [0] [0]]
            [[8] [12] [0] [0]]]))))

(deftest move-tiles-left
  (testing "Stack vector to left - three"
    (is (= (sut/move-tiles-left [[0] [2] [4] [6]])
           [[2] [4] [6] [0]])))

  (testing "Stack vector to left - two"
    (is (= (sut/move-tiles-left [[0] [0] [4] [6]])
           [[4] [6] [0] [0]])))

  (testing "Stack vector to left - single "
    (is (= (sut/move-tiles-left [[0] [0] [0] [6]])
           [[6] [0] [0] [0]])))

  (testing "Stack vector to left - all zeroes"
    (is (= (sut/move-tiles-left [[0] [0] [0] [0]])
           [[0] [0] [0] [0]])))

  (testing "Stack vector to left - all numbers"
    (is (= (sut/move-tiles-left [[2] [4] [6] [8]])
           [[2] [4] [6] [8]])))

  (testing "Stack vector to left - zero in the middle"
    (is (= (sut/move-tiles-left [[2] [4] [0] [8]])
           [[2] [4] [8] [0]])))

  (testing "Stack vector to left - zeroes in the middle"
    (is (= (sut/move-tiles-left [[2] [0] [0] [8]])
           [[2] [8] [0] [0]]))))
