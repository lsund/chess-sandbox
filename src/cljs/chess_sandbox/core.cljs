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

(defn select
"Set a square as selected"
  [x y]
  (swap! state/selected assoc :square [x y])
  (if-let [[c t] (@state/position [x y])]
    (swap! state/selected assoc :piece [c t])))

(defn unselect
  "Unselect any square"
  []
  (swap! state/selected assoc :square nil)
  (swap! state/selected assoc :piece nil))

(defn move-selected
  "Move the piece in the state/selected square to the specified square"
  [dst-x dst-y]
  (let [[dst-c dst-t] (@state/position [dst-x dst-y])
        src-c ((@state/selected :piece) 0)]
    (when (not= src-c dst-c)
      (swap! state/move-counter inc)
      (swap! state/position assoc (@state/selected :square) nil)
      (swap! state/position assoc [dst-x dst-y] (@state/selected :piece))
      (swap! 
        state/position-strings 
        conj 
        (util/position-to-string @state/position))
      (logger/move 
        @state/selected 
        {:square [dst-x dst-y] :piece [dst-c dst-t]}))))

(defn selected-square?
  [x y]
  (and (not-empty @state/selected) (= (@state/selected :square) [x y])))

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
  " If we have a square selected, and click the same square,
    we unselect that square else if we have a piece selected, 
    we move it to the new square and unselect. Else we select
    the new square. Then we draw the new board"
  [e]
  (let [[x y] (square e)]
      (cond (selected-square? x y)   (unselect)
            (@state/selected :piece) (do (move-selected x y)
                                        (unselect))
            :else                   (select x y))
    (draw/board)))

(defn handle-button
  []
  (when (> (count @state/position-strings) 1)
    (swap! state/position-strings pop)
    (swap! state/move-counter dec)
    (let [content (dom/getElement "panel")
          laststr (last @state/position-strings)
          lastpos (util/string-to-position laststr)]
      (.removeChild content (.-lastChild content))
      (reset! state/position lastpos))))

(defn init!
  []
  (draw/board)
  (events/listen js/window event-type/CLICK #(handle-click %))
  (events/listen (dom/getElement "mybutton") event-type/CLICK #(handle-button)))

