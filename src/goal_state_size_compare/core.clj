(ns goal-state-size-compare.core
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            [clojure.set :refer :all]
            [planner.planner :refer :all]
            [matcher-starter.ops-search-base-ops :refer :all]
            ))

;----------------------------
;----------------------------
;--------------States--------------
;----------------------------
;----------------------------

(def state
  "A small scenario with a rooms and a few objects"
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
     (holds R)

     (holdable key)
     (in key D)

     (holdable dog)
     (in dog A)

     (holdable cat)
     (in cat A)

     (holdable mouse)
     (in mouse A)

     }
  )

(def state-many-objs
  "A more complicated scenario with more objects"
  '#{
     ;define agentLL
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
     (holds R)

     (holdable key)
     (in key D)

     (holdable dog)
     (in dog A)

     (holdable cat)
     (in cat A)

     (holdable mouse)
     (in mouse A)

     (holdable phone)
     (in phone B)

     (holdable cup)
     (in cup F)

     (holdable glass)
     (in glass K)

     (holdable remote)
     (in remote I)

     (holdable water)
     (in water I)

     (holdable watch)
     (in watch G)

     }
  )

(def state-huge
  "A large scenario with lots of objects scattered around the rooms"
  '#{
     ;define agentLL
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
     (holds R)

     (holdable key)
     (in key D)

     (holdable dog)
     (in dog A)

     (holdable cat)
     (in cat A)

     (holdable mouse)
     (in mouse A)

     (holdable phone)
     (in phone B)

     (holdable cup)
     (in cup F)

     (holdable glass)
     (in glass K)

     (holdable remote)
     (in remote I)

     (holdable water)
     (in water I)

     (holdable watch)
     (in watch G)

     (holdable case)
     (in case E)

     (holdable keyboard)
     (in keyboard K)

     (holdable screen)
     (in screen J)

     (holdable chair)
     (in chair A)

     (holdable folder)
     (in folder D)

     (holdable apple)
     (in apple D)

     (holdable mango)
     (in mango G)

     (holdable pear)
     (in pear H)

     (holdable glove)
     (in glove I)

     (holdable hat)
     (in hat B)

     (holdable scarf)
     (in scarf C)

     (holdable tea)
     (in tea C)

     (holdable coffee)
     (in coffee D)

     }
  )

;--------------------------------------------
;--------------------------------------------
;--------------------Tests------------------------
;--------------------------------------------
;--------------------------------------------

