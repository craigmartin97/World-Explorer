(ns ops-list-size.ops-vs-planner.test-3
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            [planner.planner :refer :all]
            [ops-list-size.ops-vs-planner.operations :refer :all]))

(def test-state-three
  '#{(agent R)
     (wears R nil)

     (room A)
     (room B)
     (room C)
     (room D)
     (room E)

     (holdable gold-leaf)
     (holdable cake)
     (holdable book)
     (holdable wedding-dress)
     (holdable business-suit)
     (holdable rags)
     (holdable halonic-garb)

     (wearable wedding-dress)
     (wearable business-suit)
     (wearable rags)
     (wearable halonic-garb)

     (holy halonic-garb)

     (in R A)
     (in halonic-garb B)
     (in cake C)
     (in wedding-dress C)
     (in business-suit C)
     (in rags D)
     (in book D)
     (in gold-leaf D)
     })

(defn test-three-very-short-opssearch []
  (time (ops-search test-state-three '((wears R halonic-garb)) very-short-ops-opssearch))
  )

(defn test-three-very-short-planner []
  (time (planner test-state-three '(wears R halonic-garb) very-short-ops-planner))
  )

(defn test-three-short-opssearch []
  (time (ops-search test-state-three '((wears R halonic-garb)) short-ops-opssearch))
  )

(defn test-three-short-planner []
  (time (planner test-state-three '(wears R halonic-garb) short-ops-planner))
  )

(defn test-three-medium-opssearch []
  (time (ops-search test-state-three '((wears R halonic-garb)) medium-ops-opssearch))
  )

(defn test-three-medium-planner []
  (time (planner test-state-three '(wears R halonic-garb) medium-ops-planner))
  )

(defn test-three-large-opssearch []
  (time (ops-search test-state-three '((wears R halonic-garb)) large-ops-opssearch))
  )

(defn test-three-large-planner []
  (time (planner test-state-three '(wears R halonic-garb) large-ops-planner))
  )

(defn test-three-very-large-opssearch []
  (time (ops-search test-state-three '((wears R halonic-garb)) very-large-ops-opssearch))
  )

(defn test-three-very-large-planner []
  (time (planner test-state-three '(wears R halonic-garb) very-large-ops-planner))
  )