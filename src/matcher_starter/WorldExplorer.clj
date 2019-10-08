(ns world-explorer
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]

  )
)

(use 'clojure.tools.trace)

;; *ns* // check what namespace your currently in
;; (in-ns 'world-explorer) // this changes the namespace to this file
; ALT+SHIFT + L, loads file into REPL onside

;(time (ops-search intermediate-state-one '((in R E) (opened C-F false) (opened B-E false)) operations))


(comment
  --RULES--
  - A door has a key
  - A door has two connecting rooms
  )

(comment
  --KEYWORDS--
  - AGENT (agent ?agent)
  - ROOM (room ?room)
  - KEY (key ?key)
  - DOOR (door ?door)
  - HOLDABLE (holdable ?key)


  -OPENED (opened ?door true/false)
  -UNLOCKED (unlocked ?door true/false)
  -CONNECTS (connects ?door ?A ?B)                          ;(connects ?door A)(connects ?door A)
  -UNLOCKS (unlocks ?key ?door)
  -LOCKS (locks ?key ?door)
  -HOLDS "Agent holds key-a" (holds ?agent ?holdable)
  -IN (IN ?agent ?room), (IN ?key ?room)
  )

(comment
  --OPS--
  - OPEN DOOR
  - CLOSE DOOR
  - LOCK DOOR
  - UNLOCK DOOR
  - MOVE
  - PICKUP
  - DROP
  )

(def base-state-one
  "A basic world state where there are two rooms, an agent and an open door"
  '#{
     (agent R)
     (room A)
     (room D)
     (door A-D)
     (opened A-D true)
     (unlocked A-D true)
     ;(connects A-D A D)
     (connects A-D A)
     (connects A-D D)
     (in R A)
     }
  )

(def base-state-two
  "A basic state where the agent can open a door"
  '#{
     (agent R)
     (room A)
     (room D)
     (door A-D)
     (opened A-D false)
     (unlocked A-D true)
     (connects A-D A)
     (connects A-D D)
     (in R A)
     }
  )


(def base-state-three
  "A basic state where the agent can open a door, move, close the door, unlock and lock the door"
  '#{
     (agent R)
     (room A)
     (room D)
     (door A-D)
     (opened A-D false)
     (unlocked A-D false)
     (connects A-D A)
     (connects A-D D)
     (in R A)

     (key key)
     (holdable key)
     (unlocks key A-D)
     (holds R key)
     }
  )

(def base-state-four
  "A basic state where the agent can open a door, move, close the door, unlock and lock the door
  the agent can also pick up objects in the room"
  '#{
     (agent R)
     (room A)
     (room D)
     (door A-D)
     (opened A-D false)
     (unlocked A-D false)
     (connects A-D A)
     (connects A-D D)
     (in R A)

     (key key)
     (holdable key)
     (unlocks key A-D)
     (holds R nil)
     (in key A)
     }
  )

(def base-state-five
  "A basic state where the agent can open a door, move, close the door, unlock and lock the door
  the agent can also pick up objects in the room
  the agent can also drop the object"
  '#{
     (agent R)
     (room A)
     (room D)
     (door A-D)
     (opened A-D false)
     (unlocked A-D false)
     (connects A-D A)
     (connects A-D D)
     (in R A)

     (key key)
     (holdable key)
     (unlocks key A-D)
     (holds R nil)
     (in key A)
     }
  )

(def intermediate-state-one
  "A more advanced scenario"
  '#{
     (agent R)
     (room A)
     (room B)
     (room C)
     (room D)
     (room E)
     (room F)
     (door A-B)
     (door A-C)
     (door A-D)
     (door B-E)
     (door C-F)

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

     (in R D)

     (opened A-B true)
     (opened A-C true)
     (opened A-D true)
     (opened B-E true)
     (opened C-F true)

     (unlocked A-B true)
     (unlocked A-C true)
     (unlocked A-D true)
     (unlocked B-E true)
     (unlocked C-F true)

     }
  )

(def intermediate-state-two
  "A more advanced scenario"
  '#{
     (agent R)
     (room A)
     (room B)
     (room C)
     (room D)
     (room E)
     (room F)
     (door A-B)
     (door A-C)
     (door A-D)
     (door B-E)
     (door C-F)

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

     (in R D)

     (opened A-B true)
     (opened A-C true)
     (opened A-D true)
     (opened B-E false)
     (opened C-F true)

     (unlocked A-B true)
     (unlocked A-C true)
     (unlocked A-D true)
     (unlocked B-E false)
     (unlocked C-F true)

     (key key-B-E)
     (holdable key-B-E)
     (unlocks key-B-E B-E)
     (holds R nil)
     (in key-B-E B)

     }
  )

(def intermediate-state-three
  "A more advanced scenario"
  '#{
     (agent R)
     (room A)
     (room B)
     (room C)
     (room D)
     (room E)
     (room F)
     (door A-B)
     (door A-C)
     (door A-D)
     (door B-E)
     (door C-F)

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

     (in R D)

     (opened A-B false)
     (opened A-C false)
     (opened A-D false)
     (opened B-E false)
     (opened C-F false)

     (unlocked A-B true)
     (unlocked A-C true)
     (unlocked A-D true)
     (unlocked B-E false)
     (unlocked C-F true)

     (key key-B-E)
     (holdable key-B-E)
     (unlocks key-B-E B-E)
     (holds R nil)
     (in key-B-E B)

     }
  )

;(connects ?door ??_ ?room1 ??_)
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




