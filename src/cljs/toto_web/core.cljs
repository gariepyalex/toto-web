(ns toto-web.core
    (:require [reagent.core :as reagent]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [goog.events :as events]
              [goog.history.EventType :as EventType]
              [ajax.core :as ajax])
    (:import goog.History))

(enable-console-print!)

;; -------------------------
;; Views

(defn home-page []
  [:div [:h2 "Welcome to toto-web"]
   [:div [:a {:href "#/about"} "go to about page"]]
   [:button {:on-click #(println "this is a nice button buddy")} "hello"]
   [:button {:on-click (fn [] (ajax/GET "/toto"
                                        :handler #'println))}
    "hello ajax"]])

(defn about-page []
  [:div [:h2 "About toto-web"]
   [:div [:a {:href "#/"} "go to the home page"]]])

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

(secretary/defroute "/about" []
  (session/put! :current-page #'about-page))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (hook-browser-navigation!)
  (mount-root))
