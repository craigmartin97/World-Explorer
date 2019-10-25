(ns tuple-order_ops.open
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            [clj-memory-meter.core :as mm]
            )
  )

;---------------------------------------------------------------
;---------------------------------------------------------------
;--------------------------Operations for open-------------------------------------
;---------------------------------------------------------------
;---------------------------------------------------------------

(def base-opened-one
  "A map of operations that the agent can perform in the world
  This operator set only contains the open operator

  we have placed the tuples in an order we assumed to be most efficient."
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
    }
  )

(def base-opened-two
  "A map of operations that the agent can perform in the world
  This operator set only contains the open operator

  The (in ?agent ?room1) has been moved to the top so the
  test it will eliminate other rooms that the agent isnt in.
  Therefore the rest of the queries will and checks will all remain using the same room.
  "
  '{
    open
    {
     :pre
          (
           (agent ?agent)
           (in ?agent ?room1)
           (room ?room1)
           (room ?room2)
           (door ?door)
           (opened ?door false)
           (unlocked ?door true)
           (connects ?door ?room1)
           (connects ?door ?room2)

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
    }
  )

(def base-opened-three
  "A map of operations that the agent can perform in the world
  This operator set only contains the open operator

  The (connects ?door ?room1) has been moved further up the list
  as to eliminate any other doors being checks as it can only be a handful
  of doors connected to room1 which we have already found.

  Once this door has been found we then validate it is a door, and check if the door
  is opened and locked.

  Finally, we check that the room1 is a room and that the door connects to room2 as well.
  "
  '{
    open
    {
     :pre
          (
           (agent ?agent)
           (in ?agent ?room1)
           (connects ?door ?room1)
           (door ?door)
           (opened ?door false)
           (unlocked ?door true)
           (room ?room1)
           (connects ?door ?room2)
           (room ?room2)
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
    }
  )

(def state-opened
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

     (opened A-B false)
     (opened B-C false)
     (opened C-D false)
     (opened D-E false)

     (unlocked A-B true)
     (unlocked B-C true)
     (unlocked C-D true)
     (unlocked D-E true)

     (in R A)
     }
  )

;--------------------------------------------------------------
;--------------------------------------------------------------
;----------------------------tests for open----------------------------------
;--------------------------------------------------------------
;--------------------------------------------------------------

(defn test-open-one []
  "Elapsed time: 69.1724 msecs"
  (time (ops-search state-opened '((opened A-B true)) base-opened-one) )
  )

(defn test-open-two []
  "Elapsed time: 10.3939 msecs"
  (time (ops-search state-opened '((opened A-B true)) base-opened-two) )
  )

(defn test-open-three []
  "Elapsed time: 1.6453 msecs"
  (time (ops-search state-opened '((opened A-B true)) base-opened-three) )
  )

;--------------------------------------------------------------
;--------------------------------------------------------------
;----------------------------More advanced state----------------------------------
;--------------------------------------------------------------
;--------------------------------------------------------------

(def advanced-state-open
  "more advanced state to test a more challenging scenario"
  '#{
     (agent R)
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

     (door A-B)
     (door A-C)
     (door A-D)
     (door A-E)
     (door A-F)
     (door B-G)
     (door B-H)
     (door B-I)
     (door C-G)
     (door C-H)
     (door C-I)
     (door D-H)
     (door D-I)
     (door D-J)
     (door E-I)
     (door C-J)
     (door C-K)

     ;status of door
     (opened A-B false)
     (opened A-C false)
     (opened A-D false)
     (opened A-E false)
     (opened A-F false)
     (opened B-G false)
     (opened B-H false)
     (opened B-I false)
     (opened C-G false)
     (opened C-H false)
     (opened C-I false)
     (opened D-H false)
     (opened D-I false)
     (opened D-J false)
     (opened E-I false)
     (opened C-J false)
     (opened C-K false)

     (unlocked A-B true)
     (unlocked A-C true)
     (unlocked A-D true)
     (unlocked A-E true)
     (unlocked A-F true)
     (unlocked B-G true)
     (unlocked B-H true)
     (unlocked B-I true)
     (unlocked C-G true)
     (unlocked C-H true)
     (unlocked C-I true)
     (unlocked D-H true)
     (unlocked D-I true)
     (unlocked D-J true)
     (unlocked E-I true)
     (unlocked C-J true)
     (unlocked C-K true)

     ;A
     (connects A-B A)
     (connects A-B B)

     (connects A-C A)
     (connects A-C C)

     (connects A-D A)
     (connects A-D D)

     (connects A-E A)
     (connects A-E E)

     (connects A-F A)
     (connects A-F F)

     ;B
     (connects B-G B)
     (connects B-G G)

     (connects B-H B)
     (connects B-H H)

     (connects B-I B)
     (connects B-I I)

     ;C
     (connects C-G C)
     (connects C-G G)

     (connects C-H C)
     (connects C-H H)

     (connects C-I C)
     (connects C-I I)

     ;D
     (connects D-H D)
     (connects D-H H)

     (connects D-I D)
     (connects D-I I)

     (connects D-J D)
     (connects D-J J)

     ;E
     (connects E-I E)
     (connects E-I I)

     (connects E-J E)
     (connects E-J J)

     (connects E-K E)
     (connects E-K K)

     (in R A)
     }
  )

;-----------------------------------------
;-----------------------------------------
;------------------Tests for open adv scenraio-----------------------
;-----------------------------------------
;-----------------------------------------

(defn adv-test-opened-one []
  "Elapsed time: 41.553 msecs"
  (time (ops-search advanced-state-open '((opened A-B true)) base-opened-one) )
  )

(defn adv-test-opened-two []
  "Elapsed time: 10.2528 msecs"
  (time (ops-search advanced-state-open '((opened A-B true)) base-opened-two) )
  )

(defn adv-test-opened-three []
  "Elapsed time: 1.5414 msecs"
  (time (ops-search advanced-state-open '((opened A-B true)) base-opened-three) )
  )