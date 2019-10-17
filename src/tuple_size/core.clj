(ns tuple-size.core
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

     (in R A)
     }
  )

(def state-two
  "basic state for agent to move from one room to next

  Replication of state one but with keys for all the doors"
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
     (in key-F-G A)
     (in key-E-H A)
     (in key-G-H A)
     (in key-G-I A)
     (in key-I-J A)
     (in key-H-J A)

     (in R A)
     }
  )

(def state-three
  "basic state for agent to move from one room to next

  The agent is now holding one key"
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

     (holds R key-A-B)

     (in R A)
     }
  )

(def state-four
  "basic state for agent to move from one room to next

  The agent is now holding three keys"
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

     (holds R key-A-B key-A-C key-A-D)

     (in R A)
     }
  )

(def state-five
  "basic state for agent to move from one room to next

  The agent is now holding six keys"
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

     (holds R key-A-B key-A-C key-A-D key-C-D key-C-E key-D-E)

     (in R A)
     }
  )

(def state-six
  "basic state for agent to move from one room to next

  The agent is now holding 10 keys"
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

     (holds R key-A-B key-A-C key-A-D key-C-D key-C-E key-D-E key-D-J key-D-F key-B-F key-D-G)

     (in R A)
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

(def state-eight
  "basic state for agent to move from one room to next

  The agent is now holding 15 keys, an additional 50 keys have been defined"
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

     (key extra1)
     (key extra2)
     (key extra3)
     (key extra4)
     (key extra5)
     (key extra6)
     (key extra7)
     (key extra8)
     (key extra9)
     (key extra10)
     (key extra11)
     (key extra12)
     (key extra13)
     (key extra14)
     (key extra15)
     (key extra16)
     (key extra17)
     (key extra18)
     (key extra19)
     (key extra20)
     (key extra21)
     (key extra22)
     (key extra23)
     (key extra24)
     (key extra25)
     (key extra26)
     (key extra27)
     (key extra28)
     (key extra29)
     (key extra30)
     (key extra31)
     (key extra32)
     (key extra33)
     (key extra34)
     (key extra35)
     (key extra36)
     (key extra37)
     (key extra38)
     (key extra39)
     (key extra40)
     (key extra41)
     (key extra42)
     (key extra43)
     (key extra44)
     (key extra45)
     (key extra46)
     (key extra47)
     (key extra48)
     (key extra49)
     (key extra50)

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
     (holdable extra11)
     (holdable extra12)
     (holdable extra13)
     (holdable extra14)
     (holdable extra15)
     (holdable extra16)
     (holdable extra17)
     (holdable extra18)
     (holdable extra19)
     (holdable extra20)
     (holdable extra21)
     (holdable extra22)
     (holdable extra23)
     (holdable extra24)
     (holdable extra25)
     (holdable extra26)
     (holdable extra27)
     (holdable extra28)
     (holdable extra29)
     (holdable extra30)
     (holdable extra31)
     (holdable extra32)
     (holdable extra33)
     (holdable extra34)
     (holdable extra35)
     (holdable extra36)
     (holdable extra37)
     (holdable extra38)
     (holdable extra39)
     (holdable extra40)
     (holdable extra41)
     (holdable extra42)
     (holdable extra43)
     (holdable extra44)
     (holdable extra45)
     (holdable extra46)
     (holdable extra47)
     (holdable extra48)
     (holdable extra49)
     (holdable extra50)

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

     (in extra1 A)
     (in extra2 A)
     (in extra3 A)
     (in extra4 A)
     (in extra5 A)
     (in extra6 A)
     (in extra7 A)
     (in extra8 A)
     (in extra9 A)
     (in extra10 A)
     (in extra11 A)
     (in extra12 A)
     (in extra13 A)
     (in extra14 A)
     (in extra15 A)
     (in extra16 A)
     (in extra17 A)
     (in extra18 A)
     (in extra19 A)
     (in extra20 A)
     (in extra21 A)
     (in extra22 A)
     (in extra23 A)
     (in extra24 A)
     (in extra25 A)
     (in extra26 A)
     (in extra27 A)
     (in extra28 A)
     (in extra29 A)
     (in extra30 A)
     (in extra31 A)
     (in extra32 A)
     (in extra33 A)
     (in extra34 A)
     (in extra35 A)
     (in extra36 A)
     (in extra37 A)
     (in extra38 A)
     (in extra39 A)
     (in extra40 A)
     (in extra41 A)
     (in extra42 A)
     (in extra43 A)
     (in extra44 A)
     (in extra45 A)
     (in extra46 A)
     (in extra47 A)
     (in extra48 A)
     (in extra49 A)
     (in extra50 A)

     (holds R key-A-B key-A-C key-A-D key-C-D key-C-E key-D-E key-D-J key-D-F key-B-F key-D-G
            key-F-G key-E-H key-G-H key-G-I key-I-J key-H-J)

     (in R A)
     }
  )

