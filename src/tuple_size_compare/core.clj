(ns tuple-size-compare.core
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            [clojure.set :refer :all]))


;------------------------------------------------
(defn ui-out [win & str]
  (apply  println str))

(defn print-goals [q]
  (if (not (empty? q))
    (do
      (ui-out :dbg "GOALS:")
      (doseq [x q]
        (ui-out :dbg "      " (if (map? x) [(:name x) :=> (:achieves x)] x))
        )
      ;(ui-out :dbg '------)
      )
    ))


(def goalq (atom (java.util.concurrent.LinkedBlockingDeque.)))


(declare strips-loop update-path
         goal-mop-apply apply-goal-op)

(defn planner [state goal goal-ops]
  (.clear @goalq)
  (.push @goalq goal)
  (strips-loop {:state state, :cmds nil, :txt nil} goal-ops 60))


(defn strips-loop
  [path goal-ops limit]
  (if (zero? limit)
    (throw (new RuntimeException "limit exceeded in run-goal-ops")))

  ;(println path)
  (print-goals @goalq)

  (if-let [goal (.poll @goalq)]
    (cond
      (map? goal) ;; it is a partially matched op
      (do
        (ui-out :dbg '** 'APPLYING (:name goal) '=> (:achieves goal))
        ; (ui-out :dbg '** (:add goal))
        (recur
          (update-path path (goal-mop-apply (:state path) goal))
          goal-ops (dec limit))
        )

      ;; else it is a fact
      (not (contains? (:state path) goal))
      (do (ui-out :dbg 'solving goal)
          (some (partial apply-goal-op (:state path) goal)
                (vals goal-ops))
          (recur path goal-ops (dec limit))
          )
      ;; else it is an existing fact
      :else
      (recur path goal-ops (dec limit))
      )
    path
    )
  )


(defn goal-mop-apply [bd mop]
  (mfind* [(:pre mop) bd]
          (ui-out :dbg '** (mout (:add mop)))
          ; (ui-out :dbg '=> (mout mop))
          {:state (union (mout (:add mop))
                         (difference bd (mout (:del mop))))
           :cmd   (mout (:cmd mop))
           :txt   (mout (:txt mop))
           }
          ))


(defn apply-goal-op [bd goal op]
  ;(println (list 'trying (:name op)))
  (mlet [(:achieves op) goal]

        (mfind* [(:when op) bd]
                (ui-out :dbg 'using=> (:name op))
                (let [mop (mout op)]
                  ;(println (list 'new-mop mop))
                  (.push @goalq mop)
                  (ui-out :dbg 'new-goals (or (:post mop) '-none))
                  (doseq [p (reverse (:post mop))]
                    (.push @goalq p))

                  ;(println (list 'succeeded (:name op)))
                  true
                  ))
        ))


(defn update-path
  [current newp]
  { :state (:state newp),
   :cmds  (concat (:cmds current) (:cmd newp)),
   :txt   (concat (:txt current) (:txt newp))
   })

;------------------------------------------------------------
;------------------------------------------------------------
;------------------------------------------------------------
;------------------------------------------------------------
;------------------------------------------------------------
;------------------------------------------------------------


(def planner-operations
  "A map of operations that the agent can perform in the world"
  '{
    move
    {
     :name move-agent
     :achieves (in ?agent ?room2)
     :when (
            (agent ?agent)
            (in ?agent ?room1)
            ;  (connects ?door ?room1)
            (room ?room1)
            (room ?room2)
            ; (door ?door)
            ; (:guard (println (? door)) )
            )
     :post (
             ; (opened ?door true)
             ;  (unlocked ?door true)
             )
     :pre ()
     :add (
           (in ?agent ?room2)
           )
     :del (
           (in ?agent ?room1)
           )
     :txt (agent ?agent has moved from ?room1 to ?room2)
     }
    pickup
    {
     :name pickup-obj
     :achieves (holds ?agent ??x ?obj)
     :when (
            (agent ?agent)
            (room ?room1)
            (holdable ?obj)
            (in ?obj ?room1)
            )
     :post ((holds ?agent ??x)
            (in ?agent ?room1))
     :pre ()
     :add (
           (holds ?agent ??x ?obj)
           ;(holds ?agent ?obj)
           )
     :del (
           (holds ?agent ??x)
           (in ?obj ?room1)
           )
     :txt (?agent picked up ?obj from ?room1)

     }
    drop-obj
    {
     :name drop-obj
     :achieves (in ?obj ?room1)
     :when (
            (agent ?agent)
            (holdable ?obj)
            (room ?room1)
            )
     :post (
            (holds ?agent ?obj)
            (in ?agent ?room1)
            )
     :pre ()
     :add ((in ?obj ?room1))
     :del ((holds ?agent ?obj))
     :txt (?agent dropped ?obj in ?room1)
     }

    }
  )

(def operations
  '{move { :pre ((agent ?agent)
                 (in ?agent ?room1)
                 (room ?room1)
                 (room ?room2)
                 )
          :add ((in ?agent ?room2))
          :del ((in ?agent ?room1))
          :txt (?agent has moved from ?room1 to ?room2)
          }
    pickup {:pre ((agent ?agent)
                  (room ?room1)
                  (holdable ?obj)
                  (in ?agent ?room1)
                  (in ?obj ?room1)
                  (holds ?agent ??x))
            :add ((holds ?agent ??x ?obj))
            :del (
                   (in ?obj ?room1)
                   (holds ?agent ??x)
                   )
            :txt (?agent picked up ?obj from ?room1)
            }
    drop {:pre ((agent ?agent)
                (room ?room1)
                (holdable ?obj)
                (in ?agent ?room1)
                (holds ?agent ??x ?obj ??y))
          :add ((holds ?agent ??x ??y) (in ?obj ?room1))
          :del ((holds ?agent ??x ?obj ??y))
          :txt (?agent dropped ?obj in ?room1)
          }

    })


(def state
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

     }
  )

(def state-many-objs
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
;--------------------------------------------
;--------------------------------------------
;--------------------------------------------

(defn ops-pickup-dog []
  "Agent picks up dog using ops-search

  Elapsed time: 12.7335 msecs"
  (time (ops-search state '((holds R dog)) operations))
  )

(defn planner-pickup-dog []
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
  (time (ops-search state '((holds R dog cat)) operations))
  )

(defn planner-pickup-dog-cat []
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
  (time (ops-search state '((holds R dog cat mouse)) operations))
  )

(defn planner-pickup-dog-cat-mouse []
  "Agent picks up the dog, cat and mouse from room A using planner

  Elapsed time:11.057 msecs"
  (time (planner state '(holds R dog cat mouse) planner-operations))
  )

;-----------------------------
;-----------------------------
;-----------------------------
;-----------------------------

(defn ops-pickup-key []
  "Agent picks up dog, cat and mouse using ops-search

  Elapsed time: 25.3425 msecs"
  (time (ops-search state '((holds R key)) operations))
  )

(defn planner-pickup-key []
  "Agent picks up the dog, cat and mouse from room A using planner

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
  (time (ops-search state '((holds R key dog cat mouse)) operations))
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
  (time (ops-search state '((holds R dog cat mouse key)) operations))
  )

(defn planner-pickup-all-small-state-change-order []
  "Agent picks up the dog, cat, mouse and key using planner
  Changing the goal state from above to see if it affects the time complexity
  or the amount of operations the agent does

  It does affect the order as the agent will retrieve each object in the order specified
  Meaning, its not gaurenteed to be the most optimal solution
  Elapsed time:14.8527 msecs"
  (time (planner state '(holds R  dog cat mouse key) planner-operations))
  )

;-----------------------------
;-----------------------------
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
  (time (ops-search state '((holds R dog cat mouse key dog)) operations))
  )

(defn ops-duplicate-dog-two []
  "Agent picks up dog, cat, mouse and key using ops-search
  A duplicate dog object has been specified in the goal state
  to see how the ops-search will react

  Ops-search will pick up the same object multiple times
  Elapsed time: unalbe to solve"
  (time (ops-search state '((holds R dog dog dog)) operations))
  )

(defn plannner-duplicate-dog []
  "Agent picks up the dog, cat, mouse and key using planner
  A duplicate dog object has been specified in the goal state
  to see how the planner will react

  Planner will will pick up a dog, and then pick up another dog
  even though there is only one dog in the state

  Elapsed time:14.8527 msecs"
  (time (planner state '(holds R  dog cat mouse key dog) planner-operations))
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
  (time (ops-search state-many-objs '((holds R key dog cat mouse phone cup)) operations :debug true))
  )

(defn planner-search-pickup-six-objs []
  "
  Agent picksup 6 objects using planner

  Elapsed time: 67.8264 msecs"
  (time (planner state-many-objs '(holds R  key dog cat mouse phone cup) planner-operations))
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
  (time (ops-search state-many-objs '((holds R key dog cat mouse phone cup glass remote)) operations :debug true))
  )

(defn planner-search-pickup-eight-objs []
  "
  Agent picks up eight objects using planner

  Elapsed time: 67.8264 msecs"
  (time (planner state-many-objs '(holds R  key dog cat mouse phone cup glass remote) planner-operations))
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
  (time (ops-search state-many-objs '((holds R key dog cat mouse phone cup glass remote water watch)) operations :debug true))
  )

(defn planner-search-pickup-many []
  "
  Agent picks up many objects (10) using planner

  Elapsed time: 67.8264 msecs"
  (time (planner state-many-objs '(holds R  key dog cat mouse phone cup glass remote water watch) planner-operations))
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
     (time (planner state-huge '(holds R  key dog cat mouse phone cup glass remote water watch
                                       case keyboard screen
                                       ) planner-operations))
     )

(defn planner-search-pickup-huge-state-14 []
  "
  Agent picks up lots of items from different locations using planner

  Elapsed time: 120.6767 msecs"
  (time (planner state-huge '(holds R  key dog cat mouse phone cup glass remote water watch
                                    case keyboard screen chair
                                    ) planner-operations))
  )

(defn planner-search-pickup-huge-state-15 []
  "
  Agent picks up lots of items from different locations using planner

  Elapsed time: 134.023 msecs"
  (time (planner state-huge '(holds R  key dog cat mouse phone cup glass remote water watch
                                    case keyboard screen chair folder
                                    ) planner-operations))
  )

(defn planner-search-pickup-huge-state-16 []
  "
  Agent picks up lots of items from different locations using planner

  Elapsted time (when limit was 100) : 167.133399 msecs
  Elapsed time: unsolvable, unless we edit the limit in planner code (60 to 100)"
  (time (planner state-huge '(holds R  key dog cat mouse phone cup glass remote water watch
                                    case keyboard screen chair folder apple
                                    ) planner-operations))
  )

(defn planner-search-pickup-huge-state-all []
  "
  Agent picks up lots of items from different locations using planner

  Elapsted time (when limit was 120) : 307.8745 msecs
  Elapsed time: unsolvable, unless we edit the limit in planner code (60 to 120)"
  (time (planner state-huge '(holds R  key dog cat mouse phone cup glass remote water watch
                                    case keyboard screen chair folder apple mango
                                    pear glove hat scarf tea coffee
                                    ) planner-operations))
  )