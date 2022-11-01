(ns cljs-2048.styles
  (:require
   [spade.core :refer [defglobal defclass defkeyframes]]))

(defglobal defaults
  [:body
   {:background-color :#ddd
    :font-family ["Lucida Console", :Monaco, :monospace]
    :color :#3A3A47
    :width :100%}])

(defclass board-row
  []
  {:clear :both})

(defclass board-tile
  [bg-color]
  {:float            :left
   :width            :5rem
   :height           :3.5rem
   :margin           :0.125rem
   :padding          :0.5rem
   :text-align       :center
   :font-size        :2rem
   :background-color bg-color
   :border-radius    :0.25rem
   :padding-top      :1.5rem})

(defkeyframes anim-frames []
  ["0%" {:opacity 0}]
  ["100%" {:opacity 1}])

(defclass new-tile
  []
  :animation [[(anim-frames) "560ms" 'ease-in-out]])

(defclass board
  []
  {:background-color :#DDDDDD
   :border-radius    :0.5rem
   :padding          :0.5rem
   :position        :absolute
   :width            :25rem
   :margin           :0.25rem})
