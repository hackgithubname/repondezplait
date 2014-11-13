(ns repondezplait.views
  (:require ; [hiccup.core :refer [html]]
            [hiccup.page :refer [html5]]))


(defn template [& content]
  (html5 [:head
           [:meta {:charset "utf-8"}]
           [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge"}]
           [:title "Repondezplait!"]
           [:meta {:name "description" :content ""}]
           [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
           [:link {:rel "stylesheet" :href "http://fonts.googleapis.com/css?family=Lato:300,400,700"}]
           ;; [:link {:rel "stylesheet" :href "//cdnjs.cloudflare.com/ajax/libs/normalize/3.0.1/normalize.min.css"}]
           [:link {:rel "stylesheet" :href "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css"}]
           [:link {:rel "stylesheet" :href "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap-theme.min.css"}]
           [:link {:rel "stylesheet" :href "/style.css"}]]
         [:body
           [:div.container
             content
             ;; [:div.site-header
             ;;   [:header
             ;;     [:a {:href "/"}
             ;;       [:img {:src "/images/logo.png"}]]
             ;;     [:a {:href "/"} "Home"]
             ;;     [:a {:href "/inventory"} "Inventory"]
             ;;     [:a {:href "/contact"} "Location / Contact"]]]
             ;; [:div.contents "shiiiii"]
             [:footer.footer
               [:p "© Nora Hamada 2014"]]]
           [:script {:src "https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"}]
           [:script {:src "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"}]]))

(def respond (template
  [:div.respond
    [:div.jumbotron
      [:h1 "Thank you!"]
      [:p.lead "Your response has been recorded and you may change it at any time. You have pleased Nora, high archon of technical recruiting; prepare to recieve her boon."]]]))

(defn responses [entries] (template
  [:nav.navbar.navbar-default
    [:container-fluid
      [:navbar-header
        [:span.navbar-brand "Repondezplait!"]]]]
  [:table.table.table-hover
    [:thead
      [:tr
        [:th "Sent At"]
        [:th "Sender"]
        [:th "Recipient"]
        [:th "Text Preview"]
        [:th.spacer]
        [:th "Answered At"]
        [:th "Answer"]]]
    [:tbody
      (for [entry entries]
        [(keyword (str "th" (case (:answer entry)
                              true ".success"
                              false ".danger"
                              "")))
          [:th (:sent entry)]
          [:th (:sender entry)]
          [:th (:recipient entry)]
          [:th (let [trunc-length 80
                     text (:text entry)]
                 (str (subs text 0 trunc-length)
                      (when (> trunc-length (count text))
                        "...")))]
          [:th.spacer]
          [:th (:answered_at entry)]
          [:th (case (:answer entry)
                 true "Yes"
                 false "No"
                 "")]])]]))

;; (def respond
;;   (html [:div.home
;;            "Thank you! Your response has been recorded and you may change it at any time."
;;            [:div.intro "Take a look around our fabulous site and enjoy the handiwork of our talented and devilishly handsome designer. Welcome to Delta Auto, we're not as skeezy as you might imagine! We're way more so!"]
;;            [:div.quote
;;              [:blockquote
;;                [:p "I bought a car from Delta Auto and it broke down seconds after I was off the lot. As god is my witness, I shall have vengence upon this miserable establishment and the wretches who inhabit it."]
;;                [:footer "Steve Jobs, CEO Apple"]]]
;;            [:img {:src "http://designmodo.github.io/Flat-UI/images/icons/svg/retina.svg"}]]))

