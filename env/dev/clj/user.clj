(ns user
  (:require [mount.core :as mount]
            [chess-sandbox.figwheel :refer [start-fw stop-fw cljs]]
            chess-sandbox.core))

(defn start []
  (mount/start-without #'chess-sandbox.core/http-server
                       #'chess-sandbox.core/repl-server))

(defn stop []
  (mount/stop-except #'chess-sandbox.core/http-server
                     #'chess-sandbox.core/repl-server))

(defn restart []
  (stop)
  (start))


