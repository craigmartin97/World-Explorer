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

(def base-state-fourteen
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
     })

(def base-state-sixteen
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

     (chest Z)
     (in Z A)
     })

(def base-state-eighteen
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

     (key key2)
     (holdable key2)
     (unlocks key2 A-D)
     (in key2 D)
     })

(def base-state-twenty
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

     (room E)
     (door D-E)
     (opened D-E true)
     (unlocked D-E true)
     (connects D-E D)
     (connects D-E E)
     })

(def base-state-twentytwo
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

     (room E)
     (door D-E)
     (opened D-E true)
     (unlocked D-E true)

     (connects D-E D)
     (connects D-E E)
     (chest Z)
     (in Z A)
     })

(def base-state-twentyfour
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

     (room E)
     (door D-E)
     (opened D-E false)
     (unlocked D-E false)

     (connects D-E D)
     (connects D-E E)
     (key key2)
     (holdable key2)
     (unlocks key2 D-E)
     (in key2 D)
     })

(def base-state-twentysix
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

     (room E)
     (door D-E)
     (opened D-E true)
     (unlocked D-E true)

     (connects D-E D)
     (connects D-E E)
     (chest Z)
     (in Z A)

     (chestkey key3)
     (holdable key3)
     (unlocks key3 Z)
     (in key3 E)
     })

(def base-state-twentyeight
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

     (room E)
     (door D-E)
     (opened D-E false)
     (unlocked D-E false)

     (connects D-E D)
     (connects D-E E)
     (key key2)
     (holdable key2)
     (unlocks key2 D-E)
     (in key2 D)

     (room F)
     (door E-F)
     (opened E-F true)
     (unlocked E-F true)
     })

(def base-state-thirty
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

     (room E)
     (door D-E)
     (opened D-E true)
     (unlocked D-E true)

     (connects D-E D)
     (connects D-E E)
     (chest Z)
     (in Z A)

     (chestkey key3)
     (holdable key3)
     (unlocks key3 Z)
     (in key3 E)

     (key key2)
     (holdable key2)
     (unlocks key2 D-E)
     (in key2 D)
     })

(def base-state-thirtytwo
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

     (room E)
     (door D-E)
     (opened D-E false)
     (unlocked D-E false)

     (connects D-E D)
     (connects D-E E)
     (key key2)
     (holdable key2)
     (unlocks key2 D-E)
     (in key2 D)

     (room F)
     (door E-F)
     (opened E-F false)
     (unlocked E-F false)

     (key key5)
     (holdable key5)
     (unlocks key5 E-F)
     (in key5 E)
     })

(def base-state-thirtyfour
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

     (room E)
     (door D-E)
     (opened D-E false)
     (unlocked D-E false)

     (connects D-E D)
     (connects D-E E)
     (key key2)
     (holdable key2)
     (unlocks key2 D-E)
     (in key2 D)

     (room F)
     (door E-F)
     (opened E-F false)
     (unlocked E-F false)

     (key key5)
     (holdable key5)
     (unlocks key5 E-F)
     (in key5 E)

     (chest Y)
     (in Y E)
     })

(def base-state-thirtysix
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

     (room E)
     (door D-E)
     (opened D-E false)
     (unlocked D-E false)

     (connects D-E D)
     (connects D-E E)
     (key key2)
     (holdable key2)
     (unlocks key2 D-E)
     (in key2 D)

     (room F)
     (door E-F)
     (opened E-F false)
     (unlocked E-F false)

     (key key5)
     (holdable key5)
     (unlocks key5 E-F)
     (in key5 E)

     (key key6)
     (holdable key6)
     (unlocks key6 E-F)
     (in key6 F)
     })

(def base-state-thirtyeight
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

     (room E)
     (door D-E)
     (opened D-E false)
     (unlocked D-E false)

     (connects D-E D)
     (connects D-E E)
     (key key2)
     (holdable key2)
     (unlocks key2 D-E)
     (in key2 D)

     (room F)
     (door E-F)
     (opened E-F false)
     (unlocked E-F false)

     (key key5)
     (holdable key5)
     (unlocks key5 E-F)
     (in key5 E)

     (chest Y)
     (in Y E)

     (key key7)
     (holdable key7)
     (unlocks key7 Y)
     (in key7 F)
     })

(def base-state-fourty
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

     (room E)
     (door D-E)
     (opened D-E false)
     (unlocked D-E false)

     (connects D-E D)
     (connects D-E E)
     (key key2)
     (holdable key2)
     (unlocks key2 D-E)
     (in key2 D)

     (room F)
     (door E-F)
     (opened E-F false)
     (unlocked E-F false)

     (key key5)
     (holdable key5)
     (unlocks key5 E-F)
     (in key5 E)

     (key key6)
     (holdable key6)
     (unlocks key6 E-F)
     (in key6 F)

     (window window1)
     (connects window1 D)

     (window window2)
     (connects window2 F)
     })

