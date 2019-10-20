(ns tuple-size.seven
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

(def state-seven
  "basic state for agent to move from one room to next

  The agent is now holding 15 keys"
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

     (door A-B)
     (door A-C)
     (door A-D)
     (door C-D)
     (door C-E)
     (door D-E)
     (door D-J)
     (door D-F)
     (door B-F)
     (door D-G)
     (door F-G)
     (door E-H)
     (door G-H)
     (door G-I)
     (door I-J)
     (door H-J)

     (connects A-B A)
     (connects A-B B)

     (connects A-D A)
     (connects A-B D)

     (connects A-C A)
     (connects A-C C)

     (connects C-D C)
     (connects C-D D)

     (connects C-E C)
     (connects C-E E)

     (connects D-F D)
     (connects D-F F)

     (connects D-G D)
     (connects D-G G)

     (connects D-J D)
     (connects D-J J)

     (connects D-E D)
     (connects D-E E)

     (connects B-F B)
     (connects B-F F)

     (connects F-G F)
     (connects F-G G)

     (connects E-H E)
     (connects E-H H)

     (connects G-H G)
     (connects G-H H)

     (connects G-I G)
     (connects G-I I)

     (connects H-J H)
     (connects H-J J)

     (connects I-J I)
     (connects I-J J)


     (opened A-B true)
     (opened A-C true)
     (opened A-D true)
     (opened C-D true)
     (opened C-E true)
     (opened D-F true)
     (opened D-G true)
     (opened D-J true)
     (opened D-E true)
     (opened B-F true)
     (opened F-G true)
     (opened E-H true)
     (opened G-H true)
     (opened G-I true)
     (opened H-J true)
     (opened I-J true)

     (unlocked A-B true)
     (unlocked A-C true)
     (unlocked A-D true)
     (unlocked C-D true)
     (unlocked C-E true)
     (unlocked D-F true)
     (unlocked D-G true)
     (unlocked D-J true)
     (unlocked D-E true)
     (unlocked B-F true)
     (unlocked F-G true)
     (unlocked E-H true)
     (unlocked G-H true)
     (unlocked G-I true)
     (unlocked H-J true)
     (unlocked I-J true)

     (key key-A-B)
     (key key-A-C)
     (key key-A-D)
     (key key-C-D)
     (key key-C-E)
     (key key-D-E)
     (key key-D-J)
     (key key-D-F)
     (key key-B-F)
     (key key-D-G)
     (key key-F-G)
     (key key-E-H)
     (key key-G-H)
     (key key-G-I)
     (key key-I-J)
     (key key-H-J)

     (holdable key-A-B)
     (holdable key-A-C)
     (holdable key-A-D)
     (holdable key-C-D)
     (holdable key-C-E)
     (holdable key-D-E)
     (holdable key-D-J)
     (holdable key-D-F)
     (holdable key-B-F)
     (holdable key-D-G)
     (holdable key-F-G)
     (holdable key-E-H)
     (holdable key-G-H)
     (holdable key-G-I)
     (holdable key-I-J)
     (holdable key-H-J)

     (unlocks key-A-B A-B)
     (unlocks key-A-C A-C)
     (unlocks key-A-D A-D)
     (unlocks key-C-D C-D)
     (unlocks key-C-E C-E)
     (unlocks key-D-E D-E)
     (unlocks key-D-J D-J)
     (unlocks key-D-F D-F)
     (unlocks key-B-F B-F)
     (unlocks key-F-G F-G)
     (unlocks key-E-H E-H)
     (unlocks key-G-H G-H)
     (unlocks key-G-I G-I)
     (unlocks key-I-J I-J)
     (unlocks key-H-J H-J)

     (in key-A-B A)
     (in key-A-C A)
     (in key-A-D A)
     (in key-C-D A)
     (in key-C-E A)
     (in key-D-E A)
     (in key-D-J A)
     (in key-D-F A)
     (in key-B-F A)
     (in key-D-G A)
     (in key-F-G A)
     (in key-E-H A)
     (in key-G-H A)
     (in key-G-I A)
     (in key-I-J A)
     (in key-H-J A)

     (holds R key-A-B key-A-C key-A-D key-C-D key-C-E key-D-E key-D-J key-D-F key-B-F key-D-G
            key-F-G key-E-H key-G-H key-G-I key-I-J key-H-J)

     (in R A)
     }
  )

;---------------------------------
;---------------------------------
;--------------Making the agent hold 15 keys-------------------
;---------------------------------
;---------------------------------

(defn test-seven-move-to-f []
  "Elapsed time: 104704.5978 msecs"
  (time (ops-search state-seven '((in R F)) operations :debug true))
  )

(defn test-seven-move-to-g []
  "Elapsed time = 103737.5413 msecs"
  (time (ops-search state-seven '((in R G)) operations :debug true))
  )

(defn test-seven-move-to-h []
  "Elapsed time = 612853.4319 msecs"
  (time (ops-search state-seven '((in R H)) operations :debug true))
  )

(defn test-seven-move-to-j []
  "Elapsed time = 107439.1592 msecs"
  (time (ops-search state-seven '((in R J)) operations :debug true))
  )