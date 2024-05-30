(ns cljs-2048.views
  (:require
   [cljs-2048.events :as events]
   [cljs-2048.subs :as subs]
   [re-frame.core :as re-frame]
   [re-pressed.core :as rp]))

(defn dispatch-keydown-rules
  []
  (re-frame/dispatch
   [::rp/set-keydown-rules
    {:event-keys [[[::events/move-left]
                   [{:keyCode 37}]] ;; lef-arrow

                  [[::events/move-up]
                   [{:keyCode 38}]] ;; up-arrow

                  [[::events/move-right]
                   [{:keyCode 39}]] ;; lef-arrow

                  [[::events/move-down]
                   [{:keyCode 40}]] ;; down-arrow
                  ]
     :clear-keys
     [[{:keyCode 27} ;; escape
       ]]}]))

;; Panel used to show Score and Best score
(defn score-panel
  [type value]
  (let [[header class] (if (= type ::high-score)
                         ["High Score" "score score-high"]
                         ["Score" "score"])]
    [:div {:class class
           :aria-orientation "vertical" :aria-labelledby "mobile-menu-button"}
     [:span {:class "block px-4 py-2 text-lg text-gray-700"}
      [:h3 header]
      value]]))


(defn score
  []
  (let [score @(re-frame/subscribe [::subs/score])
        high-score @(re-frame/subscribe [::subs/high-score])]
    [:div {:class "flex flex-row" :id "score-panel"}
     (score-panel ::score score)
     (score-panel ::high-score high-score)]))

(defn btn-new-game
  []
  [:button {:class "text-white bg-zinc-700 hover:bg-zinc-800 focus:ring-4 focus:ring-zinc-300 font-medium rounded-lg text-sm px-5 py-2.5 me-2 mb-2 focus:outline-none"
            :on-click #(re-frame/dispatch [::events/start-game])}
   "New Game"])

(defn gameover-panel []
  [:div {:class "relative flex justify-center items-center z-20"}
   [:div {:class "w-full h-full bg-gray-900 bg-opacity-60 top-0 fixed sticky-0"}
    [:div {:class "2xl:container  2xl:mx-auto py-48 px-4 md:px-28 flex justify-center items-center"}
     [:div {:class "w-96 md:w-auto relative flex flex-col justify-center items-center bg-white bg-opacity-60 py-16 px-4 md:px-24 xl:py-24 xl:px-36"}
      [:div
       [:h1 {:role "main" :class "text-3xl dark:text-white lg:text-4xl font-semibold text-center text-gray-800"}
        "Game Over!"]]

      [:div {:class "flex flex-col justify-center items-center"}
       [btn-new-game]]]]]])

(defn header
  []
  [:header
   [:h2 {:class "text-4xl font-bold leading-8 sm:truncate sm:text-3xl sm:tracking-tight"} "2048"]])

(defn tile-panel
  "Displaying the game tile"
  [row-index col-index [value state]]
  ^{:key col-index}
   [:div {:class (str "text-5xl font-bold size-32 flex justify-center items-center rounded-md tile-" value
                      (cond
                        (= state :merged)
                        " tile-merged"

                        (= state :random)
                        " tile-new"

                        :else "")  )}
   (if (zero? value) "" value)])

(defn board-panel
  "Rows and columns display of current board"
  []
  (let [board @(re-frame/subscribe [::subs/board])] ;; Get the current state of the board
    ;; absolute and z-10 to make the board on top of the grid
    [:div {:class "absolute z-10 grid grid-rows-4 grid-cols-4 gap-4 p-4" :id "board-panel"}
     ;; [:pre (with-out-str (cljs.pprint/pprint @board))] ;; print the board in page for debugging
     (map-indexed
      (fn [row-index row] ;; Each row of the board
        ^{:key row-index}
         (map-indexed
         (fn [col-index cell]
           (tile-panel row-index col-index cell))
         row))
      board)]))

(defn grid-panel
  "display 4x4 grid of the game"
  []
  ;; absolute and z-0 to make the grid behind the board
  [:div {:class "absolute z-0 grid grid-rows-4 grid-cols-4 gap-4 p-4 rounded-md" :style { :background-color "rgb(187, 173, 160)" } :id "grid-panel"}
   (map-indexed
    (fn [row-index row]
      ^{:key row-index}
       (map-indexed
       (fn [col-index cell]
         ^{:key col-index}
          ;; size-32 makes the grid cells big
          [:div {:class "size-32 rounded-md " :style { :background-color "rgba(238, 228, 218, 0.35)" }} ])
       (range 4)))
    (range 4))])

(defn game-panel
  []
  (let [gameover @(re-frame/subscribe [::subs/gameover])]
    [:div {:class "container mx-auto mt-2 px-4 sm:px-0"}
     (when gameover
       (gameover-panel))
     [header]
     [btn-new-game]
     [score]
     [:div {:class "flex flex-row"}
       [grid-panel]
       [board-panel]]]))

(defn main-panel
  []
  [game-panel])
