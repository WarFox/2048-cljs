(ns cljs-2048.animation-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [cljs-2048.animation :as animation]))

(deftest calculate-moves-test
  (testing "calculates horizontal moves"
    (testing "single tile moving right"
      (let [old-board [[[2] [0] [0] [0]]
                      [[0] [0] [0] [0]]
                      [[0] [0] [0] [0]]
                      [[0] [0] [0] [0]]]
            new-board [[[0] [0] [0] [2]]
                      [[0] [0] [0] [0]]
                      [[0] [0] [0] [0]]
                      [[0] [0] [0] [0]]]
            expected {[0 0] [0 3]}]
        (is (= expected (animation/calculate-moves old-board new-board)))))

    (testing "multiple tiles moving right"
      (let [old-board [[[2] [4] [0] [0]]
                      [[0] [0] [0] [0]]
                      [[0] [0] [0] [0]]
                      [[0] [0] [0] [0]]]
            new-board [[[0] [0] [2] [4]]
                      [[0] [0] [0] [0]]
                      [[0] [0] [0] [0]]
                      [[0] [0] [0] [0]]]
            expected {[0 0] [0 2]
                     [0 1] [0 3]}]
        (is (= expected (animation/calculate-moves old-board new-board))))))

  (testing "calculates vertical moves"
    (testing "single tile moving down"
      (let [old-board [[[2] [0] [0] [0]]
                      [[0] [0] [0] [0]]
                      [[0] [0] [0] [0]]
                      [[0] [0] [0] [0]]]
            new-board [[[0] [0] [0] [0]]
                      [[0] [0] [0] [0]]
                      [[0] [0] [0] [0]]
                      [[2] [0] [0] [0]]]
            expected {[0 0] [3 0]}]
        (is (= expected (animation/calculate-moves old-board new-board)))))

    (testing "multiple tiles moving down"
      (let [old-board [[[2] [0] [0] [0]]
                      [[4] [0] [0] [0]]
                      [[0] [0] [0] [0]]
                      [[0] [0] [0] [0]]]
            new-board [[[0] [0] [0] [0]]
                      [[0] [0] [0] [0]]
                      [[2] [0] [0] [0]]
                      [[4] [0] [0] [0]]]
            expected {[0 0] [2 0]
                     [1 0] [3 0]}]
        (is (= expected (animation/calculate-moves old-board new-board))))))

  (testing "handles merging tiles"
    (let [old-board [[[2] [2 :random] [0] [0]]
                    [[0] [0] [0] [0]]
                    [[0] [0] [0] [0]]
                    [[0] [0] [0] [0]]]
          new-board [[[0] [0] [0] [4 :merged]]
                    [[0] [0] [0] [0]]
                    [[0] [0] [0] [0]]
                    [[0] [0] [0] [0]]]
          expected {[0 0] [0 3]
                   [0 1] [0 3]}]  ; Both tiles should move to the same position
      (is (= expected (animation/calculate-moves old-board new-board)))))

(testing "handles merging and moving tiles"
    (let [old-board [[[2] [2] [0] [0]]
                    [[0] [0] [0] [0]]
                    [[0] [0] [0] [0]]
                    [[4] [0] [0] [0]]]
          new-board [[[0] [0] [0] [4 :merged]]
                    [[0] [0] [0] [0]]
                    [[0] [0] [0] [0]]
                    [[0] [0] [0] [4]]]
          expected {[0 0] [0 3]
                   [0 1] [0 3]
                   [3 0] [3 3] }]  ; Both tiles should move to the same position
      (is (= expected (animation/calculate-moves old-board new-board)))))

  (testing "handles no movement"
    (let [board [[[2] [4] [0] [0]]
                [[0] [0] [0] [0]]
                [[0] [0] [0] [0]]
                [[0] [0] [0] [0]]]
          expected {}]
      (is (= expected (animation/calculate-moves board board)))))

  (testing "handles empty board"
    (let [empty-board [[[0] [0] [0] [0]]
                      [[0] [0] [0] [0]]
                      [[0] [0] [0] [0]]
                      [[0] [0] [0] [0]]]
          expected {}]
      (is (= expected (animation/calculate-moves empty-board empty-board))))))
