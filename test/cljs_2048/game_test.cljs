(ns cljs-2048.game-test
  (:require [cljs-2048.game :as sut]
            [cljs-2048.board :as board]
            [cljs.test :refer-macros [deftest testing is]]))

(deftest gameover-test
  (testing "When there are NO empty tiles,"
    (with-redefs [board/empty-tiles (fn [_] [])]

      (testing "game over if old-board and new-board are same"
        (let [old-board ::board new-board ::board]
          (is (true? (sut/gameover? old-board new-board)))))

      (testing "NOT game over if old-board and new-board are same"
        (let [old-board ::old-board new-board ::new-board]
          (is (false? (sut/gameover? old-board new-board)))))))

  (testing "When there are empty tiles,"
    (with-redefs [board/empty-tiles (fn [_] [::not-empty])]

      (testing "game is NOT over if old-board and new-board are same"
        (let [old-board ::board new-board ::board]
          (is (false? (sut/gameover? old-board new-board)))))

      (testing "game is NOT over if old-board and new-board are same"
        (let [old-board ::old-board new-board ::new-board]
          (is (false? (sut/gameover? old-board new-board))))))))
