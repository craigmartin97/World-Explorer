(ns ops-list-size.ops-vs-planner.test-4
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            [planner.planner :refer :all]
            [ops-list-size.ops-vs-planner.operations :refer :all]))

(def test-state-four
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

     (altar altar)
     (demonic altar)
     (structure altar)

     (in R A)
     (in halonic-garb B)
     (in cake C)
     (in wedding-dress C)
     (in business-suit C)
     (in rags D)
     (in book D)
     (in gold-leaf D)
     (in altar A)
     })

(defn test-four-very-short-opssearch []
  (time (ops-search test-state-four '((gilded altar)) very-short-ops-opssearch))
  )

(defn test-four-very-short-planner []
  (time (planner test-state-four '(gilded altar) very-short-ops-planner))
  )

(defn test-four-short-opssearch []
  (time (ops-search test-state-four '((gilded altar)) short-ops-opssearch))
  )

(defn test-four-short-planner []
  (time (planner test-state-four '(gilded altar) short-ops-planner))
  )

(defn test-four-medium-opssearch []
  (time (ops-search test-state-four '((gilded altar)) medium-ops-opssearch))
  )

(defn test-four-medium-planner []
  (time (planner test-state-four '(gilded altar) medium-ops-planner))
  )

(defn test-four-large-opssearch []
  (time (ops-search test-state-four '((gilded altar)) large-ops-opssearch))
  )

(defn test-four-large-planner []
  (time (planner test-state-four '(gilded altar) large-ops-planner))
  )

(defn test-four-very-large-opssearch []
  (time (ops-search test-state-four '((gilded altar)) very-large-ops-opssearch))
  )

(defn test-four-very-large-planner []
  (time (planner test-state-four '(gilded altar) very-large-ops-planner))
  )