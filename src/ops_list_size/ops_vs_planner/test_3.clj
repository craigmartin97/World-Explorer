(ns ops-list-size.ops-vs-planner.test-3
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            [planner.planner :refer :all]
            [ops-list-size.ops-vs-planner.operations :refer :all]))

(comment "The test scenario now offers more items it pick up and items that can be worn.
          This test starts to let more operators in the operator list be applicable which has a heavy effect on ops-search.
          The very-short-ops lists are not able to solve this problem.

          Planner is still seemingly unaffected as no clear time increase is seen as more operators are added.
          This is likely because the planner will only have to look at the ':achieves' key to deem an operator necessary.")

(def test-state-three
  "State containing some rooms, holdable items, and clothes."
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
  "Elapsed time: 5718.136033 msecs"
  (time (ops-search test-state-three '((wears R halonic-garb)) very-short-ops-opssearch))
  )

(defn test-three-very-short-planner []
  "Elapsed time:2.156133333 msecs"
  (time (planner test-state-three '(wears R halonic-garb) very-short-ops-planner))
  )

(defn test-three-short-opssearch []
  "Elapsed time: 355.0893333  msecs"
  (time (ops-search test-state-three '((wears R halonic-garb)) short-ops-opssearch))
  )

(defn test-three-short-planner []
  "Elapsed time: 21.12256667 msecs"
  (time (planner test-state-three '(wears R halonic-garb) short-ops-planner))
  )

(defn test-three-medium-opssearch []
  "Elapsed time:378.1288333  msecs"
  (time (ops-search test-state-three '((wears R halonic-garb)) medium-ops-opssearch))
  )

(defn test-three-medium-planner []
  "Elapsed time: 21.6971 msecs"
  (time (planner test-state-three '(wears R halonic-garb) medium-ops-planner))
  )

(defn test-three-large-opssearch []
  "Elapsed time: 431.8688333 msecs"
  (time (ops-search test-state-three '((wears R halonic-garb)) large-ops-opssearch))
  )

(defn test-three-large-planner []
  "Elapsed time: 21.7216  msecs"
  (time (planner test-state-three '(wears R halonic-garb) large-ops-planner))
  )

(defn test-three-very-large-opssearch []
  "Elapsed time: 556.285 msecs"
  (time (ops-search test-state-three '((wears R halonic-garb)) very-large-ops-opssearch))
  )

(defn test-three-very-large-planner []
  "Elapsed time: 20.82706667 msecs"
  (time (planner test-state-three '(wears R halonic-garb) very-large-ops-planner))
  )