(def state-nine
     "basic state for agent to move from one room to next

     The agent is now holding 35 keys"
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

        (key extra1)
        (key extra2)
        (key extra3)
        (key extra4)
        (key extra5)
        (key extra6)
        (key extra7)
        (key extra8)
        (key extra9)
        (key extra10)
        (key extra11)
        (key extra12)
        (key extra13)
        (key extra14)
        (key extra15)
        (key extra16)
        (key extra17)
        (key extra18)
        (key extra19)
        (key extra20)
        (key extra21)
        (key extra22)
        (key extra23)
        (key extra24)
        (key extra25)
        (key extra26)
        (key extra27)
        (key extra28)
        (key extra29)
        (key extra30)
        (key extra31)
        (key extra32)
        (key extra33)
        (key extra34)
        (key extra35)
        (key extra36)
        (key extra37)
        (key extra38)
        (key extra39)
        (key extra40)
        (key extra41)
        (key extra42)
        (key extra43)
        (key extra44)
        (key extra45)
        (key extra46)
        (key extra47)
        (key extra48)
        (key extra49)
        (key extra50)

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
        (holdable extra11)
        (holdable extra12)
        (holdable extra13)
        (holdable extra14)
        (holdable extra15)
        (holdable extra16)
        (holdable extra17)
        (holdable extra18)
        (holdable extra19)
        (holdable extra20)
        (holdable extra21)
        (holdable extra22)
        (holdable extra23)
        (holdable extra24)
        (holdable extra25)
        (holdable extra26)
        (holdable extra27)
        (holdable extra28)
        (holdable extra29)
        (holdable extra30)
        (holdable extra31)
        (holdable extra32)
        (holdable extra33)
        (holdable extra34)
        (holdable extra35)
        (holdable extra36)
        (holdable extra37)
        (holdable extra38)
        (holdable extra39)
        (holdable extra40)
        (holdable extra41)
        (holdable extra42)
        (holdable extra43)
        (holdable extra44)
        (holdable extra45)
        (holdable extra46)
        (holdable extra47)
        (holdable extra48)
        (holdable extra49)
        (holdable extra50)

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

        (in extra1 A)
        (in extra2 A)
        (in extra3 A)
        (in extra4 A)
        (in extra5 A)
        (in extra6 A)
        (in extra7 A)
        (in extra8 A)
        (in extra9 A)
        (in extra10 A)
        (in extra11 A)
        (in extra12 A)
        (in extra13 A)
        (in extra14 A)
        (in extra15 A)
        (in extra16 A)
        (in extra17 A)
        (in extra18 A)
        (in extra19 A)
        (in extra20 A)
        (in extra21 A)
        (in extra22 A)
        (in extra23 A)
        (in extra24 A)
        (in extra25 A)
        (in extra26 A)
        (in extra27 A)
        (in extra28 A)
        (in extra29 A)
        (in extra30 A)
        (in extra31 A)
        (in extra32 A)
        (in extra33 A)
        (in extra34 A)
        (in extra35 A)
        (in extra36 A)
        (in extra37 A)
        (in extra38 A)
        (in extra39 A)
        (in extra40 A)
        (in extra41 A)
        (in extra42 A)
        (in extra43 A)
        (in extra44 A)
        (in extra45 A)
        (in extra46 A)
        (in extra47 A)
        (in extra48 A)
        (in extra49 A)
        (in extra50 A)

        (holds R key-A-B key-A-C key-A-D key-C-D key-C-E key-D-E key-D-J key-D-F key-B-F key-D-G
               key-F-G key-E-H key-G-H key-G-I key-I-J key-H-J

               extra1 extra2 extra3 extra4 extra5 extra6 extra7 extra8 extra9 extra10
               extra11 extra12 extra13 extra14 extra15 extra16 extra17 extra18 extra19 extra20
               )

        (in R A)
        }
     )

