(ns tuple-order.move
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            [clj-memory-meter.core :as mm]
            )
  )

(comment
  "I am going to create multiple sets of operators with the tuples
  and operators in different orders to see if it improves the speed and efficiency of the ops-searcher
  "
  )

(def base-move-one
  "A map of operations that the agent can perform in the world
  This operator set only contains the move operator

  we have placed the tuples in an order we assumed to be most efficient."
  '{
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
    }
  )

(def base-move-two
  "A map of operations that the agent can perform in the world
  This operator set only contains the move operator

  We have rearranged the tuples with the '2 set tuples' at the bottom
  as an inital test to see if anything changes"
  '{
    move
    {
     :pre
          (
           (opened ?door true)
           (unlocked ?door true)
           (connects ?door ?room1)
           (connects ?door ?room2)
           (in ?agent ?room1)
           (agent ?agent)
           (room ?room1)
           (room ?room2)
           (door ?door)
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
    }
  )

(def base-move-three
  "A map of operations that the agent can perform in the world
  This operator set only contains the move operator

  We moved the tuple to check which room the agent is in to the top of the :pre conditions
  this was done as it ensured that one of the rooms being checked would always be the room
  where the agent is currently located."
  '{


    move
    {
     :pre
          (
           (in ?agent ?room1)
           (opened ?door true)
           (unlocked ?door true)
           (connects ?door ?room1)
           (connects ?door ?room2)
           (agent ?agent)
           (room ?room1)
           (room ?room2)
           (door ?door)
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
    }
  )

(def base-move-four
  "A map of operations that the agent can perform in the world
  This operator set only contains the move operator

  We moved the connects tuples towards the top of the :pre so we could limit
  which doors where looked at by ensuring the doors where connected to room one"
  '{


    move
    {
     :pre
          (
           (in ?agent ?room1)
           (connects ?door ?room1)
           (connects ?door ?room2)
           (opened ?door true)
           (unlocked ?door true)
           (agent ?agent)
           (room ?room1)
           (room ?room2)
           (door ?door)
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
    }
  )

(def base-move-five
  "A map of operations that the agent can perform in the world
  This operator set only contains the move operator

  We have moved the agent check to the top of the :pre conditions.
  So the keys arent confused for agents as they both use the same keyword 'in'."
  '{
    move
    {
     :pre
          (
           (agent ?agent)
           (in ?agent ?room1)
           (connects ?door ?room1)
           (connects ?door ?room2)
           (opened ?door true)
           (unlocked ?door true)
           (room ?room1)
           (room ?room2)
           (door ?door)
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
    }
  )

(def base-move-six
  "A map of operations that the agent can perform in the world
  This operator set only contains the move operator

  We have moved the '2 tuple' sets directly below where their matcher variables are first defined
  We deduced this would make it more efficent for more complex scenarios.
  For example, once you have a door its better to check that its unlocked and open before
  figuring out which room its connected to.

  This is quicker because the second connects tuple can be 2 values. Specifying it later means
  that the opened/unlocked door tuples aren't checked twice."
  '{
    move
    {
     :pre
          (
           (agent ?agent)
           (in ?agent ?room1)
           (connects ?door ?room1)
           (door ?door)
           ;entrypy reduction
           (opened ?door true)
           (unlocked ?door true)
           (room ?room1)
           (connects ?door ?room2)
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
    }
  )

(def state-move
  "basic state for agent to move from one room to next"
  '#{
     (agent R)
     (room A)
     (room B)
     (room C)
     (room D)
     (room E)

     (door A-B)
     (door B-C)
     (door C-D)
     (door D-E)

     (connects A-B A)
     (connects A-B B)

     (connects B-C B)
     (connects B-C C)

     (connects C-D C)
     (connects C-D D)

     (connects D-E D)
     (connects D-E E)

     (opened A-B true)
     (opened B-C true)
     (opened C-D true)
     (opened D-E true)

     (unlocked A-B true)
     (unlocked B-C true)
     (unlocked C-D true)
     (unlocked D-E true)

     (in R A)
     }
  )


;----------------------------------------------------
;----------------------------------------------------
;----------------------Tests for move------------------------------
;----------------------------------------------------
;----------------------------------------------------
(defn test-one []
  (time (ops-search state-move '((in R B)) base-move-one) )
  )

(defn test-two []
  (time (ops-search state-move '((in R B)) base-move-two) )
  )

(defn test-three []
  (time (ops-search state-move '((in R B)) base-move-three) )
  )

(defn test-four []
  (time (ops-search state-move '((in R B)) base-move-four) )
  )

(defn test-five []
  (time (ops-search state-move '((in R B)) base-move-five) )
  )

(defn test-six []
  (time (ops-search state-move '((in R B)) base-move-six) )
  )