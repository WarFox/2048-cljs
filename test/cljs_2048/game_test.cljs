(ns cljs-2048.game-test
  (:require [cljs-2048.game :as sut]
            [cljs.test :refer-macros [deftest testing is]]))

(deftest can-move-sideways?-test
  (is (true? (sut/can-move-sideways? [2 2 3 4])))
  (is (true? (sut/can-move-sideways? [1 3 3 4])))
  (is (true? (sut/can-move-sideways? [1 2 4 4])))
  (is (true? (sut/can-move-sideways? [2 2 2 4])))
  (is (true? (sut/can-move-sideways? [1 2 2 2])))
  (is (nil? (sut/can-move-sideways? [1 2 3 4]))))

(def can-move-sideways [[2 8 2 4]
                        [4 2 4 8]
                        [2 8 2 2]
                        [4 2 4 8]])

(def can-move-up-or-down [[2 8 2 4]
                          [4 2 4 8]
                          [2 8 2 4]
                          [4 8 4 8]])

(deftest can-move-test
  (is (true? (sut/can-move? can-move-sideways)))
  (is (true? (sut/can-move? can-move-up-or-down))))

(deftest gameover?-test
  (testing "game not over, when there can be move to left"
    (is (false? (sut/gameover? can-move-sideways))))

  (testing "game not over, when there can be move up"
    (is (false? (sut/gameover? can-move-up-or-down))))

  (testing "game over, when there can be no more moves"
    (is (true? (sut/gameover? [[2 4 2 4]
                               [4 2 4 2]
                               [2 4 2 4]
                               [4 2 4 2]])))))
