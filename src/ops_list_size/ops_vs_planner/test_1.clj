(ns ops-list-size.ops-vs-planner.test-1
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            [planner.planner :refer :all]
            [ops-list-size.ops-vs-planner.operations :refer :all]))

(comment "Simple test using a very basic world. The agent can only use the move operator to move from one room to another.
          Ops search performs better than planner due to the miniscule amount of possibilities.

          Ops search took longer as more operators were added whilst planner remained seemingly unaffected.
          This is because planner will only use operators that are relevant to it's current goal by looking at an operators ':achieves' key.
          Meanwhile, ops search will have to check every possible combination with each operator, even if the operator is irrelevant.")

(def test-state-one
  "Simple state with two rooms."
  '#{(agent R)

     (room A)
     (room B)

     (in R A)
     })

(defn test-one-very-short-opssearch []
  "Elapsed time: 3.110133333 msecs"
  (time (ops-search test-state-one '((in R B)) very-short-ops-opssearch))
  )

(defn test-one-very-short-planner []
  "Elapsed time: 12.96636667 msecs"
  (time (planner test-state-one '(in R B) very-short-ops-planner))
  )

(defn test-one-short-opssearch []
  "Elapsed time: 3.293133333 msecs"
  (time (ops-search test-state-one '((in R B)) short-ops-opssearch))
  )

(defn test-one-short-planner []
  "Elapsed time: 12.9435 msecs"
  (time (planner test-state-one '(in R B) short-ops-planner))
  )

(defn test-one-medium-opssearch []
  "Elapsed time: 3.7026 msecs"
  (time (ops-search test-state-one '((in R B)) medium-ops-opssearch))
  )

(defn test-one-medium-planner []
  "Elapsed time: 12.263  msecs"
  (time (planner test-state-one '(in R B) medium-ops-planner))
  )

(defn test-one-large-opssearch []
  "Elapsed time: 3.925966667 msecs"
  (time (ops-search test-state-one '((in R B)) large-ops-opssearch))
  )

(defn test-one-large-planner []
  "Elapsed time: 13.05133333 msecs"
  (time (planner test-state-one '(in R B) large-ops-planner))
  )

(defn test-one-very-large-opssearch []
  "Elapsed time: 5.544166667 msecs"
  (time (ops-search test-state-one '((in R B)) very-large-ops-opssearch))
  )

(defn test-one-very-large-planner []
  "Elapsed time: 12.13326667 msecs"
  (time (planner test-state-one '(in R B) very-large-ops-planner))
  )