(defn ops-pickup-dog []
  "Agent picks up dog using ops-search

  Elapsed time: 12.7335 msecs"
  (time (ops-search state '((holds R dog)) operations))
  )

(defn planner-pickup-dog []                                 ;faster
  "Agent picks up the dog from room A using planner

  Elapsed time:4.4539 msecs"
  (time (planner state '(holds R dog) planner-operations))
  )

;-----------------------------
;-----------------------------
;-----------------------------
;-----------------------------

(defn ops-pickup-dog-cat []
  "Agent picks up dog and cat using ops-search

  Elapsed time: 159.4964 msecs"
  (time (ops-search state '((holds R dog cat)) ops-search-compare-operations))
  )

(defn planner-pickup-dog-cat []                             ;faster
  "Agent picks up the dog and cat from room A using planner

  Elapsed time:6.091 msecs"
  (time (planner state '(holds R dog cat) planner-operations))
  )

;-----------------------------
;-----------------------------
;-----------------------------
;-----------------------------

(defn ops-pickup-dog-cat-mouse []
  "Agent picks up dog, cat and mouse using ops-search

  Elapsed time: 613.6952 msecs"
  (time (ops-search state '((holds R dog cat mouse)) ops-search-compare-operations))
  )

(defn planner-pickup-dog-cat-mouse []                       ;faster
  "Agent picks up the dog, cat and mouse from room A using planner

  Elapsed time:11.057 msecs"
  (time (planner state '(holds R dog cat mouse) planner-operations))
  )

;-----------------------------
;-----------------------------
;-----------------------------
;-----------------------------

(defn ops-pickup-key []
  "Agent picks up key using ops-search

  Elapsed time: 25.3425 msecs"
  (time (ops-search state '((holds R key)) ops-search-compare-operations))
  )

(defn planner-pickup-key []                                 ;faster
  "Agent picks up the key from room A using planner

  Elapsed time:4.0412 msecs"
  (time (planner state '(holds R key) planner-operations))
  )

;-----------------------------
;-----------------------------
;-----------------------------
;-----------------------------

(defn ops-pickup-all-small-state []
  "Agent picks up dog, cat, mouse and key using ops-search

  Elapsed time: 13039.4086 msecs"
  (time (ops-search state '((holds R key dog cat mouse)) ops-search-compare-operations))
  )

(defn planner-pickup-all-small-state []
  "Agent picks up the dog, cat, mouse and key using planner

  Elapsed time:14.8527 msecs"
  (time (planner state '(holds R key dog cat mouse) planner-operations))
  )

;-----------------------------
;-----------------------------
;-----------------------------
;-----------------------------

(defn ops-pickup-all-small-state-change-order []
  "Agent picks up dog, cat, mouse and key using ops-search
  Changing the goal state from above to see if it affects the time complexity
  or the amount of operations the agent does

  It does affect the order as the agent will retrieve each object in the order specified
  Meaning, its not gaurenteed to be the most optimal solution
  Elapsed time: 13039.4086 msecs"
  (time (ops-search state '((holds R dog cat mouse key)) ops-search-compare-operations))
  )

(defn planner-pickup-all-small-state-change-order []
  "Agent picks up the dog, cat, mouse and key using planner
  Changing the goal state from above to see if it affects the time complexity
  or the amount of operations the agent does

  It does affect the order as the agent will retrieve each object in the order specified
  Meaning, its not gaurenteed to be the most optimal solution
  Elapsed time:14.8527 msecs"
  (time (planner state '(holds R dog cat mouse key) planner-operations))
  )

;-----------------------------
;-----------------------------
;-------------Duplicate objects in goal state----------------
;-----------------------------
;-----------------------------

(defn ops-duplicate-dog []
  "Agent picks up dog, cat, mouse and key using ops-search
  A duplicate dog object has been specified in the goal state
  to see how the ops-search will react

  Op-search will try and pick up one dog, and another and another.
  As there is only one dog in the world the agent wont be able to do this.
  It will manage to find one dog, and try every possible combination and explore all
  depths until it eventually fails and returns nil.

  The ide in this case will run out of avaliable memory before this happens.
  Elapsed time: unable to solve"
  (time (ops-search state '((holds R dog cat mouse key dog)) ops-search-compare-operations))
  )

(defn ops-duplicate-dog-two []
  "Agent picks up dog, cat, mouse and key using ops-search
  A duplicate dog object has been specified in the goal state
  to see how the ops-search will react

  Ops-search will fail to pick up the dog multiple times as it there is only one dog in the state
  Elapsed time: unalbe to solve"
  (time (ops-search state '((holds R dog dog dog)) ops-search-compare-operations))
  )

(defn plannner-duplicate-dog []
  "Agent picks up the dog, cat, mouse and key using planner
  A duplicate dog object has been specified in the goal state
  to see how the planner will react

  Planner will will pick up a dog, and then pick up another dog
  even though there is only one dog in the state

  Elapsed time:14.8527 msecs"
  (time (planner state '(holds R dog cat mouse key dog) planner-operations))
  )

;----------------------------------
;----------------------------------
;----------------Harder tests------------------
;----------------------------------
;----------------------------------


;--------------------------------
;---------------Pickup 6 items-----------------
;--------------------------------


(defn ops-search-pickup-six-objs []
  "
  Agent picksup 6 objects using ops-search
  Elapsed time: "
  (time (ops-search state-many-objs '((holds R key dog cat mouse phone cup)) ops-search-compare-operations :debug true))
  )

(defn planner-search-pickup-six-objs []
  "
  Agent picksup 6 objects using planner

  Elapsed time: 67.8264 msecs"
  (time (planner state-many-objs '(holds R key dog cat mouse phone cup) planner-operations))
  )

;-----------------------------------
;-----------------------------------
;-----------------Pickup 8 items------------------
;-----------------------------------
;-----------------------------------



(defn ops-search-pickup-eight-objs []
  "
  Agent will pickup eight objects using op-search
  Elapsed time: "
  (time (ops-search state-many-objs '((holds R key dog cat mouse phone cup glass remote)) ops-search-compare-operations))
  )

(defn planner-search-pickup-eight-objs []
  "
  Agent picks up eight objects using planner

  Elapsed time: 67.8264 msecs"
  (time (planner state-many-objs '(holds R key dog cat mouse phone cup glass remote) planner-operations))
  )

;------------------------------------
;------------------------------------
;---------------Pickup ten items---------------------
;------------------------------------
;------------------------------------

(defn ops-search-pickup-many []
  "
  Agent picks up many objects (10) using ops-search
  Elapsed time: unsolvable"
  (time (ops-search state-many-objs '((holds R key dog cat mouse phone cup glass remote water watch)) ops-search-compare-operations))
  )

(defn planner-search-pickup-many []
  "
  Agent picks up many objects (10) using planner

  Elapsed time: 67.8264 msecs"
  (time (planner state-many-objs '(holds R key dog cat mouse phone cup glass remote water watch) planner-operations))
  )

;-------------------------------
;-------------------------------
;---------------Planner with huge state----------------
;-------------------------------
;-------------------------------

(defn planner-search-pickup-huge-state-13 []
  "
  Agent picks up lots of items from different locations using planner

  Elapsed time: 117.7888 msecs"
  (time (planner state-huge '(holds R key dog cat mouse phone cup glass remote water watch
                                    case keyboard screen
                                    ) planner-operations))
  )

(defn planner-search-pickup-huge-state-14 []
  "
  Agent picks up lots of items from different locations using planner

  Elapsed time: 120.6767 msecs"
  (time (planner state-huge '(holds R key dog cat mouse phone cup glass remote water watch
                                    case keyboard screen chair
                                    ) planner-operations))
  )

(defn planner-search-pickup-huge-state-15 []
  "
  Agent picks up lots of items from different locations using planner

  Elapsed time: 134.023 msecs"
  (time (planner state-huge '(holds R key dog cat mouse phone cup glass remote water watch
                                    case keyboard screen chair folder
                                    ) planner-operations))
  )

(defn planner-search-pickup-huge-state-16 []
  "
  Agent picks up lots of items from different locations using planner

  Elapsted time (when limit was 100) : 167.133399 msecs
  Elapsed time: unsolvable, unless we edit the limit in planner code (60 to 100)"
  (time (planner state-huge '(holds R key dog cat mouse phone cup glass remote water watch
                                    case keyboard screen chair folder apple
                                    ) planner-operations))
  )

(defn planner-search-pickup-huge-state-all []
  "
  Agent picks up lots of items from different locations using planner

  Elapsted time (when limit was 120) : 307.8745 msecs
  Elapsed time: unsolvable, unless we edit the limit in planner code (60 to 120)"
  (time (planner state-huge '(holds R key dog cat mouse phone cup glass remote water watch
                                    case keyboard screen chair folder apple mango
                                    pear glove hat scarf tea coffee
                                    ) planner-operations))
  )



