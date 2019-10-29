(ns ops-list-size.ops-vs-planner.test-6
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            [planner.planner :refer :all]
            [ops-list-size.ops-vs-planner.operations :refer :all]))

(comment "This test uses a similar goal to test 5, but simplifies the scenario to only contain the tuples
          necessary for reaching this goal (e.g. no additional wounded agents or holdable items).
          The very-large operations lists are still the only lists able to solve this problem.

          The drawn conclusions are the same as test 5 although ops-search doesn't have the same Stack Overflow issue
          on any of its operator lists due to having less paths to take.

          Planner does take less time to solve this test than it took in test 6. It's not certain, but it's likely
          that the additional time in test 5 was due to the ':when' matching and/or the planner having to retreat
          on dead-end paths.")

(def test-state-six
  "Simplified state containing some rooms, holdable items, a wearable item, an altar, and a wounded agent."
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
  "Elapsed time: 69.43773333 msecs"
  (time (ops-search test-state-six '((agent priest)) very-short-ops-opssearch))
  )

(defn test-six-very-short-planner []
  "Elapsed time: 3.593433333 msecs"
  (time (planner test-state-six '(agent priest) very-short-ops-planner))
  )

(defn test-six-short-opssearch []
  "Elapsed time: 378.0087 msecs"
  (time (ops-search test-state-six '((agent priest)) short-ops-opssearch))
  )

(defn test-six-short-planner []
  "Elapsed time:3.507866667  msecs"
  (time (planner test-state-six '(agent priest) short-ops-planner))
  )

(defn test-six-medium-opssearch []
  "Elapsed time:1225.955967  msecs"
  (time (ops-search test-state-six '((agent priest)) medium-ops-opssearch))
  )

(defn test-six-medium-planner []
  "Elapsed time:3.785466667  msecs"
  (time (planner test-state-six '(agent priest) medium-ops-planner))
  )

(defn test-six-large-opssearch []
  "Elapsed time: 2494.141433 msecs"
  (time (ops-search test-state-six '((agent priest)) large-ops-opssearch))
  )

(defn test-six-large-planner []
  "Elapsed time: 4.0594 msecs"
  (time (planner test-state-six '(agent priest) large-ops-planner))
  )

(defn test-six-very-large-opssearch []
  "Elapsed time: 3686.441333 msecs"
  (time (ops-search test-state-six '((agent priest)) very-large-ops-opssearch))
  )

(defn test-six-very-large-planner []
  "Elapsed time: 99.62633333 msecs"
  (time (planner test-state-six '(agent priest) very-large-ops-planner))
  )