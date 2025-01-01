(ns cljs-2048.board-test
  (:require
   [cljs-2048.board :as board]
   [cljs.test :refer-macros [deftest testing is]]))

(deftest initial-board-test
  (testing "Inital board must be a 4x4 matrix filled with zeroes"
    (is (= board/initial-board
              [[[0] [0] [0] [0]]
               [[0] [0] [0] [0]]
               [[0] [0] [0] [0]]
               [[0] [0] [0] [0]]]))))

(deftest set-tile
  (let [board board/initial-board]
    (testing "Set value at given x and y"
      (is (= (board/set-tile board 1 2 4 :state)
             [[[0] [0] [0]        [0]]
              [[0] [0] [4 :state] [0]]
              [[0] [0] [0]        [0]]
              [[0] [0] [0]        [0]]])))))

(deftest get-tile
  (let [board (board/set-tile board/initial-board 1 3 8 :state)]
    (testing "Get tile value of given x and y"
      (is (= (board/get-tile board 1 3) [8 :state])))))

(deftest empty-tiles
  (is (empty? (board/empty-tiles [[[1] [2] [3] [4]]
                                [[5] [6] [7] [6]]
                                [[9] [7] [11] [12]]
                                [[13] [8] [15] [16]]])))

  (is (= (board/empty-tiles [[[1] [2] [3] [0]]
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
      (is (= (board/random-tile board)
             board)))))

;; Transpose Test
(def test-board
  [[1 2 3 4]
   [5 6 7 8]
   [9 10 11 12]
   [13 14 15 16]])

(deftest transpose-test
  (testing "flips the matrix diagonally so as row and column indices are switched"
    (is (= (board/transpose test-board)
           [[1 5 9  13]
            [2 6 10 14]
            [3 7 11 15]
            [4 8 12 16]]))))

(deftest rotate-left-test
  (is (= (board/rotate-left test-board)
         [[4 8 12 16]
          [3 7 11 15]
          [2 6 10 14]
          [1 5 9 13]])))

(deftest rotate-right-test
  (is (= (board/rotate-right test-board)
         [[13 9 5 1]
          [14 10 6 2]
          [15 11 7 3]
          [16 12 8 4]])))

;; End of transpose test

(deftest fill-zeroes-test
  (testing "fills empty vector with n zeroes"
    (is (= (board/fill-zeroes [] 10)
            [[0] [0] [0] [0] [0] [0] [0] [0] [0] [0]])))

  (testing "fills non-empty vector with no zeroes"
    (is (= (board/fill-zeroes [[2] [4]] 7)
           [[2] [4] [0] [0] [0] [0] [0]])))

  (testing "if vector size is = n, return the vector"
    (is (= (board/fill-zeroes [[2] [4]] 2)
           [[2] [4]])))

  (testing "if vector size is > n, return the vector"
    (is (= (board/fill-zeroes [1 2 3 4] 2)
           [1 2 3 4]))))

(deftest merge-left-test
  (is (= (board/merge-left [[2] [2] [0] [0]])
         [[4 :merged] [0] [0] [0]]))

  (is (= (board/merge-left [[4] [4] [2] [2]])
         [[8 :merged] [4 :merged] [0] [0]]))

  (is (= (board/merge-left [[1] [2] [3] [4]])
         [[1] [2] [3] [4]]))

  (is (= (board/merge-left [[4] [4] [4] [4]])
         [[8 :merged] [8 :merged] [0] [0]]))

  (is (= (board/merge-left [[2] [0] [4] [4]])
         [[2] [8 :merged] [0] [0]]))

  (is (= (board/merge-left [[0] [0] [4] [4]])
         [[8 :merged] [0] [0] [0]]))

  (is (= (board/merge-left [[4] [4] [0] [4]])
         [[8 :merged] [4] [0] [0]]))

  (is (= (board/merge-left [[4] [4] [8] [4]])
         [[8 :merged] [8] [4] [0]]))

  (is (= (board/merge-left [[4] [2] [2] [0]])
         [[4] [4 :merged] [0] [0]]))

  (testing "with random and merged"
    (is (= (board/merge-left [[4] [2 :random] [2 :merged] [0]])
         [[4] [4 :merged] [0] [0]])))

  (testing "map merge-left for a matrix"
    (is (= (map board/merge-left
                [[[4] [4] [2] [2]]
                 [[6] [4] [2] [2]]
                 [[8] [8] [2] [0]]
                 [[6] [8] [2] [2]]])
           [[[8 :merged]  [4 :merged] [0]         [0]]
            [[6]          [4]         [4 :merged] [0]]
            [[16 :merged] [2]         [0]         [0]]
            [[6]          [8]         [4 :merged] [0]]]))))

(deftest reverse-test
  (is (= (board/reverse-board test-board)
         [[4 3 2 1]
          [8 7 6 5]
          [12 11 10 9]
          [16 15 14 13]])))

(deftest equal?-test
  (is (true? (board/equal?
              [[[0] [0] [0] [0]]
               [[0] [0] [0] [0]]
               [[0] [0] [2 :random] [0]]
               [[4 :merged] [0] [4] [0]]]

              [[[0] [0] [0] [0]]
               [[0] [0] [0] [0]]
               [[0] [0] [2] [0]]
               [[4] [0] [4] [0]]])))

  (is (true? (board/equal?
              [[[0] [0] [0] [0]]
               [[0] [0] [0] [0]]
               [[0] [0] [2 :random] [0]]
               [[4 :merged] [0] [4] [0]]]

              [[[0] [0] [0] [0]]
               [[0] [0] [0] [0]]
               [[0] [0] [2] [0]]
               [[4] [0] [4] [0]]])))

  (is (false? (board/equal?
              [[[0] [0] [0] [0]]
               [[0] [0] [0] [0]]
               [[0] [0] [2 :random] [0]]
               [[4 :merged] [0] [4] [0]]]

              [[[0] [0] [0] [0]]
               [[0] [0] [0] [0]]
               [[2] [0] [0] [0]]
               [[4] [0] [4] [0]]]))))
