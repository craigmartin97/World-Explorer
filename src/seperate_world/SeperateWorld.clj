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

(def base-world-fourteen
  '#{
     (agent R)
     (room A)
     (room D)
     (door A-D)
     (key key)
     (holdable key)
     })

(def base-state-fourteen-split
  '#{
     (opened A-D false)
     (unlocked A-D false)
     (connects A-D A)
     (connects A-D D)
     (in R A)

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

(def base-world-sixteen
  '#{
     (agent R)
     (room A)
     (room D)
     (door A-D)
     (key key)
     (holdable key)
     (chest Z)
     (connects A-D A)
     (connects A-D D)
     (unlocks key A-D)
     })

(def base-state-sixteen-split
  '#{
     (opened A-D false)
     (unlocked A-D false)
     (in R A)

     (holds R nil)
     (in key A)

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

(def base-world-eighteen
  '#{
     (agent R)
     (room A)
     (room D)
     (door A-D)
     (key key)
     (holdable key)
     (key key2)
     (holdable key2)
     (connects A-D A)
     (connects A-D D)
     (unlocks key A-D)
     (unlocks key2 A-D)
     })

(def base-state-eighteen-split
  '#{
     (opened A-D false)
     (unlocked A-D false)
     (in R A)

     (holds R nil)
     (in key A)

     (in key2 D)
     })

(def base-world-twenty
  '#{
     (agent R)
     (room A)
     (room D)
     (door A-D)
     (key key)
     (holdable key)
     (room E)
     (door D-E)
     (connects A-D A)
     (connects A-D D)
     (unlocks key A-D)
     (connects D-E D)
     (connects D-E E)
     })

(def base-state-twenty-split
  '#{
     (opened A-D false)
     (unlocked A-D false)

     (in R A)

     (holds R nil)
     (in key A)

     (opened D-E true)
     (unlocked D-E true)
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

(def base-world-twentytwo
  '#{
     (agent R)
     (room A)
     (room D)
     (door A-D)
     (connects A-D A)
     (connects A-D D)
     (key key)
     (holdable key)
     (unlocks key A-D)
     (room E)
     (door D-E)
     (connects D-E D)
     (connects D-E E)
     (chest Z)
     })


(def base-state-twentytwo-split
  '#{
     (opened A-D false)
     (unlocked A-D false)
     (in R A)

     (holds R nil)
     (in key A)

     (opened D-E true)
     (unlocked D-E true)

     (in Z A)
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

(def base-world-twentyfour
  '#{
     (agent R)
     (room A)
     (room D)
     (door A-D)
     (connects A-D A)
     (connects A-D D)
     (key key)
     (holdable key)
     (unlocks key A-D)
     (room E)
     (door D-E)
     (connects D-E D)
     (connects D-E E)
     (key key2)
     (holdable key2)
     (unlocks key2 D-E)
     })

(def base-state-twentyfour-split
  '#{
     (opened A-D false)
     (unlocked A-D false)
     (in R A)

     (holds R nil)
     (in key A)

     (opened D-E false)
     (unlocked D-E false)

     (in key2 D)
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

(def base-world-twentysix
  '#{
     (agent R)
     (room A)
     (room D)
     (door A-D)
     (connects A-D A)
     (connects A-D D)
     (key key)
     (holdable key)
     (unlocks key A-D)
     (room E)
     (door D-E)
     (connects D-E D)
     (connects D-E E)
     (chest Z)
     (chestkey key3)
     (holdable key3)
     (unlocks key3 Z)
     })

(def base-state-twentysix-split
  '#{
     (opened A-D false)
     (unlocked A-D false)
     (in R A)

     (holds R nil)
     (in key A)

     (opened D-E true)
     (unlocked D-E true)

     (in Z A)

     (in key3 E)
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

(def base-world-twentyeight
  '#{
     (agent R)
     (room A)
     (room D)
     (door A-D)
     (connects A-D A)
     (connects A-D D)
     (key key)
     (holdable key)
     (unlocks key A-D)
     (room E)
     (door D-E)
     (connects D-E D)
     (connects D-E E)
     (key key2)
     (holdable key2)
     (unlocks key2 D-E)
     (room F)
     (door E-F)
     })

