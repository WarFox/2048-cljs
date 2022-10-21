(ns cljs-2048.views
  (:require
   [re-frame.core :as re-frame]
   [re-pressed.core :as rp]
   [cljs-2048.events :as events]
   [cljs-2048.styles :as styles]
   [cljs-2048.subs :as subs]))

(defn dispatch-keydown-rules
  []
  (re-frame/dispatch
   [::rp/set-keydown-rules
    {:event-keys [[[::events/set-re-pressed-example "Hello, world!"]
                   [{:keyCode 72} ;; h
                    {:keyCode 69} ;; e
                    {:keyCode 76} ;; l
                    {:keyCode 76} ;; l
                    {:keyCode 79} ;; o
                    ]]]

     :clear-keys
     [[{:keyCode 27} ;; escape
       ]]}]))

;; Displaying the game cell
(defn cellcolors [cellvalue]
  (case cellvalue
    0         "#BBBBBB"
    2         "#7fbfc7"
    4         "#82b4c2"
    8         "#86aabc"
    16        "#899fb7"
    32        "#8c95b1"
    64        "#908aac"
    128       "#937fa7"
    256       "#9675a1"
    512       "#996a9c"
    1024      "#9d5f97"
    2048      "#a05591"
    4096      "#a34a8c"
    8192      "#a74086"
    "default" "#aa3581"))

(defn cell-panel [index x]
  ^{:key index}
  [:div {:class (styles/board-cell (cellcolors x))}
   (if (= x 0) "" x)])

;; Panel used to show Score and Best score
(defn score-panel [header value]
  [:div.score
   [:h3 header]
   [:p value]])

;; Displaying the game board with the cells in it
(defn board-panel []
  (let [board (re-frame/subscribe [::subs/board])] ;; Get the current state of the board
    [:div {:class (styles/board)}
     ;; (cljs.pprint/pprint board) ; debugging to console.log the board data
     (map-indexed ;; Each row of the board
      (fn [index x]
        ^{:key index}
        [:div {:class (styles/board-row)}
         (map-indexed ;; Each cell of the row
          cell-panel
          x)])
      @board)
     [:br.clear]]))

(defn display-game []
  (let [score (re-frame/subscribe [::subs/score])
        high-score (re-frame/subscribe [::subs/high-score])]
    [:div#game-panel
     (score-panel "Score" @score)
     (score-panel "High Score" @high-score)
     [:button {:on-click #(re-frame/dispatch [::events/start-game])} "New Game"]
     [:br.clear]
     (board-panel)]))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [:h1 @name]
     [display-game]]))
