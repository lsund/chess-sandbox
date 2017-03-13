
(ns ^{:author "lsund"
      :doc    ""}
      chess-sandbox.util
      (:require clojure.set))

(def rep-to-piece 
  {"K" [:white :king]
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

(def piece-to-rep (clojure.set/map-invert rep-to-piece))

(defn string-to-squares
    "Given a string representation of a board, return a sequence of where 
    each element has the format [[x y] s] where [x y] is the coordinates of
    a particular square and s is the string representation of the piece in
    this square"
  [s]
  (map-indexed (fn [i x] [[(int (/ i 8)) (mod i 8)] x]) s))

(defn string-to-position
  "Given a string representation of a board, return a position"
  [s]
  (let [sqrs (string-to-squares s)]
    (apply 
      hash-map 
      (mapcat 
        (fn 
          [[sqr rep]] 
          [sqr (rep-to-piece rep)]) sqrs))))

(defn position-to-string
  "Given a position, return a string representation of a board"
  [pos]
    (apply str (map (fn [[x y]] (piece-to-rep y)) (sort pos))))
