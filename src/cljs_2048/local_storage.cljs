(ns cljs-2048.local-storage)

(defn set-item!
  "Set `key' in browser's localStorage to `val`."
  [key val]
  (.setItem (.-localStorage js/window) key val))

(defn get-item
  "Returns value of `key' from browser's localStorage."
  [key]
  (.getItem (.-localStorage js/window) key))

(defn set-high-score!
  [val]
  (set-item! ::high-score val))

(defn get-high-score
  []
  (max (get-item ::high-score) 0))