(def base-state-twentyeight-split
  '#{
     (opened A-D false)
     (unlocked A-D false)
     (in R A)

     (holds R nil)
     (in key A)

     (opened D-E false)
     (unlocked D-E false)

     (in key2 D)

     (opened E-F true)
     (unlocked E-F true)
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

(def base-world-thirty
  '#{
     (agent R)
     (room A)
     (room D)
     (door A-D)
     (connects A-D A)
     (connects A-D D)
     (key key)
     (holdable key)
     (unlocks key A-D)
     (room E)
     (door D-E)
     (connects D-E D)
     (connects D-E E)
     (chest Z)
     (chestkey key3)
     (holdable key3)
     (unlocks key3 Z)

     (key key2)
     (holdable key2)
     (unlocks key2 D-E)
     })

(def base-state-thirty-split
  '#{
     (opened A-D false)
     (unlocked A-D false)
     (in R A)

     (holds R nil)
     (in key A)

     (opened D-E true)
     (unlocked D-E true)

     (in Z A)

     (in key3 E)

     (in key2 D)
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

(def base-world-thirtytwo
  '#{
     (agent R)
     (room A)
     (room D)
     (door A-D)
     (connects A-D A)
     (connects A-D D)
     (key key)
     (holdable key)
     (unlocks key A-D)
     (room E)
     (door D-E)
     (connects D-E D)
     (connects D-E E)
     (key key2)
     (holdable key2)
     (unlocks key2 D-E)
     (room F)
     (door E-F)
     (key key5)
     (holdable key5)
     (unlocks key5 E-F)
     })

(def base-state-thirtytwo-split
  '#{
     (opened A-D false)
     (unlocked A-D false)
     (in R A)

     (holds R nil)
     (in key A)

     (opened D-E false)
     (unlocked D-E false)

     (in key2 D)

     (opened E-F false)
     (unlocked E-F false)

     (in key5 E)
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

(def base-world-thirtyfour
  '#{
     (agent R)
     (room A)
     (room D)
     (door A-D)
     (connects A-D A)
     (connects A-D D)
     (key key)
     (holdable key)
     (unlocks key A-D)
     (room E)
     (door D-E)
     (connects D-E D)
     (connects D-E E)
     (key key2)
     (holdable key2)
     (unlocks key2 D-E)
     (room F)
     (door E-F)
     (key key5)
     (holdable key5)
     (unlocks key5 E-F)
     (chest Y)
     })

(def base-state-thirtyfour-split
  '#{
     (opened A-D false)
     (unlocked A-D false)
     (in R A)

     (holds R nil)
     (in key A)

     (opened D-E false)
     (unlocked D-E false)

     (in key2 D)

     (opened E-F false)
     (unlocked E-F false)

     (in key5 E)
     (in Y E)
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

(def base-world-thirtysix
  '#{
     (agent R)
     (room A)
     (room D)
     (door A-D)
     (connects A-D A)
     (connects A-D D)
     (key key)
     (holdable key)
     (unlocks key A-D)
     (room E)
     (door D-E)
     (connects D-E D)
     (connects D-E E)
     (key key2)
     (holdable key2)
     (unlocks key2 D-E)
     (room F)
     (door E-F)
     (key key5)
     (holdable key5)
     (unlocks key5 E-F)
     (key key6)
     (holdable key6)
     (unlocks key6 E-F)
     })

(def base-state-thirtysix-split
  '#{
     (opened A-D false)
     (unlocked A-D false)
     (in R A)

     (holds R nil)
     (in key A)

     (opened D-E false)
     (unlocked D-E false)

     (in key2 D)

     (opened E-F false)
     (unlocked E-F false)

     (in key5 E)

     (in key6 F)
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

(def base-world-thirtyeight
  '#{
     (agent R)
     (room A)
     (room D)
     (door A-D)
     (connects A-D A)
     (connects A-D D)
     (key key)
     (holdable key)
     (unlocks key A-D)
     (room E)
     (door D-E)
     (connects D-E D)
     (connects D-E E)
     (key key2)
     (holdable key2)
     (unlocks key2 D-E)
     (room F)
     (door E-F)
     (key key5)
     (holdable key5)
     (unlocks key5 E-F)
     (chest Y)
     (key key7)
     (holdable key7)
     (unlocks key7 Y)
     })