(def state-ten
  "basic state for agent to move from one room to next

  The agent is now holding 65 keys"
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

     (key extra1)
     (key extra2)
     (key extra3)
     (key extra4)
     (key extra5)
     (key extra6)
     (key extra7)
     (key extra8)
     (key extra9)
     (key extra10)
     (key extra11)
     (key extra12)
     (key extra13)
     (key extra14)
     (key extra15)
     (key extra16)
     (key extra17)
     (key extra18)
     (key extra19)
     (key extra20)
     (key extra21)
     (key extra22)
     (key extra23)
     (key extra24)
     (key extra25)
     (key extra26)
     (key extra27)
     (key extra28)
     (key extra29)
     (key extra30)
     (key extra31)
     (key extra32)
     (key extra33)
     (key extra34)
     (key extra35)
     (key extra36)
     (key extra37)
     (key extra38)
     (key extra39)
     (key extra40)
     (key extra41)
     (key extra42)
     (key extra43)
     (key extra44)
     (key extra45)
     (key extra46)
     (key extra47)
     (key extra48)
     (key extra49)
     (key extra50)

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
     (holdable extra11)
     (holdable extra12)
     (holdable extra13)
     (holdable extra14)
     (holdable extra15)
     (holdable extra16)
     (holdable extra17)
     (holdable extra18)
     (holdable extra19)
     (holdable extra20)
     (holdable extra21)
     (holdable extra22)
     (holdable extra23)
     (holdable extra24)
     (holdable extra25)
     (holdable extra26)
     (holdable extra27)
     (holdable extra28)
     (holdable extra29)
     (holdable extra30)
     (holdable extra31)
     (holdable extra32)
     (holdable extra33)
     (holdable extra34)
     (holdable extra35)
     (holdable extra36)
     (holdable extra37)
     (holdable extra38)
     (holdable extra39)
     (holdable extra40)
     (holdable extra41)
     (holdable extra42)
     (holdable extra43)
     (holdable extra44)
     (holdable extra45)
     (holdable extra46)
     (holdable extra47)
     (holdable extra48)
     (holdable extra49)
     (holdable extra50)

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

     (in extra1 A)
     (in extra2 A)
     (in extra3 A)
     (in extra4 A)
     (in extra5 A)
     (in extra6 A)
     (in extra7 A)
     (in extra8 A)
     (in extra9 A)
     (in extra10 A)
     (in extra11 A)
     (in extra12 A)
     (in extra13 A)
     (in extra14 A)
     (in extra15 A)
     (in extra16 A)
     (in extra17 A)
     (in extra18 A)
     (in extra19 A)
     (in extra20 A)
     (in extra21 A)
     (in extra22 A)
     (in extra23 A)
     (in extra24 A)
     (in extra25 A)
     (in extra26 A)
     (in extra27 A)
     (in extra28 A)
     (in extra29 A)
     (in extra30 A)
     (in extra31 A)
     (in extra32 A)
     (in extra33 A)
     (in extra34 A)
     (in extra35 A)
     (in extra36 A)
     (in extra37 A)
     (in extra38 A)
     (in extra39 A)
     (in extra40 A)
     (in extra41 A)
     (in extra42 A)
     (in extra43 A)
     (in extra44 A)
     (in extra45 A)
     (in extra46 A)
     (in extra47 A)
     (in extra48 A)
     (in extra49 A)
     (in extra50 A)

     (holds R key-A-B key-A-C key-A-D key-C-D key-C-E key-D-E key-D-J key-D-F key-B-F key-D-G
            key-F-G key-E-H key-G-H key-G-I key-I-J key-H-J

            extra1 extra2 extra3 extra4 extra5 extra6 extra7 extra8 extra9 extra10
            extra11 extra12 extra13 extra14 extra15 extra16 extra17 extra18 extra19 extra20
            extra21 extra22 extra23 extra24 extra25 extra26 extra27 extra28 extra29 extra30
            extra31 extra32 extra33 extra34 extra35 extra36 extra37 extra38 extra39 extra40
            extra41 extra42 extra43 extra44 extra45 extra46 extra47 extra48 extra49 extra50
            )

     (in R A)
     }
  )

