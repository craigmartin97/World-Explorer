(ns world-explorer
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            [matcher-starter.ops_search_base_ops :refer :all]
            )
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


;;Test one - run with state up to 52
(time (ops-search base-state-fourteen '((in R D)) operations))

;;Test two - run with state up to 52
(time (ops-search base-state-fourteen '((in R D) (opened A-D false)) operations))

;;Test three - run with state up to 52
(time (ops-search base-state-fourteen '((in R D) (opened A-D false) (unlocked A-D false) (holds R nil)) operations))
