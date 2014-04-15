(ns regarde.models.exercise
  (:require [korma.core :as sql]
            [regarde.models.entities :as entities]
            [regarde.models.rating :as rating]
            [clojure.string :as str])
  (:refer-clojure :exclude [find]))

(defn valid? [exercise]
  (not (str/blank? (:name exercise))))

(defn create-exercise [exercise-attrs]
  (if (valid? exercise-attrs)
    (sql/insert entities/exercises (sql/values (select-keys exercise-attrs [:name])))
    false))

(defn all []
  (sql/select entities/exercises))

(defn users-done [exercise]
  (rating/users-done exercise))

(defn users-not-done [exercise]
  (rating/users-not-done exercise))

(defn complete? [users-completed users]
  (let [[all-users completed-users]
        [(into [] (map #(:id %) users))
         (into [] (map #(:id %) users-completed))]]
    (== 0 (compare all-users completed-users))))

(defn find [exercise-id]
  (first (sql/select entities/exercises
                     (sql/where {:id (Integer. exercise-id)}))))
