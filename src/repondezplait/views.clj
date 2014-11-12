(ns repondezplait.views
  (:require [hiccup.core :refer [html]]
            [hiccup.page :refer [html5]]))


(def index
  (html5 [:head
           [:meta {:charset "utf-8"}]
           [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge"}]
           [:title "Repondezplait"]
           [:meta {:name "description" :content ""}]
           [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
           [:link {:rel "stylesheet" :href "http://fonts.googleapis.com/css?family=Lato:300,400,700"}]
           [:link {:rel "stylesheet" :href "//cdnjs.cloudflare.com/ajax/libs/normalize/3.0.1/normalize.min.css"}]
           [:link {:rel "stylesheet" :href "/style.css"}]]
         [:body
           ;; [:div.site-header
           ;;   [:header
           ;;     [:a {:href "/"}
           ;;       [:img {:src "/images/logo.png"}]]
           ;;     [:a {:href "/"} "Home"]
           ;;     [:a {:href "/inventory"} "Inventory"]
           ;;     [:a {:href "/contact"} "Location / Contact"]]]
           ;; [:div.contents "shiiiii"]
           ;; [:footer " more shiiii "]
           ;; [:script {:src "/bower_components/flatui/js/jquery.placeholder.js"}]
          ]))

;; (def respond
;;   (html [:div.home
;;            "Thank you! Your response has been recorded and you may change it at any time."
;;            [:div.intro "Take a look around our fabulous site and enjoy the handiwork of our talented and devilishly handsome designer. Welcome to Delta Auto, we're not as skeezy as you might imagine! We're way more so!"]
;;            [:div.quote
;;              [:blockquote
;;                [:p "I bought a car from Delta Auto and it broke down seconds after I was off the lot. As god is my witness, I shall have vengence upon this miserable establishment and the wretches who inhabit it."]
;;                [:footer "Steve Jobs, CEO Apple"]]]
;;            [:img {:src "http://designmodo.github.io/Flat-UI/images/icons/svg/retina.svg"}]]))

