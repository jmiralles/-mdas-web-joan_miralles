(ns pokemon-client.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::pokemon
 (fn [db]
   (:pokemon db)))

(re-frame/reg-sub
 ::pokemon-id
 (fn [db]
   (:pokemon-id db)))


(re-frame/reg-sub
 ::pokemon-id-coll
 (fn [db]
   (:pokemon-id-coll db)))


(re-frame/reg-sub
 ::user-id
 (fn [db]
   (:user-id db)))

(re-frame/reg-sub
 ::failure-http
 (fn [db]
   (:failure-http db)))