(def base-state-thirtyeight-split
  '#{
     (opened A-D false)
     (unlocked A-D false)
     (in R A)

     (holds R nil)
     (in key A)

     (opened D-E false)
     (unlocked D-E false)

     (in key2 D)

     (opened E-F false)
     (unlocked E-F false)

     (in key5 E)

     (in Y E)

     (in key7 F)
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

(def base-world-fourty
  '#{
     (agent R)
     (room A)
     (room D)
     (door A-D)
     (connects A-D A)
     (connects A-D D)
     (key key)
     (holdable key)
     (unlocks key A-D)
     (room E)
     (door D-E)
     (connects D-E D)
     (connects D-E E)
     (key key2)
     (holdable key2)
     (unlocks key2 D-E)
     (room F)
     (door E-F)
     (key key5)
     (holdable key5)
     (unlocks key5 E-F)
     (key key6)
     (holdable key6)
     (unlocks key6 E-F)
     (window window1)
     (connects window1 D)
     (window window2)
     (connects window2 F)
     })

(def base-state-fourty-split
  '#{
     (opened A-D false)
     (unlocked A-D false)
     (in R A)

     (holds R nil)
     (in key A)

     (opened D-E false)
     (unlocked D-E false)

     (in key2 D)

     (opened E-F false)
     (unlocked E-F false)

     (in key5 E)

     (in key6 F)
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

(def base-world-fourtytwo
  '#{
     (agent R)
     (room A)
     (room D)
     (door A-D)
     (connects A-D A)
     (connects A-D D)
     (key key)
     (holdable key)
     (unlocks key A-D)
     (room E)
     (door D-E)
     (connects D-E D)
     (connects D-E E)
     (key key2)
     (holdable key2)
     (unlocks key2 D-E)
     (room F)
     (door E-F)
     (key key5)
     (holdable key5)
     (unlocks key5 E-F)
     (chest Y)
     (key key7)
     (holdable key7)
     (unlocks key7 Y)
     (room G)
     (door A-G)
     })

(def base-state-fourtytwo-split
  '#{
     (opened A-D false)
     (unlocked A-D false)
     (in R A)

     (holds R nil)
     (in key A)

     (opened D-E false)
     (unlocked D-E false)

     (in key2 D)

     (opened E-F false)
     (unlocked E-F false)

     (in key5 E)

     (in Y E)

     (in key7 F)

     (opened A-G true)
     (unlocked A-G true)
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

(def base-world-fourtyfour
  '#{
     (agent R)
     (room A)
     (room D)
     (door A-D)
     (connects A-D A)
     (connects A-D D)
     (key key)
     (holdable key)
     (unlocks key A-D)
     (room E)
     (door D-E)
     (connects D-E D)
     (connects D-E E)
     (key key2)
     (holdable key2)
     (unlocks key2 D-E)
     (room F)
     (door E-F)
     (key key5)
     (holdable key5)
     (unlocks key5 E-F)
     (chest Y)
     (key key7)
     (holdable key7)
     (unlocks key7 Y)
     (room G)
     (door A-G)
     (connects A-G A)
     (connects A-G G)
     })

(def base-state-fourtyfour-split
  '#{
     (opened A-D false)
     (unlocked A-D false)
     (in R A)

     (holds R nil)
     (in key A)

     (opened D-E false)
     (unlocked D-E false)

     (in key2 D)

     (opened E-F false)
     (unlocked E-F false)

     (in key5 E)

     (in Y E)

     (in key7 F)

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

(def base-world-fourtysix
  '#{
     (agent R)
     (room A)
     (room D)
     (door A-D)
     (connects A-D A)
     (connects A-D D)
     (key key)
     (holdable key)
     (unlocks key A-D)
     (room E)
     (door D-E)
     (connects D-E D)
     (connects D-E E)
     (key key2)
     (holdable key2)
     (unlocks key2 D-E)
     (room F)
     (door E-F)
     (connects E-F E)
     (connects E-F F)
     (key key5)
     (holdable key5)
     (unlocks key5 E-F)
     (chest Y)
     (key key7)
     (holdable key7)
     (unlocks key7 Y)
     (room G)
     (door A-G)
     (connects A-G A)
     (connects A-G G)
     })

