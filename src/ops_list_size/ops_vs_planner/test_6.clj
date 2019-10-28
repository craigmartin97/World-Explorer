(ns ops-list-size.ops-vs-planner.test-6
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            [planner.planner :refer :all]
            [ops-list-size.ops-vs-planner.operations :refer :all]))

(def test-state-six
  '#{(agent R)
     (wears R nil)

     (wounded priest)

     (room A)
     (room B)
     (room C)

     (holdable gold-leaf)
     (holdable halonic-garb)

     (wearable halonic-garb)

     (holy halonic-garb)

     (altar altar)
     (demonic altar)
     (structure altar)

     (in R A)
     (in priest C)
     (in halonic-garb B)
     (in gold-leaf B)
     (in altar A)
     })

(defn test-six-very-short-opssearch []
  (time (ops-search test-state-six '((agent priest)) very-short-ops-opssearch))
  )

(defn test-six-very-short-planner []
  (time (planner test-state-six '(agent priest) very-short-ops-planner))
  )

(defn test-six-short-opssearch []
  (time (ops-search test-state-six '((agent priest)) short-ops-opssearch))
  )

(defn test-six-short-planner []
  (time (planner test-state-six '(agent priest) short-ops-planner))
  )

(defn test-six-medium-opssearch []
  (time (ops-search test-state-six '((agent priest)) medium-ops-opssearch))
  )

(defn test-six-medium-planner []
  (time (planner test-state-six '(agent priest) medium-ops-planner))
  )

(defn test-six-large-opssearch []
  (time (ops-search test-state-six '((agent priest)) large-ops-opssearch))
  )

(defn test-six-large-planner []
  (time (planner test-state-six '(agent priest) large-ops-planner))
  )

(defn test-six-very-large-opssearch []
  (time (ops-search test-state-six '((agent priest)) very-large-ops-opssearch))
  )

(defn test-six-very-large-planner []
  (time (planner test-state-six '(agent priest) very-large-ops-planner))
  )