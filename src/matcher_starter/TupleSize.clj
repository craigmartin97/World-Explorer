(ns tuple-size
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

(def state-one
  "basic state for agent to move from one room to next

  Elapsed time = 3376.4839 msecs"
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

(def state-two
  "basic state for agent to move from one room to next
  In this state there is a key for each door and the agent holds one key

  Elapsed time = 14285.742 msecs"
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

     (key key-A-B)
     (key key-B-C)
     (key key-C-D)
     (key key-D-E)

     (holdable key-A-B)
     (holdable key-B-C)
     (holdable key-C-D)
     (holdable key-D-E)

     (unlocks key-A-B A-B)
     (unlocks key-B-C B-C)
     (unlocks key-C-D C-D)
     (unlocks key-D-E D-E)

     (holds R key-A-B)
     }
  )

(def state-three
  "basic state for agent to move from one room to next
  The agent is now holding three keys

  Elapsed time = 20108.4387 msecs"
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

     (key key-A-B)
     (key key-B-C)
     (key key-C-D)
     (key key-D-E)

     (holdable key-A-B)
     (holdable key-B-C)
     (holdable key-C-D)
     (holdable key-D-E)

     (unlocks key-A-B A-B)
     (unlocks key-B-C B-C)
     (unlocks key-C-D C-D)
     (unlocks key-D-E D-E)

     (holds R key-A-B key-B-C key-C-D)
     }
  )

(def state-four
  "basic state for agent to move from one room to next
  Created an additional ten keys that are holdable

  Elapsed time = 41743.5006 msecs"
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

     (key key-A-B)
     (key key-B-C)
     (key key-C-D)
     (key key-D-E)
     (key extra1)
     (key extra2)
     (key extra3)
     (key extra4)
     (key extra5)
     (key extra6)
     (key extra17)
     (key extra18)
     (key extra19)
     (key extra110)

     (holdable key-A-B)
     (holdable key-B-C)
     (holdable key-C-D)
     (holdable key-D-E)
     (holdable extra1)
     (holdable extra2)
     (holdable extra3)
     (holdable extra4)
     (holdable extra5)
     (holdable extra6)
     (holdable extra7)
     (holdable extra8)
     (holdable extra9)
     (holdable extra10)


     (unlocks key-A-B A-B)
     (unlocks key-B-C B-C)
     (unlocks key-C-D C-D)
     (unlocks key-D-E D-E)

     (holds R key-A-B key-B-C key-C-D key-D-E)
     }
  )

(def state-five
  "basic state for agent to move from one room to next
  The agent now holds an extra 5 keys

  Elapsed time = Elapsed time: 187710.4359 msecs"
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

     (key key-A-B)
     (key key-B-C)
     (key key-C-D)
     (key key-D-E)
     (key extra1)
     (key extra2)
     (key extra3)
     (key extra4)
     (key extra5)
     (key extra6)
     (key extra17)
     (key extra18)
     (key extra19)
     (key extra110)

     (holdable key-A-B)
     (holdable key-B-C)
     (holdable key-C-D)
     (holdable key-D-E)
     (holdable extra1)
     (holdable extra2)
     (holdable extra3)
     (holdable extra4)
     (holdable extra5)
     (holdable extra6)
     (holdable extra7)
     (holdable extra8)
     (holdable extra9)
     (holdable extra10)


     (unlocks key-A-B A-B)
     (unlocks key-B-C B-C)
     (unlocks key-C-D C-D)
     (unlocks key-D-E D-E)

     (holds R key-A-B key-B-C key-C-D key-D-E extra1 extra2 extra3 extra4 extra5)
     }
  )


(def state-six
  "basic state for agent to move from one room to next
  The agent now holds an extra 5 keys"
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

     (key key-A-B)
     (key key-B-C)
     (key key-C-D)
     (key key-D-E)
     (key extra1)
     (key extra2)
     (key extra3)
     (key extra4)
     (key extra5)
     (key extra6)
     (key extra17)
     (key extra18)
     (key extra19)
     (key extra110)

     (holdable key-A-B)
     (holdable key-B-C)
     (holdable key-C-D)
     (holdable key-D-E)
     (holdable extra1)
     (holdable extra2)
     (holdable extra3)
     (holdable extra4)
     (holdable extra5)
     (holdable extra6)
     (holdable extra7)
     (holdable extra8)
     (holdable extra9)
     (holdable extra10)


     (unlocks key-A-B A-B)
     (unlocks key-B-C B-C)
     (unlocks key-C-D C-D)
     (unlocks key-D-E D-E)

     (holds R key-A-B key-B-C key-C-D key-D-E extra1 extra2 extra3 extra4
            extra5 extra6 extra7 extra8 extra9 extra10)
     }
  )

;-------------------------------------------------
;-------------------------------------------------
;----------------------Tests---------------------------
;-------------------------------------------------
;-------------------------------------------------

(defn test-one []
  (time (ops-search state-one '((in R D)) operations :debug true))

  )

(defn test-two []
  (time (ops-search state-two '((in R D)) operations :debug true))

  )

(defn test-three []
  (time (ops-search state-three '((in R D)) operations :debug true))

  )

(defn test-four []
  (time (ops-search state-four '((in R D)) operations :debug true))

  )

(defn test-five []
  (time (ops-search state-five '((in R D)) operations :debug true))

  )

(defn test-six []
  (time (ops-search state-six '((in R D)) operations :debug true))

  )