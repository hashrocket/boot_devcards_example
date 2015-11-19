(ns carder.core
  (:require [goog.dom :as gdom]
            [cognitect.transit :as t]
            [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]
            [devcards.core :as dc :refer-macros [defcard deftest]]))

(enable-console-print!)

(def init-data
  {:dashboard/items
   [{:id 0 :type :dashboard/post
     :author "Laura Smith"
     :title "A Post!"
     :content "Lorem ipsum dolor sit amet, quem atomorum te quo"}
    {:id 1 :type :dashboard/photo
     :title "A Photo!"
     :image "photo.jpg"
     :caption "Lorem ipsum"}
    {:id 2 :type :dashboard/post
     :author "Jim Jacobs"
     :title "Another Post!"
     :content "Lorem ipsum dolor sit amet, quem atomorum te quo"}
    {:id 3 :type :dashboard/graphic
     :title "Charts and Stufff!"
     :image "chart.jpg"}
    {:id 4 :type :dashboard/post
     :author "May Fields"
     :title "Yet Another Post!"
     :content "Lorem ipsum dolor sit amet, quem atomorum te quo"}]})

(defui Post
  static om/IQuery
  (query [this]
    [:id :type :title :author :content])
  Object
  (render [this]
    (dom/div nil
      (dom/h2 nil (-> this om/props :title))
      (dom/p nil  (-> this om/props :content)))))

(def post (om/factory Post))

(defui Photo
  static om/IQuery
  (query [this]
    [:id :type :title :image :caption])
  Object
  (render [this]
    (dom/p nil
      (-> this om/props :title)
      (dom/img {:src (-> this om/props :image)}))))

(def photo (om/factory Photo))

(defui Graphic
  static om/IQuery
  (query [this]
    [:id :type :image])
  Object
  (render [this]
    (dom/p nil
      (-> this om/props :title)
      (dom/img {:src (-> this om/props :image)}))))

(def graphic (om/factory Graphic))

(defui DashboardItem
  static om/Ident
  (ident [this {:keys [id type]}]
    [type id])
  static om/IQuery
  (query [this]
    (zipmap
      [:dashboard/post :dashboard/photo :dashboard/graphic]
      (map #(conj % :favorites)
        [(om/get-query Post)
         (om/get-query Photo)
         (om/get-query Graphic)])))
  Object
  (render [this]
    (let [props (om/props this)]
      (dom/li nil
        (case (:type props)
          :dashboard/graphic (graphic props)
          :dashboard/photo (photo props)
          :dashboard/post (post props))))))

(def dashboard-item (om/factory DashboardItem))

(defui Dashboard
  static om/IQuery
  (query [this]
    [{:dashboard/items (om/get-query DashboardItem)}])
  Object
  (render [this]
    (apply dom/ul nil
      (map dashboard-item (:dashboard/items (om/props this))))))

(defmulti read om/dispatch)

(defmethod read :dashboard/items
  [{:keys [state ast]} k _]
  (let [st @state]
    {:value   (into [] (map #(get-in st %)) (get st k))
     :dynamic (update-in ast [:query]
                #(->> (for [[k _] %]
                        [k [:favorites]])
                  (into {})))
     :static  (update-in ast [:query]
                #(->> (for [[k v] %]
                        [k (into [] (remove #{:favorites}) v)])
                  (into {})))}))

(def w (t/writer :json))
(def app-state (atom (om/tree->db Dashboard init-data true)))

(defn ^:export main []
  (if-let [node (gdom/getElement "app")]
    (let [reconciler (om/reconciler
                       {:state app-state
                        :parser (om/parser {:read read})})]
      (om/add-root! reconciler Dashboard node))))
