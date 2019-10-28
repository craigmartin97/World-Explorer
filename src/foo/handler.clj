(ns foo.handler
  (:gen-class) ;; <--- you need this too!
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.adapter.jetty :refer :all] ;; <-- need a server!
            [tuple-size.one :refer :all]
            [tuple-size.eleven :refer :all]
            ))

;; Compojure routes
(defroutes app-routes
           (GET "/" ... ))

;; Ring handler
(def app
  (handler/site app-routes))

;; Pass the handler to Jetty on port 8080
(defn -main []

  (test-one-move-to-f)
  ; (test-eleven-move-to-f)
  )