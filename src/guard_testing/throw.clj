(ns guard-testing.throw
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]))

(comment "Guard that room 1 and room 2 aren't the same room.")

(def op-throw-base
  '{throw {:pre ((agent ?agent)
                 (room ?room1)
                 (room ?room2)
                 (door ?door)
                 (holdable ?obj)
                 (in ?agent ?room1)
                 (holds ?agent ?obj)
                 (connects ?door ?room1)
                 (connects ?door ?room2)
                 (unlocked ?door true)
                 (opened ?door true))
           :add ((holds ?agent nil)
                 (in ?obj ?room2))
           :del ((holds ?agent ?obj))
           :txt ((?agent throws the ?obj into ?room2))
           }
      }
  )

(def op-throw-guarded-1
  '{throw {:pre ((agent ?agent)
                 (room ?room1)
                 (room ?room2)
                 (door ?door)
                 (holdable ?obj)
                 (in ?agent ?room1)
                 (holds ?agent ?obj)
                 (connects ?door ?room1)
                 (connects ?door ?room2)
                 (unlocked ?door true)
                 (opened ?door true)
                 (:guard (not= (? room1) (? room2)))
                 )
           :add ((holds ?agent nil)
                 (in ?obj ?room2))
           :del ((holds ?agent ?obj))
           :txt ((?agent throws the ?obj into ?room2))
           }
    }
)

(def op-throw-guarded-2
  '{throw {:pre ((agent ?agent)
                 (room ?room1)
                 (room ?room2)
                 (:guard (not= (? room1) (? room2)))
                 (door ?door)
                 (holdable ?obj)
                 (in ?agent ?room1)
                 (holds ?agent ?obj)
                 (connects ?door ?room1)
                 (connects ?door ?room2)
                 (unlocked ?door true)
                 (opened ?door true)
                 )
           :add ((holds ?agent nil)
                 (in ?obj ?room2))
           :del ((holds ?agent ?obj))
           :txt ((?agent throws the ?obj into ?room2))
           }
    }
)