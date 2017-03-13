
(ns ^{:author "lsund"
      :doc    "Structures for holding the current state of the chess game"}
      chess-sandbox.state
      (:require [chess-sandbox.util :as util]))

(def move-counter (atom 0))

;; A portable notation for describing a position:
;; A string of length 64 where each character describes one square starting at
;; square a1,b1,...,h1,a2...h2,...,a8,...,h8. A character in the string can be
;; one of the [k,q,r,b,n,p] for black and [K,Q,R,B,N,P] for white according to
;; convention:
;; k/K = King
;; q/Q = Queen
;; r/R = Rook
;; b/B = Bishop
;; n/N = Knight
;; p/p = Pawn 
;; if the square contains a piece or . if the square contains no piece.
;; Storing positions for an entire game allows for reversing moves. The longest
;; game played lasted about 250 moves (very uncommon) A java string of this form
;; is around 64 bytes meaning the maximum space of storing all position strings
;; would be 250 * 64 =~ 16M.
(def position-string 
  (str  "rp....PRnp....PNbp....PBqp....PQkp....PKbp....PBnp....PNrp....PR" ))

(def position-strings (atom [position-string]))

(def position (atom (util/string-to-position (first @position-strings))))

(def selected (atom {:square nil :piece nil}))

