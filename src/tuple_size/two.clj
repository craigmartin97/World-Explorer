(ns tuple-size.two
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            [clj-memory-meter.core :as mm]
            [matcher-starter.ops-search-base-ops :refer :all]
            )
  )

;-----------------------------------------
;-----------------------------------------
;----------------State-------------------------
;-----------------------------------------
;-----------------------------------------

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