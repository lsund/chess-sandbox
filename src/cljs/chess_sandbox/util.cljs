
(ns ^{:author "lsund"
      :doc    ""}
      chess-sandbox.util
      (:require clojure.set))

(def str-pce {"K" [:white :king]
              "Q" [:white :queen]
              "R" [:white :rook]
              "B" [:white :bishop]
              "N" [:white :knight]
              "P" [:white :pawn]
              "k" [:black :king]
              "q" [:black :queen]
              "r" [:black :rook]
              "b" [:black :bishop]
              "n" [:black :knight]
              "p" [:black :pawn]
              "." nil})

(def pce-str (clojure.set/map-invert str-pce))

(defn string-to-squares
  [s]
  (map-indexed (fn [i x] [[(int (/ i 8)) (mod i 8)] x]) s))

(defn string-to-position
  [s]
  (let [sqrs (string-to-squares s)]
    (apply 
      hash-map 
      (mapcat 
        (fn 
          [[sqr rep]] 
          [sqr (str-pce rep)]) sqrs))))

(defn position-to-string
  [pos]
    (apply str (map (fn [[x y]] (pce-str y)) (sort pos))))
