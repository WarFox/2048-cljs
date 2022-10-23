(ns cljs-2048.styles
  (:require-macros
   [garden.def :refer [defcssfn]])
  (:require
   [spade.core   :refer [defglobal defclass]]
   [garden.units :refer [deg px]]
   [garden.color :refer [rgba]]))

(defglobal defaults
  [:body
   {:background-color :#ddd
    :font-family ["Lucida Console", :Monaco, :monospace]
    :color :#3A3A47
    :width :100%}])

(defclass level1
  []
  {:color :green})

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

(defclass board
  []
  {:background-color :#DDDDDD
   :border-radius    :0.5rem
   :padding          :0.5rem
   :position        :absolute
   :width            :25rem
   :margin           :0.25rem})
