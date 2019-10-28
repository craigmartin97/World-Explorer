(ns ops-list-size.ops-vs-planner.test-2
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            [planner.planner :refer :all]
            [ops-list-size.ops-vs-planner.operations :refer :all]))

(def test-state-two
  '#{(agent R)

     (room A)
     (room B)
     (room C)
     (room D)
     (room E)

     (holdable halonic-garb)

     (in R A)
     (in halonic-garb B)
     })

(defn test-two-very-short-opssearch []
  (time (ops-search test-state-two '((holds R halonic-garb)) very-short-ops-opssearch))
  )

(defn test-two-very-short-planner []
  (time (planner test-state-two '(holds R halonic-garb) very-short-ops-planner))
  )

(defn test-two-short-opssearch []
  (time (ops-search test-state-two '((holds R halonic-garb)) short-ops-opssearch))
  )

(defn test-two-short-planner []
  (time (planner test-state-two '(holds R halonic-garb) short-ops-planner))
  )

(defn test-two-medium-opssearch []
  (time (ops-search test-state-two '((holds R halonic-garb)) medium-ops-opssearch))
  )

(defn test-two-medium-planner []
  (time (planner test-state-two '(holds R halonic-garb) medium-ops-planner))
  )

(defn test-two-large-opssearch []
  (time (ops-search test-state-two '((holds R halonic-garb)) large-ops-opssearch))
  )

(defn test-two-large-planner []
  (time (planner test-state-two '(holds R halonic-garb) large-ops-planner))
  )

(defn test-two-very-large-opssearch []
  (time (ops-search test-state-two '((holds R halonic-garb)) very-large-ops-opssearch))
  )

(defn test-two-very-large-planner []
  (time (planner test-state-two '(holds R halonic-garb) very-large-ops-planner))
  )