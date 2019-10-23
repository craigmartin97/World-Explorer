(ns planner_vs_opssearch.opssearch
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]))

(def operations
  '{
    move
    {
     :pre
          (
            (agent ?agent)
            (in ?agent ?room1)
            (opened ?door true)
            (unlocked ?door true)
            (door ?door)
            (room ?room1)
            (room ?room2)



            )
     :add
          (
            (in ?agent ?room2)
            )
     :del
          (
            (in ?agent ?room1)
            )
     :txt (?agent has moved from ?room1 to ?room2)
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

     (door A)
     (door B)
     (door C)
     (door D)
     (door E)
     (door F)
     (door G)
     (door H)
     (door I)
     (door J)
     (door K)

     (opened A true)
     (opened B true)
     (opened C true)
     (opened D true)
     (opened E true)
     (opened F true)
     (opened G true)
     (opened H true)
     (opened I true)
     (opened J true)
     (opened K true)

     (unlocked A true)
     (unlocked B true)
     (unlocked C true)
     (unlocked D true)
     (unlocked E true)
     (unlocked F true)
     (unlocked G true)
     (unlocked H true)
     (unlocked I true)
     (unlocked J true)
     (unlocked K true)

     (in R A)
     }
  )