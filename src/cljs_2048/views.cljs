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

;; Displaying the game tile
(defn tile-panel
  [row-index col-index [value state]]
  ^{:key col-index}
   [:div {:class (str "tile tile-" value " tile-position-" (inc row-index) "-" (inc col-index)
                      (cond
                        (= state :merged)
                        " tile-merged"

                        (= state :random)
                        " tile-new"

                        :else ""))}
   (if (zero? value) "" value)])

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

(defn board-panel
  "Rows and columns display of current board"
  []
  (let [board (re-frame/subscribe [::subs/board])] ;; Get the current state of the board
    [:div.board
     ;; [:pre (with-out-str (cljs.pprint/pprint @board))] ;; print the board in page for debugging
     (map-indexed
      (fn [row-index row] ;; Each row of the board
            ^{:key row-index}
            [:div.row
             (map-indexed (fn [col-index cell]
                            (tile-panel row-index col-index cell))
                          row)]);; Each tile of the row
          @board)
     [:br.clear]]))

(defn score
  []
  (let [score (re-frame/subscribe [::subs/score])
        high-score (re-frame/subscribe [::subs/high-score])]
    [:div {:class "flex flex-row-reverse"}
     (score-panel ::high-score @high-score)
     (score-panel ::score @score)]))

(defn gameover-panel []
  [:div {:class "relative flex justify-center items-center z-20"}
   [:div {:class "w-full h-full bg-gray-900 bg-opacity-60 top-0 fixed sticky-0"}
    [:div {:class "2xl:container  2xl:mx-auto py-48 px-4 md:px-28 flex justify-center items-center"}
     [:div {:class "w-96 md:w-auto relative flex flex-col justify-center items-center bg-white bg-opacity-60 py-16 px-4 md:px-24 xl:py-24 xl:px-36"}
      [:div
       [:h1 {:role "main" :class "text-3xl dark:text-white lg:text-4xl font-semibold text-center text-gray-800"}
        "Game Over!"]]

      (score)

      [:div {:class "flex flex-col justify-center items-center"}
       [:button.btn-primary {:on-click #(re-frame/dispatch [::events/start-game])} "New Game"]]]]]])

(defn game-panel
  []
  (let [gameover (re-frame/subscribe [::subs/gameover])]
    [:div {:class "flex flex-col items-center"}
     (when @gameover (gameover-panel))
     [:button.btn-primary {:on-click #(re-frame/dispatch [::events/start-game])} "New Game"]
     [board-panel]]))

(defn header
  [name]
  [:h2.header name])

(defn main-panel
  []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div {:class "relative flex min-h-screen flex-col justify-center overflow-hidden bg-gray-50 py-6 sm:py-12"}
     [:div {:class "container mx-auto"}
     (header @name)
     [score]]
     [game-panel]]))
