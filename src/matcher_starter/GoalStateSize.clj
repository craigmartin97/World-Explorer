(comment
"In this experiment I am evaluating the efficency of the
both the ops-search and the planner inference engines in respect to
the size of the goal state size.

In these tests I will keep the size of the state and operators lists
the same but change the quantity of goal states

I will be measuring the results by:
 1) the amount of time taken for each function to finish executing
 2) the trace call of the stack, how many nodes did it visit before reaching a solution?
 3) did it manage to reach a correct result?
 4) the amount of memory the function/object required
"
)


(ns goal-state-size
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
     (holds R nil)
     (in key-B-E B)
   }
)

;helper functions to call to achieve results of tests
(defn test-one []
  "Move the agent from room A to room D"
  (time (ops-search move-A-D-all-unlocked '((in R D)) operations))
)

(defn test-two []
  "Move the agent from room A to room K"
  (time (ops-search move-A-D-all-unlocked '((in R K)) operations))
)

(defn test-three []
  "Move the agent from room A to room K and close the door behind him (D-K close)"
  (time (ops-search move-A-D-all-unlocked '((in R K) (opened D-K false)) operations))
)
