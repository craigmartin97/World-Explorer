(ns goal-state-size.core
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            [clj-memory-meter.core :as mm]

            )
  )

(use 'clojure.tools.trace)




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
            (holds ?agent nil)
            (in ?obj ?room1)
            )
     :add
          (
            (holds ?agent ?obj)
            )
     :del
          (
            (holds ?agent nil)
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
            (holds ?agent ?obj)
            )
     :add
          (
            (holds ?agent nil)
            (in ?obj ?room1)
            )
     :del
          (
            (holds ?agent ?obj)
            )
     :txt (?agent dropped ?obj in ?room1)
     }
    }
  )

;test one - (time (ops-search move-A-D-all-unlocked '((in R K)) operations))
;test two - (time (ops-search move-A-D-all-unlocked '((in R K) (opened )) operations))
(def move-A-D-all-unlocked
  "A more advanced scenario"
  '#{
     ;define agent
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
     ;define doors
     (door A-B)
     (door A-C)
     (door A-D)
     (door B-E)
     (door C-F)
     (door D-K)
     (door E-G)
     (door G-H)
     (door E-I)
     (door I-J)
     ;define connections (connects door room)
     (connects A-B A)
     (connects A-B B)
     (connects A-C A)
     (connects A-C C)
     (connects A-D A)
     (connects A-D D)
     (connects B-E B)
     (connects B-E E)
     (connects C-F C)
     (connects C-F F)
     (connects D-K D)
     (connects D-K K)
     (connects E-G E)
     (connects E-G G)
     (connects E-I E)
     (connects E-I I)
     (connects G-H G)
     (connects G-H H)
     (connects I-J I)
     (connects I-J J)
     ;define where agent is in which room
     (in R A)
     ;define the state of the doors, open or closed
     (opened A-B true)
     (opened A-C true)
     (opened A-D true)
     (opened B-E true)
     (opened C-F true)
     (opened D-K true)
     (opened E-G true)
     (opened G-H true)
     (opened E-I true)
     (opened I-J true)
     ;define if the doors are locked or unlocked
     (unlocked A-B true)
     (unlocked A-C true)
     (unlocked A-D true)
     (unlocked B-E true)
     (unlocked C-F true)
     (unlocked D-K true)
     (unlocked E-G true)
     (unlocked G-H true)
     (unlocked E-I true)
     (unlocked I-J true)
     ;specify keys for the doors
     (key key-B-E)
     (holdable key-B-E)
     (unlocks key-B-E B-E)
     (in key-B-E B)

     (key key-A-D)
     (holdable key-A-D)
     (unlocks key-A-D A-D)
     (in key-A-D A)

     ;test 1-4
     (holds R key-A-D)

     ;test 5
     ;(holds R nil)
     }
  )

;helper functions to call to achieve results of tests
(defn test-one []
  "Move the agent from room A to room D
  A move from one room to another

  Elapsed time: 3046.6834 msecs
  "
  (time (ops-search move-A-D-all-unlocked '((in R D)) operations :debug true))
  )

(defn test-two []
  "Move the agent from room A to room D and close the door behind

  Elapsed time: 15371.377 msecs"
  (time (ops-search move-A-D-all-unlocked '((in R D) (opened A-D false)) operations :debug true))
  )

(defn test-three []
  "Move the agent from room A to room D and close the door behind and lock the door

  Elapsed time: 70883.8343 msecs"
  (time (ops-search move-A-D-all-unlocked '((in R D) (opened A-D false) (unlocked A-D false)) operations :debug true))
  )

(defn test-four []
  "Move the agent from room A to room D and close the door behind and lock the door.
  The agent will then drop the key at the after locking the door
  moves room -> closes door -> locks door -> drops key (total 4 ops)

  Elapsed time: 275409.6173 msecs"
  (time (ops-search move-A-D-all-unlocked '((in R D) (opened A-D false) (unlocked A-D false) (holds R nil)) operations))
  )

(defn test-five []
  "Move the agent from room A to room D and close the door behind and lock the door.
  The agent will then drop the key at the after locking the door
  moves room -> closes door -> locks door -> drops key (total 4 ops)

  Elapsed time: unsolvable in reasonable time (left for over 1 hour)"
  (time (ops-search move-A-D-all-unlocked '((holds R key-A-D) (in R D) (opened A-D false) (unlocked A-D false) (holds R nil)) operations :debug true))
  )


;-----------------------------------------------------------------------------------------------------------------------
;-----------------------------------------------------------------------------------------------------------------------
;-----------------------------------------------------------------------------------------------------------------------
;--------------------------------------------Does Order Matter?---------------------------------------------------------------------------
;-----------------------------------------------------------------------------------------------------------------------
;-----------------------------------------------------------------------------------------------------------------------
;-----------------------------------------------------------------------------------------------------------------------



(defn test-two-change-order []
  "Move the agent from room A to room D and close the door behind

  Elapsed time: 15482.871"
  (time (ops-search move-A-D-all-unlocked '((opened A-D false) (in R D) ) operations))
  )

(defn test-three-change-order []
  "Move the agent from room A to room D and close the door behind and lock the door

  Elapsed time: 71461.0799"
  (time (ops-search move-A-D-all-unlocked '((unlocked A-D false) (opened A-D false) (in R D)  ) operations))
  )


;-----------------------------------------------------------------------------------------------------------------------
;-----------------------------------------------------------------------------------------------------------------------
;-----------------------------------------------------------------------------------------------------------------------
;-------------------------------------------------------------Deeper Depths----------------------------------------------------------
;-----------------------------------------------------------------------------------------------------------------------
;-----------------------------------------------------------------------------------------------------------------------
;-----------------------------------------------------------------------------------------------------------------------


(def closed-doors-state
  "A more advanced scenario"
  '#{


     ;define where agent is in which room
     (in R A)
     ;define the state of the doors, open or closed
     (opened A-B false)
     (opened A-C false)
     (opened A-D false)
     (opened B-E false)
     (opened C-F false)
     (opened D-K false)
     (opened E-G false)
     (opened G-H false)
     (opened E-I false)
     (opened I-J false)
     ;define if the doors are locked or unlocked
     (unlocked A-B true)
     (unlocked A-C true)
     (unlocked A-D true)
     (unlocked B-E true)
     (unlocked C-F true)
     (unlocked D-K true)
     (unlocked E-G true)
     (unlocked G-H true)
     (unlocked E-I true)
     (unlocked I-J true)

     ;specify keys for the doors

     (in key-A-D A)
     (in key-A-B A)
     (in key-A-C A)
     (in key-B-E B)
     (in key-C-F C)
     (in key-D-K D)
     (in key-E-G E)
     (in key-G-H G)
     (in key-E-I E)
     (in key-I-J I)

     ;test 5
     (holds R nil)
     }
  )

(def world
  '#{
     (agent R)

     (door A-B)
     (door A-C)
     (door A-D)
     (door B-E)
     (door C-F)
     (door D-K)
     (door E-G)
     (door G-H)
     (door E-I)
     (door I-J)

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

     (connects A-B A)
     (connects A-B B)
     (connects A-C A)
     (connects A-C C)
     (connects A-D A)
     (connects A-D D)
     (connects B-E B)
     (connects B-E E)
     (connects C-F C)
     (connects C-F F)
     (connects D-K D)
     (connects D-K K)
     (connects E-G E)
     (connects E-G G)
     (connects E-I E)
     (connects E-I I)
     (connects G-H G)
     (connects G-H H)
     (connects I-J I)
     (connects I-J J)

     (key key-A-D)
     (key key-A-B)
     (key key-A-C)
     (key key-C-F)
     (key key-B-E)
     (key key-D-K)
     (key key-E-G)
     (key key-E-I)
     (key key-G-H)
     (key key-I-J)

     (holdable key-A-D)
     (holdable key-A-B)
     (holdable key-A-C)
     (holdable key-C-F)
     (holdable key-B-E)
     (holdable key-D-K)
     (holdable key-E-G)
     (holdable key-E-I)
     (holdable key-G-H)
     (holdable key-I-J)

     (unlocks key-A-D A-D)
     (unlocks key-A-B A-B)
     (unlocks key-A-C A-C)
     (unlocks key-C-F C-F)
     (unlocks key-B-E B-E)
     (unlocks key-D-K D-K)
     (unlocks key-E-G E-G)
     (unlocks key-E-I E-I)
     (unlocks key-G-H G-H)
     (unlocks key-I-J I-J)
     })

(defn test-six []
  "Test six opens door A-D and door C-F

  Elapsed time: 76957.0342 msecs"
  (time (ops-search closed-doors-state '((opened A-D true) (opened C-F true)) operations :world world :debug true))
)

(defn test-seven []
  "Test seven opens door A-B, opens door A-D and opens the door C-F

  Elapsed time: 198102.5083 msecs"
  (time (ops-search closed-doors-state '((opened A-D true) (opened C-F true) (opened A-B true)) operations
                    :world world :debug true))
)

(defn test-eight []
  "Test eight will have to pick up a key, lock a door, open a door, move and open another door"
  (time (ops-search closed-doors-state '((opened A-D true) (opened C-F true) (unlocked A-B false)) operations
                    :world world :debug true))
  )

(defn test-nine []
  "Test nine opens door A-D, door C-F, door A-B
   and picks up key-C-F

  Elapsed time: unsolvable
   "
  (time (ops-search closed-doors-state '((opened A-D true) (opened C-F true) (opened A-B true) (holds R key-C-F)) operations
                    :world world :debug true))
)


(defn test-ten []
  "Test ten tries to get the agent to move from the first room
  to the furthest room away which is the deepest depth.

  Elapsed time: unsolvable"
  (time (ops-search closed-doors-state '((in R J)) operations :world world :debug true))
)