(ns carder-devcards.core
  (:require [devcards.core :as dc :include-macros true]
            [carder.core :as carder]
            [sablono.core :as sab :include-macros true])
  (:require-macros [devcards.core :refer [defcard]]))

(enable-console-print!)

(defn on-click [ratom]
  (swap! ratom update-in [:count] inc))

(defonce first-example-state (atom {:count 55}))

(defcard example-counter
  "# Shared
  This card uses a shared atom"
  (fn [data-atom owner]
    (sab/html [:h3 "Example Counter w/Shared Initial Atom: " (:count @data-atom)]))
  first-example-state
  {:inspect-data true :history true})

(defcard example-incrementer
  (fn [data-atom owner]
    (sab/html [:button {:onClick (fn [] (swap! data-atom update-in [:count] inc)) } "increment"]))
  first-example-state)

(defcard example-decrementer
  (fn [data-atom owner]
    (sab/html [:button {:onClick (fn [] (swap! data-atom update-in [:count] dec)) } "decrement"]))
  first-example-state)

(defn counter4 [ratom {:keys [title button-text]}]
  [:div [:h3 title]
   [:div "Current count: " (@ratom :count)]
   [:div [:button {:on-click #(on-click ratom)}
          button-text]]])

(defn main []
  (dc/start-devcard-ui!))

