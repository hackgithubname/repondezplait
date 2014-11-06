(ns repondezplait.server
  (:require ;; [environ.core :refer [env]]
            [compojure.core :refer [defroutes GET]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.adapter.jetty :as ring]
            ;; [repondezplait.server.views :as views]
            ;; [repondezplait.server.style :refer [stylesheet]]
   )
  (:gen-class))

(defroutes routes
  ;; (GET "/style.css" [] {:headers {"Content-Type" "text/css"} :body stylesheet})

  ;; (GET "/pages/home" [] views/home)

  (POST "/incoming" request
        (println request)
        (str "")) ; Empty but successful response.

  (route/resources "/")
  (GET "*" [] views/index)
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
