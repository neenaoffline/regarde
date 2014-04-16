(ns regarde.controllers.rating-sets
  (:require [regarde.helpers :as helpers]
            [regarde.models.rating :as rating]
            [regarde.models.rating-set :as rating-set]
            [regarde.models.user :as user]
            [regarde.models.exercise :as exercise]
            [regarde.templates :as templates]
            [ring.util.response :as resp]))

(defn create [request]
  (let [current-user (helpers/current-user request)]
    (let [set (rating-set/create (:id current-user) (:exercise-id (:params request))) ]
      (doseq [[user-id rating] (:rating (:params request))]
        (rating/update-or-create set user-id {:rating rating}))))
  (resp/redirect "/"))

(defn update [request]
  (let [current-user (helpers/current-user request)]
    (let [set (rating-set/find (:id current-user) (:exercise-id (:params request))) ]
      (doseq [[user-id rating] (:rating (:params request))]
        (rating/update-or-create set user-id {:rating rating}))))
  (resp/redirect "/"))

(defn new [exercise-id request]
  (let [users (user/all-except (helpers/current-user request))
        exercise (exercise/find exercise-id)]
    (templates/new-ratings exercise users)))

(defn edit [exercise-id request]
  (let [exercise (exercise/find exercise-id)
        users (user/all-except (helpers/current-user request))
        current-user (helpers/current-user request)
        rating-set (rating-set/find (:id current-user) (:id exercise))]
    (templates/edit-ratings exercise rating-set users)))
