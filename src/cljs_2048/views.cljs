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
(defn score
  [header value]
  [:div {:class "text-xl sm:text-2xl px-2 sm:px-4 text-center rounded-sm bg-brown-500"}
   [:div {:class "uppercase text-sm font-bold text-brown-200"} header]
   [:div {:class "text-white font-bold"} value]])

(defn btn-new-game
  []
  [:button {:type "button"
            :class "text-white bg-brown-600 hover:bg-brown-800 focus:ring-4 font-medium rounded-md text-sm px-5 py-2.5 focus:outline-none"
            :on-click #(re-frame/dispatch [::events/start-game])} "New Game"])

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

(defn tile
  "Displaying the game tile"
  [row-index col-index [value state]]
  ^{:key col-index}
   [:div {:class (str "transition-transform duration-300 ease-in-out tile-position-" row-index "-" col-index)} ;; TODO apply tile-position based on new position
   [:div {:class (str "text-5xl font-bold size-32 flex justify-center items-center rounded-md tile-" value
                      (cond
                        (= state :merged)
                        " tile-merged"

                        (= state :random)
                        " tile-new"

                        :else "")  )}
   (if (zero? value) "" value)]])

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
           (tile row-index col-index cell))
         row))
      board)]))

(defn grid-panel
  "display 4x4 grid of the game"
  []
  ;; absolute and z-0 to make the grid behind the board
  [:div {:class "bg-brown-600 z-0 grid grid-rows-4 grid-cols-4 gap-4 p-4 rounded-md"
         :id "grid-panel"}
   (map-indexed
    (fn [row-index _]
      ^{:key row-index}
       (map-indexed
       (fn [col-index _]
         ^{:key col-index}
          ;; size-32 makes the grid cells big
          [:div {:class "size-32 rounded-md bg-brown-500" } ])
       (range 4)))
    (range 4))])

(defn game-panel
  []
  (let [gameover @(re-frame/subscribe [::subs/gameover])]
    [:div {:class "container mx-auto mt-5"}
     (when gameover
       (gameover-panel))

     [:div {:class "flex flex-row justify-center mt-5 rounded-md"}
       [:div {:class "text-5xl sm:text-7xl font-bold text-stone-600"} "2048"]
       [:div {:class "flex space-x-1 mx-4" :id "score-panel"}
        (score ::score @(re-frame/subscribe [::subs/score]))
        (score ::high-score @(re-frame/subscribe [::subs/high-score]))]

      [:div
       [btn-new-game]]]

     [:div {:class "flex flex-row justify-center mt-8"}
      [grid-panel]
      [board-panel]]]))

(defn main-panel
  []
  [game-panel])
