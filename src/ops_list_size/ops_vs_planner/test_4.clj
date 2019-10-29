(ns ops-list-size.ops-vs-planner.test-4
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            [planner.planner :refer :all]
            [ops-list-size.ops-vs-planner.operations :refer :all]))

(comment "This test introduces an altar to the scenario which allowed for even more operators to be applied.
          Only the large and very-large operation lists can solve this problem.

          It is interesting to note that the planner with an operations list that can't solve the problem immediately
          recognises this fact and takes almost no time to return nil. However, ops search will just keep trying until all
          possible states have been reached. This results in ops-search causing a Stack Overflow exception in the short
          and medium operations lists.

          Similar conclusions to previous tests are consistent in this test where we can see ops-search being heavily
          affected by additional operators whilst planner has very similar performance.")

(def test-state-four
  "State containing some rooms, holdable items, wearable items, and an altar."
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
  "Elapsed time: 6177.3435 msecs"
  (time (ops-search test-state-four '((gilded altar)) very-short-ops-opssearch))
  )

(defn test-four-very-short-planner []
  "Elapsed time: 2.088733333 msecs"
  (time (planner test-state-four '(gilded altar) very-short-ops-planner))
  )

(defn test-four-short-opssearch []
  "Elapsed time: Stack Overflow"
  (time (ops-search test-state-four '((gilded altar)) short-ops-opssearch))
  )

(defn test-four-short-planner []
  "Elapsed time: 2.037066667 msecs"
  (time (planner test-state-four '(gilded altar) short-ops-planner))
  )

(defn test-four-medium-opssearch []
  "Elapsed time: Stack Overflow"
  (time (ops-search test-state-four '((gilded altar)) medium-ops-opssearch))
  )

(defn test-four-medium-planner []
  "Elapsed time: 2.134933333 msecs"
  (time (planner test-state-four '(gilded altar) medium-ops-planner))
  )

(defn test-four-large-opssearch []
  "Elapsed time:61643.7911  msecs"
  (time (ops-search test-state-four '((gilded altar)) large-ops-opssearch))
  )

(defn test-four-large-planner []
  "Elapsed time: 48.73369967 msecs"
  (time (planner test-state-four '(gilded altar) large-ops-planner))
  )

(defn test-four-very-large-opssearch []
  "Elapsed time: 94397.26463 msecs"
  (time (ops-search test-state-four '((gilded altar)) very-large-ops-opssearch))
  )

(defn test-four-very-large-planner []
  "Elapsed time:45.0606  msecs"
  (time (planner test-state-four '(gilded altar) very-large-ops-planner))
  )