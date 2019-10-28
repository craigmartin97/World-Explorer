(ns ops-list-size.ops-vs-planner.test-5
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            [planner.planner :refer :all]
            [ops-list-size.ops-vs-planner.operations :refer :all]))

(def test-state-five
  '#{(agent R)
     (wears R nil)

     (demon behemoth)
     (demon ahriman)
     (demon nabriales)

     (wounded priest)
     (wounded saint)
     (wounded vicar)

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
     (in behemoth C)
     (in priest C)
     (in ahriman D)
     (in vicar D)
     (in nabriales E)
     (in saint E)
     (in halonic-garb B)
     (in cake C)
     (in wedding-dress C)
     (in business-suit C)
     (in rags D)
     (in book D)
     (in gold-leaf D)
     (in altar A)
     })

(defn test-five-very-short-opssearch []
  (time (ops-search test-state-five '((smited nabriales)) very-short-ops-opssearch))
  )

(defn test-five-very-short-planner []
  (time (planner test-state-five '(smited nabriales) very-short-ops-planner))
  )

(defn test-five-short-opssearch []
  (time (ops-search test-state-five '((smited nabriales)) short-ops-opssearch))
  )

(defn test-five-short-planner []
  (time (planner test-state-five '(smited nabriales) short-ops-planner))
  )

(defn test-five-medium-opssearch []
  (time (ops-search test-state-five '((smited nabriales)) medium-ops-opssearch))
  )

(defn test-five-medium-planner []
  (time (planner test-state-five '(smited nabriales) medium-ops-planner))
  )

(defn test-five-large-opssearch []
  (time (ops-search test-state-five '((smited nabriales)) large-ops-opssearch))
  )

(defn test-five-large-planner []
  (time (planner test-state-five '(smited nabriales) large-ops-planner))
  )

(defn test-five-very-large-opssearch []
  (time (ops-search test-state-five '((smited nabriales)) very-large-ops-opssearch))
  )

(defn test-five-very-large-planner []
  (time (planner test-state-five '(smited nabriales) very-large-ops-planner))
  )