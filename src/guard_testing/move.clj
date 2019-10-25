(ns guard-testing.move
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]))

(comment "Guard that room 1 and room 2 aren't the same room.")

(def base-move-op
    '{move {:pre ((agent ?agent)
                (room ?room1)
                (room ?room2)
                (door ?door)
                (opened ?door true)
                (unlocked ?door true)
                (connects ?door ?room1)
                (connects ?door ?room2)
                (in ?agent ?room1)
                )
            :add ((in ?agent ?room2))
            :del ((in ?agent ?room1))
            :txt (?agent has moved from ?room1 to ?room2)
          }
    }
  )