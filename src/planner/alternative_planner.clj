(ns planner.alternative-planner
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            [clojure.set :refer :all]
            [planner.planner :refer :all]
            )
  )

;(planner move-A-D-all-unlocked '(in R D) operations)
;(planner move-A-D-all-unlocked '(opened A-B true) operations)

(def operations
  "A map of operations that the agent can perform in the world

  The open operator will let the agent get from A-H or any other room quickly.
  However, it does not work if the goal is  (opened ?door true). This is because (in ?agent ?room1)
  is in the :when and not the :pre, so planner doesn't have a path to follow.

  Placing the (in ?agent ?room1) in the post will cause issues with the move operator.
  This is because it may assume that the agent is aiming to get to the wrong side of the door
  and get stuck on an infinite path trying to reach the agent, never realising it's a dead end."
  '{
    move
    {:name     move-agent
     :achieves (in ?agent ?room2)
     :when     ((agent ?agent)
                 (room ?room1 ?v-room1)
                 (room ?room2 unvisited)
                 (door ?door)
                 (connects ?door ?room1)
                 (connects ?door ?room2)
                 (:guard (not= (? room1) (? room2)))
                 )
     :post     ((in ?agent ?room1)
                 (unlocked ?door true)
                 (opened ?door true)
                 )
     :pre      ()
     :add      ((in ?agent ?room2)
                 (room ?room1 visited)
                 (room ?room2 visited)
                 )
     :del      ((in ?agent ?room1)
                 (room ?room1 unvisited)
                 (room ?room2 unvisited)
                 )
     :txt      (agent ?agent has moved from ?room1 to ?room2)
     }
    open
    {:name     open-door
     :achieves (opened ?door true)
     :when     ((agent ?agent)
                 (room ?room1 ?vis)
                 (door ?door)
                 (opened ?door false)
                 (unlocked ?door true)
                 (connects ?door ?room1)
                 (in ?agent ?room1))
     :post     ()
     :pre      ()
     :add      ((opened ?door true))
     :del      ((opened ?door false))
     :txt      (?agent has opened ?door)
     }
    }
  )

;test one - (time (ops-search move-A-D-all-unlocked '((in R K)) operations))
;test two - (time (ops-search move-A-D-all-unlocked '((in R K) (opened )) operations))
(def move-A-D-all-unlocked
  "A more advanced scenario"
  '#{
     ;define agentLL
     (agent R)
     ;define rooms
     (room A unvisited)
     (room B unvisited)
     (room C unvisited)
     (room D unvisited)
     (room E unvisited)
     (room F unvisited)
     (room G unvisited)
     (room H unvisited)
     (room I unvisited)
     (room J unvisited)
     (room K unvisited)
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
     ;(holds R key-A-D)

     ;test 5
     (holds R nil)
     }
  )

(def move-A-D-all-closed
  "A more advanced scenario"
  '#{
     ;define agentLL
     (agent R)
     ;define rooms
     (room A unvisited)
     (room B unvisited)
     (room C unvisited)
     (room D unvisited)
     (room E unvisited)
     (room F unvisited)
     (room G unvisited)
     (room H unvisited)
     (room I unvisited)
     (room J unvisited)
     (room K unvisited)
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
     (key key-B-E)
     (holdable key-B-E)
     (unlocks key-B-E B-E)
     (in key-B-E B)

     (key key-A-D)
     (holdable key-A-D)
     (unlocks key-A-D A-D)
     (in key-A-D A)

     ;test 1-4
     ;(holds R key-A-D)

     ;test 5
     (holds R nil)
     }
  )


;-------------------------------------------------
;-------------------------------------------------
;-----------------------Tests--------------------------
;-------------------------------------------------
;-------------------------------------------------

(defn move-to-b []
  "The agent moves from room a to room b"
  (planner move-A-D-all-unlocked '(in R B) operations)
  )

(defn move-to-c []
  "The agent moves from room a to room c"
  (planner move-A-D-all-unlocked '(in R C) operations)
  )

(defn move-to-f []
  "The agent moves from room a to room f"
  (planner move-A-D-all-unlocked '(in R F) operations)
  )

(defn move-to-j []
  "The agent moves from room a to room j"
  (planner move-A-D-all-unlocked '(in R J) operations)
  )