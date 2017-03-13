
(ns ^{:author "lsund"
      :doc    "Drawing functions"}
  chess-sandbox.draw
  (:require [chess-sandbox.state :as state]
            [goog.dom :as dom]))

(def dim 480)
(def sqr-dim (/ dim 8))

(def color-light "#c2c2a3")
(def color-dark "#4d4d33")
(def color-selected "#ffffff")

(def canvas (dom/getElement "mycanvas"))
(def ctx (.getContext canvas "2d"))

(defn rect
  "Draws a rectangle given two coordinates, a width and height and a color"
  [x y w h c]
    (set! (.-fillStyle ctx) c)
    (.fillRect ctx x y w h))

(defn image
  "Draws an image given a two coordinates, and two keywords describing the color
  and type of a chess piece"
  [x y c t]
  (let [img (dom/getElement (str c t))
        cx (* x sqr-dim)
        cy (* y sqr-dim)]
    (.drawImage ctx img cx cy)))

(defn board
  "Draws the board of a current state"
  []
  (let [r (range 0 8)]
    (doseq [x r
            y r
            :let [cx (* x sqr-dim) 
                  cy (* y sqr-dim)
                  c (cond
                      (= [x y] (@state/selected :square)) color-selected
                      (and (even? x) (even? y)) color-light
                      (and (even? x) (odd? y))  color-dark
                      (and (odd? x) (even? y))  color-dark
                      (and (odd? x) (odd? y))   color-light)]]
        (rect cx cy sqr-dim sqr-dim c))
    (doseq [[[x y] pce] @state/position]
      (when-let [[c t] pce]
        (image x y c t)))))