(def state-eleven
  "basic state for agent to move from one room to next

  The agent is now holding 65 keys"
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

     (key extra1)
     (key extra2)
     (key extra3)
     (key extra4)
     (key extra5)
     (key extra6)
     (key extra7)
     (key extra8)
     (key extra9)
     (key extra10)
     (key extra11)
     (key extra12)
     (key extra13)
     (key extra14)
     (key extra15)
     (key extra16)
     (key extra17)
     (key extra18)
     (key extra19)
     (key extra20)
     (key extra21)
     (key extra22)
     (key extra23)
     (key extra24)
     (key extra25)
     (key extra26)
     (key extra27)
     (key extra28)
     (key extra29)
     (key extra30)
     (key extra31)
     (key extra32)
     (key extra33)
     (key extra34)
     (key extra35)
     (key extra36)
     (key extra37)
     (key extra38)
     (key extra39)
     (key extra40)
     (key extra41)
     (key extra42)
     (key extra43)
     (key extra44)
     (key extra45)
     (key extra46)
     (key extra47)
     (key extra48)
     (key extra49)
     (key extra50)

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
     (holdable extra11)
     (holdable extra12)
     (holdable extra13)
     (holdable extra14)
     (holdable extra15)
     (holdable extra16)
     (holdable extra17)
     (holdable extra18)
     (holdable extra19)
     (holdable extra20)
     (holdable extra21)
     (holdable extra22)
     (holdable extra23)
     (holdable extra24)
     (holdable extra25)
     (holdable extra26)
     (holdable extra27)
     (holdable extra28)
     (holdable extra29)
     (holdable extra30)
     (holdable extra31)
     (holdable extra32)
     (holdable extra33)
     (holdable extra34)
     (holdable extra35)
     (holdable extra36)
     (holdable extra37)
     (holdable extra38)
     (holdable extra39)
     (holdable extra40)
     (holdable extra41)
     (holdable extra42)
     (holdable extra43)
     (holdable extra44)
     (holdable extra45)
     (holdable extra46)
     (holdable extra47)
     (holdable extra48)
     (holdable extra49)
     (holdable extra50)

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

     (in extra1 A)
     (in extra2 A)
     (in extra3 A)
     (in extra4 A)
     (in extra5 A)
     (in extra6 A)
     (in extra7 A)
     (in extra8 A)
     (in extra9 A)
     (in extra10 A)
     (in extra11 A)
     (in extra12 A)
     (in extra13 A)
     (in extra14 A)
     (in extra15 A)
     (in extra16 A)
     (in extra17 A)
     (in extra18 A)
     (in extra19 A)
     (in extra20 A)
     (in extra21 A)
     (in extra22 A)
     (in extra23 A)
     (in extra24 A)
     (in extra25 A)
     (in extra26 A)
     (in extra27 A)
     (in extra28 A)
     (in extra29 A)
     (in extra30 A)
     (in extra31 A)
     (in extra32 A)
     (in extra33 A)
     (in extra34 A)
     (in extra35 A)
     (in extra36 A)
     (in extra37 A)
     (in extra38 A)
     (in extra39 A)
     (in extra40 A)
     (in extra41 A)
     (in extra42 A)
     (in extra43 A)
     (in extra44 A)
     (in extra45 A)
     (in extra46 A)
     (in extra47 A)
     (in extra48 A)
     (in extra49 A)
     (in extra50 A)

     (holds R key-A-B key-A-C key-A-D key-C-D key-C-E key-D-E key-D-J key-D-F key-B-F key-D-G
            key-F-G key-E-H key-G-H key-G-I key-I-J key-H-J

            extra1 extra2 extra3 extra4 extra5 extra6 extra7 extra8 extra9 extra10
            extra11 extra12 extra13 extra14 extra15 extra16 extra17 extra18 extra19 extra20
            extra21 extra22 extra23 extra24 extra25 extra26 extra27 extra28 extra29 extra30
            extra31 extra32 extra33 extra34 extra35 extra36 extra37 extra38 extra39 extra40
            extra41 extra42 extra43 extra44 extra45 extra46 extra47 extra48 extra49 extra50
            )

     (in R A)
     }
  )

