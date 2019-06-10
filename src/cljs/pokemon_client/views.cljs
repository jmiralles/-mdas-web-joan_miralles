(ns pokemon-client.views
  (:require
   [re-frame.core :as re-frame]
   [pokemon-client.subs :as subs]
   ))


(defn get-pokemon-button []
  (let [value @(re-frame/subscribe [::subs/pokemon-id])]
  [:button
   {:on-click (fn [e]
                (.preventDefault e)
                (re-frame/dispatch [:get-pokemon value]))}
   "Get Pokemon Details"]))

(defn get-pokemon-types-button []
  (let [value @(re-frame/subscribe [::subs/pokemon-id])]
    [:button
     {:on-click (fn [e]
                  (.preventDefault e)
                  (re-frame/dispatch [:get-pokemon-types value]))}
     "Get pokemon types"]))

(defn add-pokemon-to-fav []
  (let [pokemon-id-coll @(re-frame/subscribe [::subs/pokemon-id-coll])
        user-id @(re-frame/subscribe [::subs/user-id])]
    [:button
     {:on-click (fn [e]
                  (.preventDefault e)
                  (re-frame/dispatch [:add-pokemon-to-favs pokemon-id-coll user-id]))}
     "Add pokemon to fav"]))

(defn pokemon-id-input []
  [:input {:type "text"
           :value @(re-frame/subscribe [::subs/pokemon-id])
           :on-change #(re-frame/dispatch [:pokemon-id-change (-> % .-target .-value)])}])

(defn pokemon-id-coll-input []
  [:input {:type "text"
           :value @(re-frame/subscribe [::subs/pokemon-id-coll])
           :on-change #(re-frame/dispatch [:pokemon-id-coll-change (-> % .-target .-value)])}])

;;pokemon add to fav
(defn  user-id-input []
  [:input {:type "text"
           :value @(re-frame/subscribe [::subs/user-id])
           :on-change #(re-frame/dispatch [:user-id-change (-> % .-target .-value)])}])

;; pokemon get details view
(defn get-pokemon-details[]
  [:div
   [get-pokemon-button]
   ])

(defn get-pokemon-types []
  [:div
[get-pokemon-types-button]])

(defn pokemon-form []
  [:div 
   [:label "Pokemon Id"]
   [pokemon-id-input]
   [get-pokemon-details]
   [get-pokemon-types]]
  )

(defn pokemon-fav-form []
  [:div 
   [:label "Pokemons Ids"]
   [pokemon-id-coll-input]
   [:label "User Id"]
   [user-id-input]
   [add-pokemon-to-fav]])

;; pokemon view
(defn pokemon-details-view [pokemon]
  (let [name (:name pokemon)
           id (:id pokemon)
           types (:pokemonTypes pokemon)
           num-of-favs (:numberOfTimesMarkedAsFavourite pokemon)]
    [:div
     [:h2 "Pokemon Details"]
     [:p "Name: " name]
     [:p "Id: " id]
     [:p "Favs: " num-of-favs]
     [:p "Types"]
     [:ul 
      (for [t types]
        [:li t])]]))

(defn pokemon-types-view [pokemon]
  (let [types (:pokemonTypes pokemon)]
    [:div
     [:h2 "Pokemon Types"]
     [:p "Types"]
     [:ul
      (for [t types]
        [:li t])]]))


(defn pokemon-view [pokemon]
  (if-let [name (:name pokemon)]
    [pokemon-details-view pokemon]
    [pokemon-types-view pokemon]))

(defn show-error [error]
  (let [status (:debug-message error)]
    [:p status]
    ))

(defn main-panel []
  (let [pokemon (re-frame/subscribe [::subs/pokemon])
        error-notify (re-frame/subscribe [::subs/failure-http])]
    [:div
     [:h1 "Pokemon Client"]
     [:h2 "GET Pokemon info"]
     [pokemon-form]
     [:h2 "Add Pokemons to fav"]
     [pokemon-fav-form]
     [pokemon-view @pokemon]
     [show-error @error-notify]]))
