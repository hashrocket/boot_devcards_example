(ns carder-devcards.core
  (:require [devcards.core :as dc :include-macros true]
            [carder.core :as carder]
            [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]
            [sablono.core :as sab :include-macros true])
  (:require-macros [devcards.core :refer [defcard]]))

(enable-console-print!)

(defcard trying-om
  (carder/dashboard-item {:type :dashboard/graphic :title "Wut." :image "image.png"}))

(defn main []
  (dc/start-devcard-ui!))
