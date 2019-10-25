(ns optimal-route.core
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            [clojure.set :refer :all]

            )
  )

;===================================================
; based on: strips-search-1a.clj from SHRDLU model
; naming changes only
;===================================================


;these operators can have all of these slots...
;{ :name put-on
;  :achieves (on ?x ?y)
;  :when   ( (at ?x ?sx) (at ?y ?sy) (:guard (not= (? sx) (? sy))) )
;  :post   ( (protected ?sx) (protected ?sy)
;            (cleartop ?x)
;            (cleartop ?y)
;            (hand empty) )
;  :pre ()
;  :del ( (at ?x ?sx)
;         (cleartop ?y)
;         (protected ?sx)
;         (protected ?sy) )
;  :add ( (at ?x ?sy)
;         (on ?x ?y) )
;  :cmd ( (pick-from ?sx)
;         (drop-at ?sy) )
;  :txt (put ?x on ?y)
;  }
;
;NB: in this example the ops have unique :achieves + :when
;
;They are processed as follows...
;
;goal <- (pop goal-stack)
;match (:achieves op) goal
;  match (:when op) BD
;    push( expand op , goal-stack )
;    push-all( expand (:post op), goal-stack )


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

;-------------------------------------------------------------
;-------------------------------------------------------------
;----------------------------Operations---------------------------------
;-------------------------------------------------------------
;-------------------------------------------------------------

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

(def ops-operations-efficent
  "A map of operations that the agent can perform in the world
  In this ops list the agent will pick up the items in the most efficent order"
  '{
    move { :pre ((agent ?agent)
                 (in ?agent ?room1)
                 (room ?room1)
                 (room ?room2)
                 )
          :add ((in ?agent ?room2))
          :del ((in ?agent ?room1))
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
           ; (holds ?agent ??x)
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

(def ops-operations-inefficent
  "In this ops list the agent wont pick up the objects in the most
  efficent order"
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

;------------------------------------
;------------------------------------
;------------------tests------------------
;------------------------------------
;------------------------------------

(defn planner-logical-order []
  "the agent will pick up the objects in a logical order
  the items are stated in the goal state in a logical efficent order

  The agent will pick up the dog and cat then move and pick up the key"
  (time (planner state '(holds R dog cat key) planner-operations))
  )

(defn planner-illogical-order []
  "Items are added in an inefficent order

  Planner here will move to the key first and pick it upm then move all the way back and pick up the dog and cat"
  (time (planner state '(holds R key dog cat) planner-operations))
  )

(defn ops-logical-order-efficent []
  "The agent uses ops-search and pick up the dog then the cat then the key"
  (time (ops-search state '((holds R dog) (holds R cat) (holds R key)) ops-operations-efficent))
  )

(defn ops-illogical-order-efficent []
  "The agent uses ops-search and pick up the dog then the cat then the key
  even though they are specified in a illogical order"
  (time (ops-search state '( (holds R key) (holds R dog) (holds R cat) ) ops-operations-efficent))
  )

(defn ops-illogical-order-inefficent []
  "The agent uses ops-search and pick up the dog then the cat then the key
  even though they are specified in a illogical order"
  (time (ops-search state '( (holds R key dog cat) ) ops-operations-inefficent))
  )