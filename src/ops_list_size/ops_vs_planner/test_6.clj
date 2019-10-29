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