(def base-state-fourtytwo
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

     (room E)
     (door D-E)
     (opened D-E false)
     (unlocked D-E false)

     (connects D-E D)
     (connects D-E E)
     (key key2)
     (holdable key2)
     (unlocks key2 D-E)
     (in key2 D)

     (room F)
     (door E-F)
     (opened E-F false)
     (unlocked E-F false)

     (key key5)
     (holdable key5)
     (unlocks key5 E-F)
     (in key5 E)

     (chest Y)
     (in Y E)

     (key key7)
     (holdable key7)
     (unlocks key7 Y)
     (in key7 F)

     (room G)
     (door A-G)
     (opened A-G true)
     (unlocked A-G true)
     })

(def base-state-fourtyfour
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

     (room E)
     (door D-E)
     (opened D-E false)
     (unlocked D-E false)

     (connects D-E D)
     (connects D-E E)
     (key key2)
     (holdable key2)
     (unlocks key2 D-E)
     (in key2 D)

     (room F)
     (door E-F)
     (opened E-F false)
     (unlocked E-F false)

     (key key5)
     (holdable key5)
     (unlocks key5 E-F)
     (in key5 E)

     (chest Y)
     (in Y E)

     (key key7)
     (holdable key7)
     (unlocks key7 Y)
     (in key7 F)

     (room G)
     (door A-G)
     (opened A-G true)
     (unlocked A-G true)

     (connects A-G A)
     (connects A-G G)
     })

(def base-state-fourtysix
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

     (room E)
     (door D-E)
     (opened D-E false)
     (unlocked D-E false)

     (connects D-E D)
     (connects D-E E)
     (key key2)
     (holdable key2)
     (unlocks key2 D-E)
     (in key2 D)

     (room F)
     (door E-F)
     (opened E-F false)
     (unlocked E-F false)

     (connects E-F E)
     (connects E-F F)

     (key key5)
     (holdable key5)
     (unlocks key5 E-F)
     (in key5 E)

     (chest Y)
     (in Y E)

     (key key7)
     (holdable key7)
     (unlocks key7 Y)
     (in key7 F)

     (room G)
     (door A-G)
     (opened A-G true)
     (unlocked A-G true)

     (connects A-G A)
     (connects A-G G)
     })

(def base-state-fourtyeight
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

     (room E)
     (door D-E)
     (opened D-E false)
     (unlocked D-E false)

     (connects D-E D)
     (connects D-E E)
     (key key2)
     (holdable key2)
     (unlocks key2 D-E)
     (in key2 D)

     (room F)
     (door E-F)
     (opened E-F false)
     (unlocked E-F false)

     (connects E-F E)
     (connects E-F F)

     (key key5)
     (holdable key5)
     (unlocks key5 E-F)
     (in key5 E)

     (chest Y)
     (in Y E)

     (key key7)
     (holdable key7)
     (unlocks key7 Y)
     (in key7 F)

     (room G)
     (door A-G)
     (opened A-G true)
     (unlocked A-G true)

     (connects A-G A)
     (connects A-G G)

     (window window3)
     (connects window3 A)
     })

(def base-state-fifty
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

     (room E)
     (door D-E)
     (opened D-E false)
     (unlocked D-E false)

     (connects D-E D)
     (connects D-E E)
     (key key2)
     (holdable key2)
     (unlocks key2 D-E)
     (in key2 D)

     (room F)
     (door E-F)
     (opened E-F false)
     (unlocked E-F false)

     (connects E-F E)
     (connects E-F F)

     (key key5)
     (holdable key5)
     (unlocks key5 E-F)
     (in key5 E)

     (chest Y)
     (in Y E)

     (key key7)
     (holdable key7)
     (unlocks key7 Y)
     (in key7 F)

     (room G)
     (door A-G)
     (opened A-G true)
     (unlocked A-G true)

     (connects A-G A)
     (connects A-G G)

     (window window3)
     (connects window3 A)

     (chest X)
     (in X G)
     })

(def base-state-fiftytwo
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

     (room E)
     (door D-E)
     (opened D-E false)
     (unlocked D-E false)

     (connects D-E D)
     (connects D-E E)
     (key key2)
     (holdable key2)
     (unlocks key2 D-E)
     (in key2 D)

     (room F)
     (door E-F)
     (opened E-F false)
     (unlocked E-F false)

     (connects E-F E)
     (connects E-F F)

     (key key5)
     (holdable key5)
     (unlocks key5 E-F)
     (in key5 E)

     (chest Y)
     (in Y E)

     (key key7)
     (holdable key7)
     (unlocks key7 Y)
     (in key7 F)

     (room G)
     (door A-G)
     (opened A-G false)
     (unlocked A-G false)

     (connects A-G A)
     (connects A-G G)

     (window window3)
     (connects window3 A)

     (key key8)
     (holdable key8)
     (unlocks key8 A-G)
     (in key8 F)
     })

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

;;Test one - run with state up to 52
(time (ops-search base-state-fourteen '((in R D)) operations))

;;Test two - run with state up to 52
(time (ops-search base-state-fourteen '((in R D) (opened A-D false)) operations))

;;Test three - run with state up to 52
(time (ops-search base-state-fourteen '((in R D) (opened A-D false) (unlocked A-D false) (holds R nil)) operations))
