(ns chess-sandbox.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[chess-sandbox started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[chess-sandbox has shut down successfully]=-"))
   :middleware identity})
