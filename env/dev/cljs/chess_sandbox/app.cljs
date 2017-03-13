(ns ^:figwheel-no-load chess-sandbox.app
  (:require [chess-sandbox.core :as core]
            [devtools.core :as devtools]
            [figwheel.client :as figwheel :include-macros true]))

(enable-console-print!)

(figwheel/watch-and-reload
  :websocket-url "ws://localhost:3449/figwheel-ws")
  ; :on-jsload core/mount-components)

(devtools/install!)

(core/init!)
