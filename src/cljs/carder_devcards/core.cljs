(ns carder-devcards.core
  (:require [devcards.core :as dc :include-macros true]
            [carder.core :as carder]
            [sablono.core :as sab :include-macros true])
  (:require-macros [devcards.core :refer [defcard]]))

(enable-console-print!)
(dc/start-devcard-ui!)

(defcard devcard-intro
   "# Devcards for foo

    <a href=\"https://github.com/bhauman/devcards/blob/master/example_src/devdemos/core.cljs\" target=\"_blank\">Here are some Devcard examples</a>")
