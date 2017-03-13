
(ns ^{:author "lsund"
      :doc    "Event handler"}
  chess-sandbox.handler
  (:require [goog.dom :as dom]
            [chess-sandbox.draw :as draw]
            [chess-sandbox.state :as state]
            [chess-sandbox.logger :as logger]
            [chess-sandbox.util :as util]))

(defn square
  "Get the square in form [x y] given a mouse click event"
  [e]
  (let [ol (.-offsetLeft draw/canvas)
        ot (.-offsetTop draw/canvas)
        cx (.-clientX e)
        cy (.-clientY e)
        px-to-sqr (fn [p] (int (Math/floor (/ p draw/sqr-dim))))]
        [(px-to-sqr (- cx ol)) (px-to-sqr (- cy ot))]))

(defn select
"Set a square as selected"
  [x y]
  (swap! state/selected assoc :square [x y])
  (if-let [[c t] (@state/position [x y])]
    (swap! state/selected assoc :piece [c t])))

(defn unselect
  "Unselect all squares"
  []
  (swap! state/selected assoc :square nil)
  (swap! state/selected assoc :piece nil))

(defn move-selected
  "Moves the currently selected piece to the specified coordinates"
  [x y]
  (swap! state/position assoc (@state/selected :square) nil)
  (swap! state/position assoc [x y] (@state/selected :piece)))

(defn update-history
  "Save a snapshot of the current state of the board" 
  []
  (swap! 
    state/position-strings 
    conj 
    (util/position-to-string @state/position)))

(defn move
  "Move the piece in the board"
  [dst-x dst-y]
  (let [[dst-c dst-t] (@state/position [dst-x dst-y])
        src-c ((@state/selected :piece) 0)]
    (when (not= src-c dst-c)
      (swap! state/move-counter inc)
      (move-selected dst-x dst-y)
      (update-history)
      (logger/move 
        @state/selected 
        {:square [dst-x dst-y] :piece [dst-c dst-t]}))))

(defn click
  "If we have a square selected, and click the same square,
    we unselect that square else if we have a piece selected, 
    we move it to the new square and unselect. Else we select
    the new square. Then we draw the new board"
  [e]
  (let [[x y] (square e)
        selected-square? 
        (fn [x y]
          (and 
            (not-empty @state/selected)
            (= (@state/selected :square) [x y])))]
      (cond (selected-square? x y)   (unselect)
            (@state/selected :piece) (do (move x y)
                                        (unselect))
            :else                     (select x y))
  (draw/board)))

(defn button
  "Revert back to the last position of the game"
  []
  (when (> (count @state/position-strings) 1)
    (swap! state/position-strings pop)
    (swap! state/move-counter dec)
    (let [content (dom/getElement "panel")
          laststr (last @state/position-strings)
          lastpos (util/string-to-position laststr)]
      (.removeChild content (.-lastChild content))
      (reset! state/position lastpos))))

