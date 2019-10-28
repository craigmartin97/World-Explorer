(ns ops-list-size.ops-vs-planner.test-1
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            [planner.planner :refer :all]
            [ops-list-size.ops-vs-planner.operations :refer :all]))

(def test-state-one
  '#{(agent R)

     (room A)
     (room B)

     (in R A)
     })

(defn test-one-very-short-opssearch []
  (time (ops-search test-state-one '((in R B)) very-short-ops-opssearch))
  )

(defn test-one-very-short-planner []
  (time (planner test-state-one '(in R B) very-short-ops-planner))
  )

(defn test-one-short-opssearch []
  (time (ops-search test-state-one '((in R B)) short-ops-opssearch))
  )

(defn test-one-short-planner []
  (time (planner test-state-one '(in R B) short-ops-planner))
  )

(defn test-one-medium-opssearch []
  (time (ops-search test-state-one '((in R B)) medium-ops-opssearch))
  )

(defn test-one-medium-planner []
  (time (planner test-state-one '(in R B) medium-ops-planner))
  )

(defn test-one-large-opssearch []
  (time (ops-search test-state-one '((in R B)) large-ops-opssearch))
  )

(defn test-one-large-planner []
  (time (planner test-state-one '(in R B) large-ops-planner))
  )

(defn test-one-very-large-opssearch []
  (time (ops-search test-state-one '((in R B)) very-large-ops-opssearch))
  )

(defn test-one-very-large-planner []
  (time (planner test-state-one '(in R B) very-large-ops-planner))
  )