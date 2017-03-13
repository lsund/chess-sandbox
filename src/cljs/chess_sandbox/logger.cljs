
(ns ^{:author "lsund"
      :doc    "Logger utilities"}
  chess-sandbox.logger
  (:require [goog.dom :as dom]
            [chess-sandbox.state :as state]
            [chess-sandbox.util :as util]))

(defn move-rep
  "Takes a move from a source square to a destination square and generates a
  algebraic chess notaition for that move"
  [src dst]
  (let [cord-to-file-rank (fn [x y] [((vec "abcdefgh") x) (- 8 y)])
        [[src-x src-y] [src-c src-t]] (vals src)
        [[dst-x dst-y] [dst-c dst-t]] (vals dst)
        [dst-f dst-r] (cord-to-file-rank dst-x dst-y)
        [src-f src-r] (cord-to-file-rank src-x src-y)
        capt (if dst-t "x" nil)]
    (if (= src-t :pawn)
      (if capt (str src-f capt dst-f dst-r) (str dst-f dst-r))
      (str (util/pce-str [src-c src-t]) capt dst-f dst-r))))

(defn move
  [src dst]
  "Logs the string representation of a move to the screen"
  (let [content (dom/getElement "panel")]
    (.appendChild
      content
      (js/document.createTextNode
        (str @state/counter "." (move-rep src dst) " ")))))

