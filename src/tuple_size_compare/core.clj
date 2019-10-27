(ns tuple-size-compare.core
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            [clojure.set :refer :all]
            [planner.planner :refer :all]
            [matcher-starter.ops-search-base-ops :refer :all]
            )
  )

;----------------------------------------
;----------------------------------------
;------------------State----------------------
;----------------------------------------
;----------------------------------------

(def state
  '#{
     ;define agent
     (agent R)
     ;define rooms
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
     (room K)

     (in R A)
     (holds R key dog cat mouse)

     (holdable key)
     (holdable dog)
     (holdable cat)
     (holdable mouse)
     }
  )

;-----------------------------------------
;-----------------------------------------
;-------------------Medium state----------------------
;-----------------------------------------
;-----------------------------------------

(def state-medium
  '#{
     ;define agent
     (agent R)
     ;define rooms
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
     (room K)

     (in R A)
     (holds R key dog cat mouse phone cup glass remote water watch)

     (holdable key)
     (holdable dog)
     (holdable cat)
     (holdable mouse)
     (holdable phone)
     (holdable cup)
     (holdable glass)
     (holdable remote)
     (holdable water)
     (holdable watch)
     }
  )

;-----------------------------------------
;-----------------------------------------
;-------------------Holds all----------------------
;-----------------------------------------
;-----------------------------------------

(def state-r-holds-all
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
     (room K)

     (in R A)
     (holds R key dog cat mouse phone cup glass remote water watch case keyboard coffee tea screen chair
            folder apple mango pear glove hat scarf)

     (holdable key)
     (holdable dog)
     (holdable cat)
     (holdable mouse)
     (holdable phone)
     (holdable cup)
     (holdable glass)
     (holdable remote)
     (holdable water)
     (holdable watch)
     (holdable case)
     (holdable keyboard)
     (holdable screen)
     (holdable chair)
     (holdable folder)
     (holdable apple)
     (holdable mango)
     (holdable pear)
     (holdable glove)
     (holdable hat)
     (holdable scarf)
     (holdable tea)
     (holdable coffee)
     }
  )



;----------------------------------------
;----------------------------------------
;--------------Tests Small--------------------------
;----------------------------------------
;----------------------------------------

(defn planner-small []
  "Elapsed time: 1.233301 msecs"
  (time (planner state '(in R F) planner-operations))
  )

(defn ops-small []
  "Elapsed time: 10.726201 msecs"
  (time (ops-search state '((in R F)) ops-search-compare-operations))
  )

;------------------------------------------
;------------------------------------------
;------------------Tests medium------------------------
;------------------------------------------
;------------------------------------------

(defn planner-medium []
  "Elapsed time: 1.186599 msecs"
  (time (planner state-medium '(in R F) planner-operations))
  )

(defn ops-medium []
  "Elapsed time: 27.296999 msecs"
  (time (ops-search state-medium '((in R F)) ops-search-compare-operations))
  )


;------------------------------------------
;------------------------------------------
;------------------Huge medium------------------------
;------------------------------------------
;------------------------------------------

(defn planner-r-holds-many []
  "Elapsed time: 1.2265 msecs"
  (time (planner state-r-holds-all '(in R F) planner-operations))
  )

(defn ops-r-holds-many []
  "Elapsed time: 82.1064 msecs"
  (time (ops-search state-r-holds-all '((in R F)) ops-search-compare-operations))
  )