(ns regarde.models.rating-set
    (:require [regarde.models.exercise :as exercise]
              [korma.core :as sql]
              [regarde.models.entities :as entities]))

(defn find [user-id exercise-id]
  (first (sql/select entities/rating-sets
                     (sql/where {:exercise_id (Integer. exercise-id)
                                 :user_id (Integer. user-id)}))))

(defn create [user-id exercise-id]
  (sql/insert entities/rating-sets (sql/values [{:user_id (Integer.  user-id)
                                    :exercise_id (Integer. exercise-id)}])))

(defn find-or-create [user-id exercise-id]
  (if-let [set (find user-id exercise-id)]
    set
    (create user-id exercise-id)))
