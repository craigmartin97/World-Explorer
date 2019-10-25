(ns tuple-order-planner.move
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

;--------------------------------------------------
;--------------------------------------------------
;---------------------State-----------------------------
;--------------------------------------------------
;--------------------------------------------------

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


;--------------------------------------
;--------------------------------------
;-----------------Planner operations---------------------
;--------------------------------------
;--------------------------------------


(def planner-operations-one
     "A map of operations that the agent can perform in the world
     This is the first iteration of the planner operations list."
     '{
       move
       {
        :name move-agent
        :achieves (in ?agent ?room2)
        :when (
                (agent ?agent)
                (room ?room1)
                (room ?room2)
                (in ?agent ?room1)
                )
        :post ()
        :pre ()
        :add (
               (in ?agent ?room2)
               )
        :del (
               (in ?agent ?room1)
               )
        :txt (agent ?agent has moved from ?room1 to ?room2)
        }

       }
     )



(def planner-operations-two
  "A map of operations that the agent can perform in the world
  This is the first iteration of the planner operations list."
  '{
    move
    {
     :name move-agent
     :achieves (in ?agent ?room2)
     :when (
             (in ?agent ?room1)
             (agent ?agent)
             (room ?room1)
             (room ?room2)
             )
     :post ()
     :pre ()
     :add (
            (in ?agent ?room2)
            )
     :del (
            (in ?agent ?room1)
            )
     :txt (agent ?agent has moved from ?room1 to ?room2)
     }

    }
  )



(def planner-operations-three
  "A map of operations that the agent can perform in the world
  This is the third iteration of the planners ops list. The (in ?agent ?room) has been adjusted
  and moved be (in ?agent ?room1)"
  '{
    move
    {
     :name move-agent
     :achieves (in ?agent ?room2)
     :when (
             (agent ?agent)
             (in ?agent ?room1)
             (room ?room1)
             (room ?room2)
             )
     :post ()
     :pre ()
     :add (
            (in ?agent ?room2)
            )
     :del (
            (in ?agent ?room1)
            )
     :txt (agent ?agent has moved from ?room1 to ?room2)
     }

    }
  )


(def ops-operations
  "Ops search most efficent order"
  '{move { :pre ((agent ?agent)
                  (in ?agent ?room1)
                  (room ?room1)
                  (room ?room2)
                  )
          :add ((in ?agent ?room2))
          :del ((in ?agent ?room1))
          :txt (?agent has moved from ?room1 to ?room2)
          }

    })


;-------------------------------------------------------
;-------------------------------------------------------
;------------------------Tests-------------------------------
;-------------------------------------------------------
;-------------------------------------------------------

(defn test-one []
  "First test for the move operation for the planner.
  Elapsed time: 3.6572 msecs
  "
  (time (planner state-many-objs '(in R J) planner-operations-one))
  )

(defn test-two []
     "First test for the move operation for the planner.
     Elapsed time: 3.405 msecs
     "
     (time (planner state-many-objs '(in R J) planner-operations-two))
     )

(defn test-three []
  "First test for the move operation for the planner.
  Elapsed time: 2.5529 msecs
  "
  (time (planner state-many-objs '(in R J) planner-operations-three))
  )

(defn test-four-ops []
  "Ops search test in the most efficent order, same as planners

  Elapsed time: 5.6284 msecs"
  (time (ops-search state-many-objs '((in R J)) ops-operations))
  )
