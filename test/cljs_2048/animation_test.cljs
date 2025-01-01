(ns cljs-2048.animation-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [cljs-2048.game :as g]
            [cljs-2048.animation :as animation]))

(deftest get-transition-style-test
  (testing "calculates transition style 0 0"
    (let [cell [0 0]]
      (is (= (animation/get-transition-style cell ::g/up)
             {"--move-x" "0em" "--move-y" "0em"}))
      (is (= (animation/get-transition-style cell ::g/left)
             {"--move-x" "0em" "--move-y" "0em"}))
      (is (= (animation/get-transition-style cell ::g/right)
             {"--move-x" "24em" "--move-y" "0em"}))
      (is (= (animation/get-transition-style cell ::g/down)
             {"--move-x" "0em" "--move-y" "24em"}))))

  (testing "calculates transition style 3 3"
    (let [cell [3 3]]
      (is (= (animation/get-transition-style cell ::g/up)
             {"--move-x" "0em" "--move-y" "-24em"}))
      (is (= (animation/get-transition-style cell ::g/left)
             {"--move-x" "-24em" "--move-y" "0em"}))
      (is (= (animation/get-transition-style cell ::g/right)
             {"--move-x" "0em" "--move-y" "0em"}))
      (is (= (animation/get-transition-style cell ::g/down)
             {"--move-x" "0em" "--move-y" "0em"}))))

  (testing "calculates transition style 2 1"
    (let [cell [2 1]]
      (is (= (animation/get-transition-style cell ::g/up)
             {"--move-x" "0em" "--move-y" "-16em"}))
      (is (= (animation/get-transition-style cell ::g/left)
             {"--move-x" "-8em" "--move-y" "0em"}))
      (is (= (animation/get-transition-style cell ::g/right)
             {"--move-x" "16em" "--move-y" "0em"}))
      (is (= (animation/get-transition-style cell ::g/down)
             {"--move-x" "0em" "--move-y" "8em"})))))