;----------------------------------
;----------------------------------
;----------------Tests Base, no keys at all------------------
;----------------------------------
;----------------------------------
(defn test-one-move-to-f []
  "Elapsed time = 35046.1226 msecs"
  (time (ops-search state-one '((in R F)) operations :debug true))
  )

(defn test-one-move-to-g []
  "Elapsed time = 32687.3366 msecs"
  (time (ops-search state-one '((in R G)) operations :debug true))
  )

(defn test-one-move-to-h []
  "Elapsed time = 612853.4319 msecs"
  (time (ops-search state-one '((in R H)) operations :debug true))
  )

(defn test-one-move-to-j []
  "Elapsed time = 33119.7027 msecs"
  (time (ops-search state-one '((in R J)) operations :debug true))
  )

;---------------------------------
;---------------------------------
;--------------Adding keys for all of the rooms-------------------
;---------------------------------
;---------------------------------

(defn test-two-move-to-f []
  "Elapsed time: 55081.366 msecs"
  (time (ops-search state-two '((in R F)) operations :debug true))
  )

(defn test-two-move-to-g []
  "Elapsed time = 54169.3132 msecs"
  (time (ops-search state-two '((in R G)) operations :debug true))
  )

(defn test-two-move-to-h []
  "Elapsed time = 612853.4319 msecs"
  (time (ops-search state-two '((in R H)) operations :debug true))
  )

(defn test-two-move-to-j []
  "Elapsed time = 53801.971 msecs"
  (time (ops-search state-two '((in R J)) operations :debug true))
  )

;---------------------------------
;---------------------------------
;--------------Making the agent hold a key-------------------
;---------------------------------
;---------------------------------

(defn test-three-move-to-f []
  "Elapsed time: 50253.8745 msecs"
  (time (ops-search state-three '((in R F)) operations :debug true))
  )

(defn test-three-move-to-g []
  "Elapsed time = 51973.6485 msecs"
  (time (ops-search state-three '((in R G)) operations :debug true))
  )

(defn test-three-move-to-h []
  "Elapsed time = 612853.4319 msecs"
  (time (ops-search state-three '((in R H)) operations :debug true))
  )

(defn test-three-move-to-j []
  "Elapsed time = 81786.3102 msecs"
  (time (ops-search state-three '((in R J)) operations :debug true))
  )

;---------------------------------
;---------------------------------
;--------------Making the agent hold 3 keys-------------------
;---------------------------------
;---------------------------------

(defn test-four-move-to-f []
  "Elapsed time: 50253.8745 msecs"
  (time (ops-search state-four '((in R F)) operations :debug true))
  )

(defn test-four-move-to-g []
  "Elapsed time = 51973.6485 msecs"
  (time (ops-search state-four '((in R G)) operations :debug true))
  )

(defn test-four-move-to-h []
  "Elapsed time = 612853.4319 msecs"
  (time (ops-search state-four '((in R H)) operations :debug true))
  )

(defn test-four-move-to-j []
  "Elapsed time = 50254.3779 msecs"
  (time (ops-search state-four '((in R J)) operations :debug true))
  )

;---------------------------------
;---------------------------------
;--------------Making the agent hold 6 keys-------------------
;---------------------------------
;---------------------------------

(defn test-five-move-to-f []
  "Elapsed time: 50253.8745 msecs"
  (time (ops-search state-five '((in R F)) operations :debug true))
  )

(defn test-five-move-to-g []
  "Elapsed time = 51973.6485 msecs"
  (time (ops-search state-five '((in R G)) operations :debug true))
  )

(defn test-five-move-to-h []
  "Elapsed time = 612853.4319 msecs"
  (time (ops-search state-five '((in R H)) operations :debug true))
  )

(defn test-five-move-to-j []
  "Elapsed time = 50254.3779 msecs"
  (time (ops-search state-five '((in R J)) operations :debug true))
  )

;---------------------------------
;---------------------------------
;--------------Making the agent hold 10 keys-------------------
;---------------------------------
;---------------------------------

(defn test-six-move-to-f []
  "Elapsed time: 50253.8745 msecs"
  (time (ops-search state-six '((in R F)) operations :debug true))
  )

(defn test-six-move-to-g []
  "Elapsed time = 51973.6485 msecs"
  (time (ops-search state-six '((in R G)) operations :debug true))
  )

(defn test-six-move-to-h []
  "Elapsed time = 612853.4319 msecs"
  (time (ops-search state-six '((in R H)) operations :debug true))
  )

(defn test-six-move-to-j []
  "Elapsed time = 50254.3779 msecs"
  (time (ops-search state-six '((in R J)) operations :debug true))
  )

;---------------------------------
;---------------------------------
;--------------Making the agent hold 15 keys-------------------
;---------------------------------
;---------------------------------

(defn test-seven-move-to-f []
  "Elapsed time: 98290.4615 msecs"
  (time (ops-search state-seven '((in R F)) operations :debug true))
  )

(defn test-seven-move-to-g []
  "Elapsed time = 51973.6485 msecs"
  (time (ops-search state-seven '((in R G)) operations :debug true))
  )

(defn test-seven-move-to-h []
  "Elapsed time = 612853.4319 msecs"
  (time (ops-search state-seven '((in R H)) operations :debug true))
  )

(defn test-seven-move-to-j []
  "Elapsed time = 50254.3779 msecs"
  (time (ops-search state-seven '((in R J)) operations :debug true))
  )

;---------------------------------
;---------------------------------
;--------------Making the agent hold 15 keys and defining 50 extra keys-------------------
;---------------------------------
;---------------------------------

(defn test-eight-move-to-f []
  "Elapsed time: 98290.4615 msecs"
  (time (ops-search state-eight '((in R F)) operations :debug true))
  )

(defn test-eight-move-to-g []
  "Elapsed time = 51973.6485 msecs"
  (time (ops-search state-eight '((in R G)) operations :debug true))
  )

(defn test-eight-move-to-h []
  "Elapsed time = 612853.4319 msecs"
  (time (ops-search state-eight '((in R H)) operations :debug true))
  )

(defn test-eight-move-to-j []
  "Elapsed time = 50254.3779 msecs"
  (time (ops-search state-eight '((in R J)) operations :debug true))
  )

;---------------------------------
;---------------------------------
;--------------The agent holds 35 keys-------------------
;---------------------------------
;---------------------------------

(defn test-nine-move-to-f []
  "Elapsed time: 98290.4615 msecs"
  (time (ops-search state-nine '((in R F)) operations :debug true))
  )

(defn test-nine-move-to-g []
  "Elapsed time = 51973.6485 msecs"
  (time (ops-search state-nine '((in R G)) operations :debug true))
  )

(defn test-nine-move-to-h []
  "Elapsed time = 612853.4319 msecs"
  (time (ops-search state-nine '((in R H)) operations :debug true))
  )

(defn test-nine-move-to-j []
  "Elapsed time = 50254.3779 msecs"
  (time (ops-search state-nine '((in R J)) operations :debug true))
  )

;---------------------------------
;---------------------------------
;--------------The agent holds 65 keys-------------------
;---------------------------------
;---------------------------------

(defn test-ten-move-to-f []
  "Elapsed time: 98290.4615 msecs"
  (time (ops-search state-ten '((in R F)) operations :debug true))
  )

(defn test-ten-move-to-g []
  "Elapsed time = 450450.1081 msecs"
  (time (ops-search state-ten '((in R G)) operations :debug true))
  )

(defn test-ten-move-to-h []
  "Elapsed time = 612853.4319 msecs"
  (time (ops-search state-ten '((in R H)) operations :debug true))
  )

(defn test-ten-move-to-j []
  "Elapsed time = 50254.3779 msecs"
  (time (ops-search state-ten '((in R J)) operations :debug true))
  )