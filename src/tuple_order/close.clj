(ns tuple-order.close
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            [clj-memory-meter.core :as mm]
            )
  )

;---------------------------------------------------------------
;---------------------------------------------------------------
;--------------------------Operations for close-------------------------------------
;---------------------------------------------------------------
;---------------------------------------------------------------

(def base-closed-one
  "A map of operations that the agent can perform in the world
  This operator set only contains the open operator

  we have placed the tuples in an order we assumed to be most efficent."
  '{
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
    }
  )

(def base-closed-two
  "A map of operations that the agent can perform in the world
  This operator set only contains the open operator

  The (in ?agent ?room1) has been moved to the top so the
  test it will elimate many other options of rooms that it could be
  "
  '{
    closed
    {
     :pre
          (
           (agent ?agent)
           (in ?agent ?room1)
           (room ?room1)
           (room ?room2)
           (door ?door)
           (opened ?door true)
           (unlocked ?door true)

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
    }
  )

(def base-closed-three
  "A map of operations that the agent can perform in the world
  This operator set only contains the open operator

    The (connects ?door ?room1) has been moved further up the list

    as to eliminate any other doors being checks as it can only be a handful
    of doors connected to room1 which we have already found.
    Once this door has been found we then validate it is a door, and check if the door
    is opened and locked.\n\n  Finally, we check that the room1 is a room and that the door connects to room2 as well.
  "
  '{
    closed
    {
     :pre
          (
           (agent ?agent)
           (in ?agent ?room1)
           (connects ?door ?room1)
           (door ?door)

           (opened ?door true)
           (unlocked ?door true)
           (room ?room1)
           (connects ?door ?room2)
           (room ?room2)
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
    }
  )

(def state-closed
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

;--------------------------------------------------------------
;--------------------------------------------------------------
;----------------------------tests for closed----------------------------------
;--------------------------------------------------------------
;--------------------------------------------------------------

(defn test-closed-one []
  "Elapsed time: 41.553 msecs"
  (time (ops-search state-closed '((opened A-B false)) base-closed-one) )
  )

(defn test-closed-two []
  "Elapsed time: 10.2528 msecs"
  (time (ops-search state-closed '((opened A-B false)) base-closed-two) )
  )

(defn test-closed-three []
  "Elapsed time: 1.5414 msecs"
  (time (ops-search state-closed '((opened A-B false)) base-closed-three) )
  )

;--------------------------------------------------------------
;--------------------------------------------------------------
;----------------------------More advanced state----------------------------------
;--------------------------------------------------------------
;--------------------------------------------------------------

(def advanced-state-closed
  "basic state for agent to move from one room to next"
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
     (opened A-B true)
     (opened A-C true)
     (opened A-D true)
     (opened A-E true)
     (opened A-F true)
     (opened B-G true)
     (opened B-H true)
     (opened B-I true)
     (opened C-G true)
     (opened C-H true)
     (opened C-I true)
     (opened D-H true)
     (opened D-I true)
     (opened D-J true)
     (opened E-I true)
     (opened C-J true)
     (opened C-K true)

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

;--------------------------------------------------------------
;--------------------------------------------------------------
;----------------------------advanced tests for closed----------------------------------
;--------------------------------------------------------------
;--------------------------------------------------------------

(defn adv-test-closed-one []
  "Elapsed time: 41.553 msecs"
  (time (ops-search advanced-state-closed '((opened A-B false)) base-closed-one) )
  )

(defn adv-test-closed-two []
  "Elapsed time: 10.2528 msecs"
  (time (ops-search advanced-state-closed '((opened A-B false)) base-closed-two) )
  )

(defn adv-test-closed-three []
  "Elapsed time: 1.5414 msecs"
  (time (ops-search advanced-state-closed '((opened A-B false)) base-closed-three) )
  )