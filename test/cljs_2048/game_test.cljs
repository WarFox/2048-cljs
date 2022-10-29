(ns cljs-2048.game-test
  (:require [cljs-2048.board :as board]
            [cljs-2048.game :as sut]
            [cljs.test :refer-macros [deftest testing is]]))

(deftest gameover?-test
  (testing "When move-left produces identical board"
    (with-redefs [board/move-left identity
                  board/move-up identity]

      (testing "When there are NO empty tiles,"
        (with-redefs [board/empty-tiles (fn [_] [])]

          (testing "game over"
            (is (true? (sut/gameover? ::board))))))

      (testing "When there are empty tiles,"
        (with-redefs [board/empty-tiles (fn [_] [::not-empty])]

          (testing "game is NOT over if old-board and new-board are same"
            (is (false? (sut/gameover? ::board))))))))

  (testing "game not over, when there can be move to left"
    (is (false? (sut/gameover? [[2 2 4 4]
                                [8 4 8 2]
                                [2 8 2 4]
                                [4 16 4 8]]))))

  (testing "game not over, when there can be move to right"
    (is (false? (sut/gameover? [[2 8 4 4]
                                [8 4 8 2]
                                [2 8 2 4]
                                [4 16 4 8]]))))

  (testing "game not over, when there can be move up"
    (is (false? (sut/gameover? [[2 8 2 4]
                                [8 4 8 4]
                                [2 8 2 4]
                                [4 16 4 8]]))))

  (testing "game not over, when there can be move down"
    (is (false? (sut/gameover? [[2 8 2 4]
                                [8 4 8 4]
                                [2 8 2 4]
                                [4 16 4 8]]))))

  (testing "game over, when there can be no more moves"
    (is (true? (sut/gameover? [[2 4 2 4]
                               [4 2 4 2]
                               [2 4 2 4]
                               [4 2 4 2]])))))
