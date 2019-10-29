(ns optimal-route.core
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            [clojure.set :refer :all]
            [planner.planner :refer :all]
            )
  )

;-------------------------------------------------------------
;-------------------------------------------------------------
;----------------------------Operations---------------------------------
;-------------------------------------------------------------
;-------------------------------------------------------------

(def ops-operations-efficent
  "A map of operations that the agent can perform in the world
  In this ops list the agent will pick up the items in the most efficent order"
  '{move
    {:pre ((agent ?agent)
            (in ?agent ?room1)
            (room ?room1)
            (room ?room2)
            )
     :add ((in ?agent ?room2))
     :del ((in ?agent ?room1))
     :txt (?agent has moved from ?room1 to ?room2)
     }

    pickup
    {:pre ((agent ?agent)
            (room ?room1)
            (holdable ?obj)
            (in ?agent ?room1)

            (in ?obj ?room1)
            )
     :add ((holds ?agent ?obj)
            )
     :del ((holds ?agent nil)
            (in ?obj ?room1)
            )
     :txt (?agent picked up ?obj from ?room1)
     }
    drop
    {:pre ((agent ?agent)
            (room ?room1)
            (holdable ?obj)
            (in ?agent ?room1)
            (holds ?agent ?obj)
            )
     :add ((holds ?agent nil)
            (in ?obj ?room1)
            )
     :del ((holds ?agent ?obj)
            )
     :txt (?agent dropped ?obj in ?room1)
     }
    }
  )

(def ops-operations-inefficent
  "In this ops list the agent wont pick up the objects in the most
  efficent order"
  '{
    move
    {:pre ((agent ?agent)
            (in ?agent ?room1)
            (room ?room1)
            (room ?room2)
            )
     :add ((in ?agent ?room2))
     :del ((in ?agent ?room1))
     :txt (?agent has moved from ?room1 to ?room2)
     }
    pickup
    {:pre ((agent ?agent)
            (room ?room1)
            (holdable ?obj)
            (in ?agent ?room1)
            (in ?obj ?room1)
            (holds ?agent ??x))
     :add ((holds ?agent ??x ?obj))
     :del ((in ?obj ?room1)
            (holds ?agent ??x)
            )
     :txt (?agent picked up ?obj from ?room1)
     }
    drop
    {:pre ((agent ?agent)
            (room ?room1)
            (holdable ?obj)
            (in ?agent ?room1)
            (holds ?agent ??x ?obj ??y))
     :add ((holds ?agent ??x ??y) (in ?obj ?room1))
     :del ((holds ?agent ??x ?obj ??y))
     :txt (?agent dropped ?obj in ?room1)
     }
    })

;--------------------------------------
;--------------------------------------
;-------------------States-------------------
;--------------------------------------
;--------------------------------------

(def state
  "Basic state with many room and a few objects"
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

;------------------------------------
;------------------------------------
;------------------tests------------------
;------------------------------------
;------------------------------------

(defn planner-logical-order []
  "the agent will pick up the objects in a logical order
  the items are stated in the goal state in a logical efficent order

  The agent will pick up the dog and cat then move and pick up the key"
  (time (planner state '(holds R dog cat key) planner-operations))
  )

(defn planner-illogical-order []
  "Items are added in an inefficent order

  Planner here will move to the key first and pick it upm then move all the way back and pick up the dog and cat"
  (time (planner state '(holds R key dog cat) planner-operations))
  )

(defn ops-logical-order-efficent []
  "The agent uses ops-search and pick up the dog then the cat then the key"
  (time (ops-search state '((holds R dog) (holds R cat) (holds R key)) ops-operations-efficent))
  )

(defn ops-illogical-order-efficent []
  "The agent uses ops-search and pick up the dog then the cat then the key
  even though they are specified in a illogical order"
  (time (ops-search state '((holds R key) (holds R dog) (holds R cat)) ops-operations-efficent))
  )

(defn ops-illogical-order-inefficent []
  "The agent uses ops-search and pick up the dog then the cat then the key
  even though they are specified in a illogical order"
  (time (ops-search state '((holds R key dog cat)) ops-operations-inefficent))
  )