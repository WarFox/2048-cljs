(ns cljs-2048.components)

(defn footer
  []
  [:footer {:class "m-4"}
   [:div {:class "mx-auto p-4 md:py-8"}
    [:div {:class "text-sm text-gray-500 sm:text-center dark:text-gray-400"}
     "Play using arrow keys on your browser"]
    [:div {:class "text-sm text-gray-500 sm:text-center dark:text-gray-400"}
     "Implemented using "
        [:a {:href "https://day8.github.io/re-frame/re-frame/" :class "hover:underline"} "re-frame"]
        ", "
        [:a {:href "https://clojurescript.org/" :class "hover:underline"} "ClojureScript"]
        ", "
        [:a {:href "https://tailwindcss.com/" :class "hover:underline"} "tailwindcss"]
        ", and "
        [:a {:href "https://react.dev/" :class "hover:underline"} "React"]]
       [:div {:class "text-sm text-gray-500 sm:text-center dark:text-gray-400"}
        "© 2024 "
        [:a {:href "https://deepumohan.com/" :class "hover:underline"} "Deepu Mohan Puthrote"]
        ". All Rights Reserved."]]])
