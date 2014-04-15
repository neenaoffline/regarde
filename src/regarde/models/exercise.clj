(ns regarde.models.exercise
  (:require [korma.core :as sql]
            [regarde.models.entities :as entities]
            [regarde.models.rating :as rating]
            [clojure.string :as str])
  (:refer-clojure :exclude [find]))

(defn errors [exercise]
  (let [error-conditions [[#(str/blank? (:name %)) "Name cannot be blank"]
                          [#(re-find #"Timothy" (:name %)) "Name cannot contain Timothy"]
                          [#(re-find #"Asif" (:name %)) "Name cannot contain Asif"]]
        errors           (reduce (fn [errors [f msg]]
                                   (if (f exercise)
                                     (cons msg errors)
                                     errors))
                                 []
                                 error-conditions)]
    (if (empty? errors)
      nil
      errors)))

(defn create-exercise [exercise-attrs]
  (if-let [errors (errors exercise-attrs)]
    (assoc exercise-attrs :errors errors)
    (sql/insert entities/exercises (sql/values (select-keys exercise-attrs [:name])))))

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
