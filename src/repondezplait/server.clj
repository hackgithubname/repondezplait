(ns repondezplait.server
  (:require ; [environ.core :refer [env]]
            [clojure.string :refer [join trim split-lines replace]]
            [compojure.core :refer [defroutes GET POST]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.adapter.jetty :as ring]
            [postal.core :refer [send-message]]
            [monger.core :as mg]
            [monger.collection :as mc]
            [monger.result :refer [ok?]]
            [repondezplait.views :as views]
            [repondezplait.style :refer [stylesheet]]
   )
  (:import [java.util Properties Date]
           [java.io InputStream ByteArrayInputStream]
           [javax.mail Session]
           [javax.mail.internet MimeMessage]
           [org.bson.types ObjectId])
  (:gen-class))


(let [{:keys [db]} (mg/connect-via-uri "mongodb://heroku_app31322565:uaado7e4a37vp8gflum8m4gu9p@ds051990.mongolab.com:51990/heroku_app31322565"
                                       ;; (System/getenv "MONGOLAB_URI")
                                       )]
  (defroutes routes
    (let [template (slurp "resources/email.html")]
      (POST "/incoming" request
            (let [message (let [content (get-in request [:params :message])
                                session (Session/getDefaultInstance (Properties.))
                                stream (ByteArrayInputStream. (.getBytes content))]
                            (MimeMessage. session stream))
                  sender (str (first (.getFrom message))) ; Apparently it's an array of multiple froms.
                  subject (str (.getSubject message))
                  raw-body-lines (split-lines (.. message getContent (getBodyPart 0) getContent))
                  recipient (-> (first raw-body-lines) (trim))
                  new-body #(->> (next raw-body-lines) (join %) (trim))
                  new-body-text (new-body "\n")
                  oid (ObjectId.)
                  new-message {:from sender :to recipient :subject subject
                               ;; :reply-to sender
                               :body [:alternative
                                      {:type "text/plain; charset=utf-8" :content new-body-text}
                                      {:type "text/html; charset=utf-8"
                                       :content (let [url-base (str "http://repondezplait.herokuapp.com/respond/" oid "/")]
                                                  (-> template (replace "{{body}}" (new-body "<br>"))
                                                               (replace "{{yes-url}}" (str url-base "yes"))
                                                               (replace "{{no-url}}" (str url-base "no"))))}]}]
              (let [{:keys [error]} (send-message {:host "smtp.sendgrid.net" :user "app31322565@heroku.com" :pass "s2scv5j7"}
                                                  new-message)]
                (when (not= :SUCCESS error)
                  (throw (Exception. error))))
              (assert (ok? (mc/insert db "emails" {:_id oid :sent (Date.) :sender sender :recipient recipient :subject subject :text new-body-text}))))
            {:status 200 :headers {"Content-Type" "text/plain"}})) ; Tell mail2webhook everything worked, otherwise it'll bounce the original email.

    (GET "/respond/:id/:answer" [id answer]
         (mc/update-by-id db "emails" (ObjectId. id) {:$set {:answered_at (Date.)
                                                             :answer (case answer "yes" true "no" false)}})
         views/respond)

    (GET "/responses" [] (views/responses (sort-by :sent #(compare %2 %1) (mc/find-maps db "emails"))))

    (GET "/style.css" [] {:headers {"Content-Type" "text/css"} :body stylesheet})
    ;; (route/resources "/")
    ;; (GET "*" [] views/index)
    (route/not-found "Not Found!")))

(def application (handler/site routes)) ; Standalone def for lein-ring.

(defn start []
  (ring/run-jetty application {:port (Integer. (or (System/getenv "PORT") 5000))
                               :join? false}))






;; (println "33")
;; ;; (println env)
;; (println (env :production))
;; (println (env :penis))
;; (println (env :asdfasdfdsfs))
;; (println "asdfaf")


(defn -main [& args]
  (start))



;; (if (not (System/getenv "PRODUCTION"))
;;   (start))
