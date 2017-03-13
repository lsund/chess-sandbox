; (ns chess-sandbox.core
;   (:require [dommy.core :refer-macros [sel sel1]] 
;             [goog.dom :as dom]
;             [goog.events :as events]
;             [goog.events.EventType :as event-type]))

(ns ^{:author "lsund"
      :doc    "The core functions for the chess frontend."}
  chess-sandbox.core
  (:require [goog.dom :as dom]
            [goog.events :as events]
            [goog.events.EventType :as event-type]
            [dommy.core :as dommy]
            [chess-sandbox.state :as state]
            [chess-sandbox.util :as util]
            [chess-sandbox.draw :as draw]
            [chess-sandbox.logger :as logger]))

(enable-console-print!)

; (defn mount-components
;   []
;   (let [content (dom/getElement "panel")]
;     (.appendChild 
;       content 
;       (js/document.createTextNode "Welcome to chess-sandbox"))))


(defn init!
  []
  nil)

(defn focus
  "Set a square as state/focused"
  [x y]
  (swap! state/focused assoc :square [x y])
  (if-let [[c t] (@state/position [x y])]
    (swap! state/focused assoc :piece [c t])))

(defn unfocus
  "Unfocus any square"
  []
  (swap! state/focused assoc :square nil)
  (swap! state/focused assoc :piece nil))

(defn move-focused
  "Move the piece in the state/focused square to the specified square"
  [dst-x dst-y]
  (let [[dst-c dst-t] (@state/position [dst-x dst-y])
        src-c ((@state/focused :piece) 0)]
    (when (not= src-c dst-c)
      (swap! state/counter inc)
      (swap! state/position assoc (@state/focused :square) nil)
      (swap! state/position assoc [dst-x dst-y] (@state/focused :piece))
      (swap! 
        state/position-strings 
        conj 
        (util/position-to-string @state/position))
      (logger/move 
        @state/focused 
        {:square [dst-x dst-y] :piece [dst-c dst-t]}))))

(defn focused-square?
  [x y]
  (and (not-empty @state/focused) (= (@state/focused :square) [x y])))

(defn square
  "Get the square in form [x y] given a mouse click event"
  [e]
  (let [ol (.-offsetLeft draw/canvas)
        ot (.-offsetTop draw/canvas)
        cx (.-clientX e)
        cy (.-clientY e)
        px-to-sqr (fn [p] (int (Math/floor (/ p draw/sqr-dim))))]
        [(px-to-sqr (- cx ol)) (px-to-sqr (- cy ot))]))

(defn handle-click
  " If we have a square focused, and click the same square,
    we unfocus that square else if we have a piece focused, 
    we move it to the new square and unfocus. Else we focus
    the new square. Then we draw the new board"
  [e]
  (let [[x y] (square e)]
      (cond (focused-square? x y)   (unfocus)
            (@state/focused :piece) (do (move-focused x y)
                                        (unfocus))
            :else                   (focus x y))
    (draw/board)))

(defn handle-button
  []
  (when (> (count @state/position-strings) 1)
    (swap! state/position-strings pop)
    (let [laststr (last @state/position-strings)
          lastpos (util/string-to-position laststr)]
      (reset! state/position lastpos))))

(events/listen js/window event-type/CLICK #(handle-click %))
(events/listen (dom/getElement ":mybutton") event-type/CLICK #(handle-button))

(draw/board)