(def base-state-fourtysix-split
  '#{
     (opened A-D false)
     (unlocked A-D false)
     (in R A)

     (holds R nil)
     (in key A)

     (opened D-E false)
     (unlocked D-E false)

     (in key2 D)

     (opened E-F false)
     (unlocked E-F false)

     (in key5 E)

     (in Y E)

     (in key7 F)

     (opened A-G true)
     (unlocked A-G true)
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

(def base-world-foutyeight
  '#{
     (agent R)
     (room A)
     (room D)
     (door A-D)
     (connects A-D A)
     (connects A-D D)
     (key key)
     (holdable key)
     (unlocks key A-D)
     (room E)
     (door D-E)
     (connects D-E D)
     (connects D-E E)
     (key key2)
     (holdable key2)
     (unlocks key2 D-E)
     (room F)
     (door E-F)
     (connects E-F E)
     (connects E-F F)
     (key key5)
     (holdable key5)
     (unlocks key5 E-F)
     (chest Y)
     (key key7)
     (holdable key7)
     (unlocks key7 Y)
     (room G)
     (door A-G)
     (connects A-G A)
     (connects A-G G)
     (window window3)
     (connects window3 A)
     })

(def base-state-fourtyeight-split
  '#{
     (opened A-D false)
     (unlocked A-D false)
     (in R A)

     (holds R nil)
     (in key A)

     (opened D-E false)
     (unlocked D-E false)

     (in key2 D)

     (opened E-F false)
     (unlocked E-F false)

     (in key5 E)

     (in Y E)

     (in key7 F)

     (opened A-G true)
     (unlocked A-G true)
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

(def base-world-fifty
  '#{
     (agent R)
     (room A)
     (room D)
     (door A-D)
     (connects A-D A)
     (connects A-D D)
     (key key)
     (holdable key)
     (unlocks key A-D)
     (room E)
     (door D-E)
     (connects D-E D)
     (connects D-E E)
     (key key2)
     (holdable key2)
     (unlocks key2 D-E)
     (room F)
     (door E-F)
     (connects E-F E)
     (connects E-F F)
     (key key5)
     (holdable key5)
     (unlocks key5 E-F)
     (chest Y)
     (key key7)
     (holdable key7)
     (unlocks key7 Y)
     (room G)
     (door A-G)
     (connects A-G A)
     (connects A-G G)
     (window window3)
     (connects window3 A)
     (chest X)
     })

(def base-state-fifty-split
  '#{
     (opened A-D false)
     (unlocked A-D false)
     (in R A)

     (holds R nil)
     (in key A)

     (opened D-E false)
     (unlocked D-E false)

     (in key2 D)

     (opened E-F false)
     (unlocked E-F false)
     (in key5 E)

     (in Y E)

     (in key7 F)

     (opened A-G true)
     (unlocked A-G true)
     (in X G)
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

(def base-world-fiftytwo
  '#{
     (agent R)
     (room A)
     (room D)
     (door A-D)
     (connects A-D A)
     (connects A-D D)
     (key key)
     (holdable key)
     (unlocks key A-D)
     (room E)
     (door D-E)
     (connects D-E D)
     (connects D-E E)
     (key key2)
     (holdable key2)
     (unlocks key2 D-E)
     (room F)
     (door E-F)
     (connects E-F E)
     (connects E-F F)
     (key key5)
     (holdable key5)
     (unlocks key5 E-F)
     (chest Y)
     (key key7)
     (holdable key7)
     (unlocks key7 Y)
     (room G)
     (door A-G)
     (connects A-G A)
     (connects A-G G)
     (window window3)
     (connects window3 A)
     (key key8)
     (holdable key8)
     (unlocks key8 A-G)
     })

(def base-state-fiftytwo-split
  '#{
     (opened A-D false)
     (unlocked A-D false)
     (in R A)

     (holds R nil)
     (in key A)

     (opened D-E false)
     (unlocked D-E false)

     (in key2 D)

     (opened E-F false)
     (unlocked E-F false)

     (in key5 E)

     (in Y E)

     (in key7 F)

     (opened A-G false)
     (unlocked A-G false)

     (in key8 F)
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

;;Test merged - run with state 14 up to 52
(time (ops-search base-state-fourteen '((in R D) (opened A-D false) (unlocked A-D false) (holds R nil)) operations))

;;Test split - run with state 14 up to 52
(time (ops-search base-state-fourteen '((in R D) (opened A-D false) (unlocked A-D false) (holds R nil)) operations :world base-world-fourteen))