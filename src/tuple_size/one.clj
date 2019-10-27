(ns tuple-size.one
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            [clj-memory-meter.core :as mm]
            [matcher-starter.ops-search-base-ops :refer :all]
            )
  )

;-----------------------------------------
;-----------------------------------------
;---------------State--------------------------
;-----------------------------------------
;-----------------------------------------

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