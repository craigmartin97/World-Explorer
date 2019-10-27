(ns matcher-starter.ops-search-base-ops
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            [clj-memory-meter.core :as mm]
            )
  )

(def operations
  "A map of operations that the agent can perform in the world"
  '{
    open
    {
     :pre
          (
           (agent ?agent)
           (room ?room1)
           (room ?room2)
           (door ?door)
           (opened ?door false)
           (unlocked ?door true)
           (connects ?door ?room1)
           (connects ?door ?room2)
           (in ?agent ?room1)
           )
     :add
          (
           (opened ?door true)
           )
     :del
          (
           (opened ?door false)
           )
     :txt (?agent has opened ?door)
     }

    closed
    {
     :pre
          (
           (agent ?agent)
           (room ?room1)
           (room ?room2)
           (door ?door)
           (opened ?door true)
           (unlocked ?door true)
           (in ?agent ?room1)
           (connects ?door ?room1)
           (connects ?door ?room2)
           )
     :add
          (
           (opened ?door false)
           )
     :del
          (
           (opened ?door true)
           )
     :txt (?agent has closed ?door)
     }

    lock
    {
     :pre
          (
           (agent ?agent)
           (room ?room1)
           (room ?room2)
           (door ?door)
           (opened ?door false)
           (unlocked ?door true)
           (in ?agent ?room1)
           (connects ?door ?room1)


           (key ?key)
           (holdable ?key)
           (unlocks ?key ?door)
           (holds ?agent ?key)
           )
     :add
          (
           (unlocked ?door false)
           )
     :del
          (
           (unlocked ?door true)
           )
     :txt (?agent has locked ?door)
     }

    unlock
    {
     :pre
          (
           (agent ?agent)
           (room ?room1)
           (room ?room2)
           (door ?door)
           (opened ?door false)
           (unlocked ?door false)
           (in ?agent ?room1)
           (connects ?door ?room1)


           (holds ?agent ??_ ?key1 ??_)
           (key ?key)

           (holdable ?key)
           (unlocks ?key ?door)
           (holds ?agent ?key)
           )
     :add
          (
           (unlocked ?door true)
           )
     :del
          (
           (unlocked ?door false)
           )
     :txt (?agent has unlocked ?door)
     }

    move
    {
     :pre
          (
           (agent ?agent)
           (room ?room1)
           (room ?room2)
           (door ?door)
           (opened ?door true)
           (unlocked ?door true)
           (connects ?door ?room1)
           (connects ?door ?room2)
           (in ?agent ?room1)
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

    pickup
    {
     :pre
          (
           (agent ?agent)
           (room ?room1)
           (holdable ?obj)
           (in ?agent ?room1)
           (holds ?agent ??x)
           (in ?obj ?room1)
           )
     :add
          (
           (holds ?agent ??x ?obj)
           )
     :del
          (
           (holds ?agent ??x)
           (in ?obj ?room1)
           )
     :txt (?agent picked up ?obj from ?room1)
     }

    drop
    {
     :pre
          (
           (agent ?agent)
           (room ?room1)
           (holdable ?obj)
           (in ?agent ?room1)
           (holds ?agent ??x ?obj ??y)
           )
     :add
          (
           (holds ?agent ??x ??y)
           (in ?obj ?room1)
           )
     :del
          (
           (holds ?agent ??x ?obj ??y)
           )
     :txt (?agent dropped ?obj in ?room1)
     }
    }
  )


;---------------------------------------
;---------------------------------------
;------------------Compare ops-search planner ops---------------------
;---------------------------------------
;---------------------------------------

(def ops-search-compare-operations
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
            :del (
                  (in ?obj ?room1)
                  (holds ?agent ??x)
                  )
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