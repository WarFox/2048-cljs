(ns cljs-2048.animation
  (:require [cljs-2048.board :as b]
            [cljs.spec.alpha :as s]))

(def board-size 4)  ; Add constant for magic number 4

(s/def ::position (s/tuple int? int?))
(s/def ::moves (s/map-of ::position ::position))

(defn board->tile-positions
  "Creates a map of positions to tile values, excluding empty tiles"
  [board]
  (->> (for [row (range board-size)
             col (range board-size)
             :let [tile (b/get-tile board row col)]
             :when (not= [0] tile)]
         [[row col] (first tile)])
       (into {})))

(defn find-tile-destination
  "Find the destination position for a tile value in the new board.
   Returns [row col] of destination or nil if no move."
  [value old-pos new-board]
  (->> (for [row (range board-size)
             col (range board-size)
             :let [new-val (first (b/get-tile new-board row col))]
             :when (and (not= [row col] old-pos)
                       (or (= new-val value)
                           (= new-val (* 2 value))))]
         [row col])
       first))

(defn calculate-moves
  "Calculate the moves for each tile from old board to new board.
   Returns a map of {[old-row old-col] [new-row new-col]} for each moved tile."
  [old-board new-board]
  (if (= old-board new-board)
    {}
    (let [old-positions (board->tile-positions old-board)]
      (->> old-positions
           (keep (fn [[old-pos value]]
                  (when-let [new-pos (find-tile-destination value old-pos new-board)]
                    [old-pos new-pos])))
           (into {})))))

(defn get-transition-class
  "Get the CSS class for tile position"
  [[row col]]
  (str "translate-x-" (* col 100) "% translate-y-" (* row 100) "%"))

(defn get-transition-style
  "Get the CSS class for tile position"
  [[old-row old-col] [new-row new-col]]
  {"--move-x" (str (* (- new-row old-row) 8) "em")
   "--move-y" (str (* (- new-col old-col) 8) "em")})
