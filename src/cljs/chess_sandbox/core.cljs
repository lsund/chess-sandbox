(ns ^{:author "lsund"
      :doc    "Front entry point"}
  chess-sandbox.core
  (:require [goog.dom :as dom]
            [goog.events :as events]
            [goog.events.EventType :as event-type]
            [chess-sandbox.draw :as draw]
            [chess-sandbox.handler :as handler]))

(enable-console-print!)

(defn init!
  "Initialize the canvas drawing and listen for events"
  []
  (draw/board)
  (events/listen js/window event-type/CLICK #(handler/click %))
  (events/listen 
    (dom/getElement "mybutton")
    event-type/CLICK
    #(handler/button)))

