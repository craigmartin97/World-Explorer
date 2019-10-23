(ns planner_vs_opssearch.opssearch
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]))

(def operations
  '{move { :pre ((agent ?agent)
            (in ?agent ?room1)
            (room ?room1)
            (room ?room2)
            )
           :add ((in ?agent ?room2))
           :del ((in ?agent ?room1))
           :txt (?agent has moved from ?room1 to ?room2)
     }
    pickup {:pre ((agent ?agent)
            (room ?room1)
            (holdable ?obj)
            (in ?agent ?room1)
            (in ?obj ?room1)
            (holds ?agent ??x))
           :add ((holds ?agent ??x ?obj))
           :del ((holds ?agent ??x))
           :txt (?agent picked up ?obj from ?room1)
    }
    drop {:pre ((agent ?agent)
            (room ?room1)
            (holdable ?obj)
            (in ?agent ?room1)
            (holds ?agent ??x ?obj ??y))
           :add ((holds ?agent ??x ??y) (in ?obj ?room1))
           :del ((holds ?agent ??x ?obj ??y))
           :txt (?agent dropped ?obj in ?room1)
     }

   })


(def state
  '#{
     ;define agentLL
     (agent R)
     ;define rooms
     (room A)
     (room B)
     (room C)
     (room D)
     (room E)
     (room F)
     (room G)
     (room H)
     (room I)
     (room J)
     (room K)

     (in R A)
     (holds R)

     (holdable key)
     (in key D)

     (holdable dog)
     (in dog A)

     (holdable cat)
     (in cat A)

     (holdable mouse)
     (in mouse A)

     }
  )