(ns ops-list-size.ops-list-size-planner
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]))

(def very-short-ops
  '{
     move
     {
       :name move-agent
       :achieves (in ?agent ?room2)
       :when ((agent ?agent)
              (room ?room1)
              (room ?room2)
              (in ?agent ?room1)
              )
       :post ()
       :pre ()
       :add ((in ?agent ?room2))
       :del ((in ?agent ?room1))
       :txt (agent ?agent has moved from ?room1 to ?room2)
     }
    }
  )

(def short-ops
  '{

    })

(def base-ops
  '{

    })


(def test-state-one
  '#{
     
     })