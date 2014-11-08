(ns repondezplait.server
  (:require [clojure.string :refer [join trim split-lines]]
            [environ.core :refer [env]]
            [compojure.core :refer [defroutes GET POST]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.adapter.jetty :as ring]
            [postal.core :refer [send-message]]
            ;; [repondezplait.server.views :as views]
            ;; [repondezplait.server.style :refer [stylesheet]]
   )
  (:import [java.util Properties]
           [java.io InputStream ByteArrayInputStream]
           [javax.mail Session]
           [javax.mail.internet MimeMessage])
  (:gen-class))

(defroutes routes
  ;; (GET "/style.css" [] {:headers {"Content-Type" "text/css"} :body stylesheet})

  ;; (GET "/pages/home" [] views/home)

  (GET "/thanks" [] "Thank you! Your response has been recorded. You have pleased Nora, high archon of technical recruiting; prepare to recieve her boon.")

  (POST "/incoming" request
        (let [content (get-in request [:params :message])
              session (Session/getDefaultInstance (Properties.))
              stream (ByteArrayInputStream. (.getBytes content))
              message (MimeMessage. session stream)
              from (first (.getFrom message)) ; Array of multiple froms apparently returned.
              raw-body-lines (split-lines (.. message getContent (getBodyPart 0) getContent))
              to (first raw-body-lines)
              html (str (->> (next raw-body-lines) (join "\n") (trim))
                        "<br /><br /><div>hi!</div>")]
          (send-message {:host "smtp.gmail.com"
                         :user "repondezplait"
                         :pass "repondezplait111"
                         :ssl true}
                        {:from from ; Maybe someday we'll be able to set this and gmail won't override it.
                         :Reply-To (str from) ; Headers not built in to postal must be explicitly converted to strings.
                         :to to
                         ;; :to (.getRecipients message javax.mail.Message$RecipientType/TO)
                         :subject (.getSubject message)
                         :body html
                         :Content-Type "text/html; charset=UTF-8"})
          {:status 200 :headers {"Content-Type" "text/plain"}}))

  (route/resources "/")
  ;; (GET "*" [] views/index)
  (route/not-found "Not Found!"))

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
