(ns ops-list-size.ops-vs-planner.test-2
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            [planner.planner :refer :all]
            [ops-list-size.ops-vs-planner.operations :refer :all]))

(comment "The test scenario now offers multiple rooms to move around an an item to pick up.
          Surprisingly, planner starts to clearly outperform ops search even with this small state.

          This test further distinguishes the differences between ops search and planner when more operators are added.
          Ops search is having to repeat its "":precondition"" matcher checks for many nodes at multiple depths, causing an
          increase in processing time need to terminate.")

(def test-state-two
  "State including a few rooms and a holdable item"
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
  "Elapsed time: 27.33423333 msecs"
  (time (ops-search test-state-two '((holds R halonic-garb)) very-short-ops-opssearch))
  )

(defn test-two-very-short-planner []
  "Elapsed time: 14.82483333 msecs"
  (time (planner test-state-two '(holds R halonic-garb) very-short-ops-planner))
  )

(defn test-two-short-opssearch []
  "Elapsed time: 31.73306667 msecs"
  (time (ops-search test-state-two '((holds R halonic-garb)) short-ops-opssearch))
  )

(defn test-two-short-planner []
  "Elapsed time: 15.5788 msecs"
  (time (planner test-state-two '(holds R halonic-garb) short-ops-planner))
  )

(defn test-two-medium-opssearch []
  "Elapsed time: 34.89746667  msecs"
  (time (ops-search test-state-two '((holds R halonic-garb)) medium-ops-opssearch))
  )

(defn test-two-medium-planner []
  "Elapsed time: 15.19706667 msecs"
  (time (planner test-state-two '(holds R halonic-garb) medium-ops-planner))
  )

(defn test-two-large-opssearch []
  "Elapsed time: 39.4169 msecs"
  (time (ops-search test-state-two '((holds R halonic-garb)) large-ops-opssearch))
  )

(defn test-two-large-planner []
  "Elapsed time: 16.44383333 msecs"
  (time (planner test-state-two '(holds R halonic-garb) large-ops-planner))
  )

(defn test-two-very-large-opssearch []
  "Elapsed time: 44.82136667 msecs"
  (time (ops-search test-state-two '((holds R halonic-garb)) very-large-ops-opssearch))
  )

(defn test-two-very-large-planner []
  "Elapsed time: 16.7997  msecs"
  (time (planner test-state-two '(holds R halonic-garb) very-large-ops-planner))
  )