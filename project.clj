(defproject repondezplait "0.1.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 ;; [org.clojure/clojurescript "0.0-2322"]
                 ;; [environ "1.0.0"]
                 [compojure "1.2.1"]
                 [ring/ring-jetty-adapter "1.3.1"]
                 [com.draines/postal "1.11.2"]
                 [com.novemberain/monger "2.0.0"]
                 [hiccup "1.0.5"]
                 ;; [garden "1.2.5"]
                 ;; [xhh/clang "0.1.0-SNAPSHOT"]
                 ]
  :min-lein-version "2.0.0" ; For Heroku.
  :main repondezplait.server
  ;; :plugins [[environ "1.0.0"]
  ;;           ;; [lein-cljsbuild "1.0.3"]
  ;;           [lein-ring "0.8.11"]
  ;;           ]
  ;; :hooks [leiningen.cljsbuild]
  ;; :cljsbuild {:builds [{:source-paths ["src/repondezplait/app"]
  ;;                       :compiler {:output-to "output/app.js"
  ;;                                  :optimizations :whitespace
  ;;                                  :pretty-print true}}]}
  ;; :ring {:handler repondezplait.server/application
  ;;        :port 5000
  ;;        :auto-refresh? true}
  :profiles {:production {:env {:production true}}}


  :env {:asdf 454545}


  )
