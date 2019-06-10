(ns pokemon-client.events
  (:require
   [re-frame.core :as re-frame]
   [pokemon-client.db :as db]
   [ajax.core :as ajax]
   [clojure.string :as str]
   ))

(def api-url "http://localhost:8080")

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
 :pokemon-id-change           
 (fn [db [_ new-id-value]]
   (assoc db :pokemon-id new-id-value))) 

(re-frame/reg-event-db
 :pokemon-id-coll-change
 (fn [db [_ new-ids-value]]
   (assoc db :pokemon-id-coll new-ids-value)))

(re-frame/reg-event-db
 :user-id-change
 (fn [db [_ new-id-value]]
   (assoc db :user-id new-id-value)))


(re-frame/reg-event-fx 
 :get-pokemon           
 (fn [cofx [_ pokemon-id]] 
   {:http-xhrio {:uri (str api-url "/pokemon-details?id=" pokemon-id)
                 :method :get
                 :timeout 10000
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [::pokemon-view-updated]
                 :on-failure [::failure-http-result]}}))

(re-frame/reg-event-fx
 :get-pokemon-types
 (fn [cofx [_ pokemon-id]]
   {:http-xhrio {:uri (str api-url "/pokemon-type?id=" pokemon-id)
                 :method :get
                 :timeout 10000
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [::pokemon-view-updated-types]
                 :on-failure [::failure-http-result]}}))

(re-frame/reg-event-fx
 :add-pokemon-to-favs
 (fn [cofx [_ pokemon-ids user-id]]
   {:http-xhrio {:uri (str api-url "/add-favourite-pokemon?userId=" user-id "&pokemonIds=" pokemon-ids)
                 :method :post
                 :format          (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :timeout 10000
                 :on-success [::pull-pokemon (first (str/split pokemon-ids #","))]
                 :on-failure [::pull-pokemon (first (str/split pokemon-ids #","))]}})) 
;; falla con un 200... debe ser por la api que no devuelve una response :(


(re-frame/reg-event-db
 ::pokemon-view-updated-types
 (fn [db [_ result]]
   (assoc-in (apply dissoc db [:pokemon :name :failure-http]) [:pokemon :pokemonTypes] result)))

(re-frame/reg-event-db
 ::pokemon-view-updated
 (fn [db [_ result]]
   (assoc (apply dissoc db [:failure-http]) :pokemon result)))

(re-frame/reg-event-db
 ::failure-http-result
 (fn [db [_ result]]
    ;; result is a map containing details of the failure
   (assoc db :failure-http result)))


(re-frame/reg-event-fx              
  ::pull-pokemon
  (fn [cofx [_ id]]
    (js/console.log "message" id)       
    {:dispatch [:get-pokemon id]})) 