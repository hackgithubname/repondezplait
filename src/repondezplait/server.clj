(ns repondezplait.server
  (:require [environ.core :refer [env]]
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

  (POST "/incoming" request
        (let [content (get-in request [:params :message])
              session (Session/getDefaultInstance (Properties.))
              stream (ByteArrayInputStream. (.getBytes content))
              message (MimeMessage. session stream)
              ]
          ;; (send-message)
          (println (.getRecipients message javax.mail.Message$RecipientType/TO))
          (doseq [from (.getFrom message)]
            (println (.getType from)))
          (doseq [from (.getFrom message)]
            (println (.toString from)))
          (println (.getSubject message))
          (println (.getContent message))
          (println (.toString (.getContent message)))
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
