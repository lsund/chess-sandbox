(ns chess-sandbox.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [chess-sandbox.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[chess-sandbox started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[chess-sandbox has shut down successfully]=-"))
   :middleware wrap-dev})
