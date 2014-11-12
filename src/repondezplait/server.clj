(ns repondezplait.server
  (:require [environ.core :refer [env]]
            [clojure.string :refer [join trim split-lines]]
            [compojure.core :refer [defroutes GET POST]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.adapter.jetty :as ring]
            [postal.core :refer [send-message]]
            [monger.core :as mg]
            [monger.collection :as mc]
            ;; [monger.result :refer [ok?]]
            ;; [repondezplait.server.views :as views]
            ;; [repondezplait.server.style :refer [stylesheet]]
   )
  (:import [java.util Properties Date]
           [java.io InputStream ByteArrayInputStream]
           [javax.mail Session]
           [javax.mail.internet MimeMessage]
           [org.bson.types ObjectId])
  (:gen-class))


(let [{:keys [db]} (mg/connect-via-uri (System/genenv "MONGOLAB_URI"))]

  (defroutes routes
    ;; (GET "/style.css" [] {:headers {"Content-Type" "text/css"} :body stylesheet})

    (GET "/respond" [] "Thank you! Your response has been recorded, and you may change it at any time. You have pleased Nora, high archon of technical recruiting; prepare to recieve her boon.")

    (let [template (slurp "resources/email.html")]
      (POST "/incoming" request
            (let [message (let [content (get-in request [:params :message])
                                session (Session/getDefaultInstance (Properties.))
                                stream (ByteArrayInputStream. (.getBytes content))]
                            (MimeMessage. session stream))
                  sender (first (.getFrom message)) ; Apparently it's an array of multiple froms.
                  raw-body-lines (split-lines (.. message getContent (getBodyPart 0) getContent))]
                  recipient (first raw-body-lines)
                  new-body (fn [sep] (->> (next raw-body-lines) (join sep) (trim)))
                  document (mc/insert db "emails" {:_id (ObjectId.) :sent (Date.) :sender sender :recipient recipient}))
              (let [{:keys [error]} (send-message {:host "smtp.gmail.com"
                                                   :user "repondezplait"
                                                   :pass "repondezplait111"
                                                   :ssl true}
                                                  {:from sender ; It doesn't matter what this is set to, Gmail will override it.
                                                   :reply-to sender
                                                   :to recipient
                                                   ;; :to (.getRecipients message javax.mail.Message$RecipientType/TO)
                                                   :subject (.getSubject message)
                                                   :body [:alternative
                                                          {:type "text/plain; charset=utf-8"
                                                           :content (new-body "\n")}
                                                          {:type "text/html; charset=utf-8"
                                                           :content (let [base-url (str "http://repondezplait.herokuapp.com/respond?id=" (:_id document) "&answer=")]
                                                                      (str (-> template
                                                                               (replace "{{body}}" (new-body "<br />"))
                                                                               (replace "{{yes-url}}" (str base-url "yes"))
                                                                               (replace "{{no-url}}" (str base-url "no")))
                                                                           "<br /><br /><div>hi!</div>"))}]})]
                (when (not= :SUCCESS error))
                  (throw (Exception. error)))
            {:status 200 :headers {"Content-Type" "text/plain"}})) ; Tell postal everything worked.

    ;; (route/resources "/")
    ;; (GET "*" [] views/index)
    (route/not-found "Not Found!")))

(def application (handler/site routes)) ; Standalone def for lein-ring.

(defn start []
  (ring/run-jetty application {:port (Integer. (env :port 5000))
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
