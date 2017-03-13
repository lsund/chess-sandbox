(ns chess-sandbox.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [chess-sandbox.core-test]))

(doo-tests 'chess-sandbox.core-test)

