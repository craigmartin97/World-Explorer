(ns ops-list-size.ops-vs-planner.test-5
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            [planner.planner :refer :all]
            [ops-list-size.ops-vs-planner.operations :refer :all]))

(comment "This test allows all operators to be applied in a fairly large world. This test is only possible with the
          very-large operations lists.

          Due to the number of possibilities in the world state, ops-search has massive problems trying to reach
          the end goal of these tests and causes a Stack Overflow on all operation lists excluding very-short-ops.

          Planner is able to handle the large amount of required operators elegantly as the ':achieves' key allows it
          to not only pick the correct operators, but ignore unnecessary paths. For example, planner will only try to
          pick up one of the 7 holdable items for consecration.

          Similar to test 4, planner is still very quick to recognise if it can't solve the problem whereas ops-search
          will continue to try every possibility until all possible states are found or a Stack Overflow occurs.")

(def test-state-five
  "State containing some rooms, holdable items, wearable items, an altar, demons, and wounded agents."
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
  "Elapsed time: 8819.068033 msecs"
  (time (ops-search test-state-five '((smited nabriales)) very-short-ops-opssearch))
  )

(defn test-five-very-short-planner []
  "Elapsed time: 2.121133333 msecs"
  (time (planner test-state-five '(smited nabriales) very-short-ops-planner))
  )

(defn test-five-short-opssearch []
  "Elapsed time: Stack Overflow"
  (time (ops-search test-state-five '((smited nabriales)) short-ops-opssearch))
  )

(defn test-five-short-planner []
  "Elapsed time:1.909333333  msecs"
  (time (planner test-state-five '(smited nabriales) short-ops-planner))
  )

(defn test-five-medium-opssearch []
  "Elapsed time:Stack Overflow"
  (time (ops-search test-state-five '((smited nabriales)) medium-ops-opssearch))
  )

(defn test-five-medium-planner []
  "Elapsed time: 2.2913 msecs"
  (time (planner test-state-five '(smited nabriales) medium-ops-planner))
  )

(defn test-five-large-opssearch []
  "Elapsed time:Stack Overflow"
  (time (ops-search test-state-five '((smited nabriales)) large-ops-opssearch))
  )

(defn test-five-large-planner []
  "Elapsed time:2.1535  msecs"
  (time (planner test-state-five '(smited nabriales) large-ops-planner))
  )

(defn test-five-very-large-opssearch []
  "Elapsed time:Stack Overflow"
  (time (ops-search test-state-five '((smited nabriales)) very-large-ops-opssearch))
  )

(defn test-five-very-large-planner []
  "Elapsed time: 159.6204333 msecs"
  (time (planner test-state-five '(smited nabriales) very-large-ops-planner))
  )