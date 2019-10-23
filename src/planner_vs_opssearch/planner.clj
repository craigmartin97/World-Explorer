(ns planner_vs_opssearch.planner
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            [clojure.set :refer :all]

            )
  )


(comment
  drop
  {
   :name drop-obj
   :achieves (drops ?agent ?obj)
   :when (
           (in ?agent ?room1)
           (agent ?agent)
           (holdable ?obj)
           (room ?room1)
           )
   :post (

           (holds ?agent ?obj)
           )
   :pre ()
   :add ((in ?obj ?room1))
   :del ((holds ?agent ?obj))
   :txt (?agent dropped ?obj in ?room1)
   }


  open
  {
   :name open
   :achieves (opened ?door true)
   :when (
           (agent ?agent)
           (connects ?door ?room1)
           (door ?door)
           (opened ?door false)
           (unlocked ?door true)
           (room ?room1)
           (room ?room2)
           (in ?agent ?room1)
           )
   :post (

           )
   :pre ()
   :add (
          (opened ?door true)
          )
   :del (
          (opened ?door false)
          )
   :txt (?agent has opened ?door)

   }


  (door doorA)
  (door doorB)
  (door doorC)
  (door doorD)
  (door doorE)
  (door doorF)
  (door doorG)
  (door doorH)
  (door doorI)
  (door doorJ)
  (door doorK)

  (connects doorA A)
  (connects doorB B)
  (connects doorC C)
  (connects doorD D)
  (connects doorE E)
  (connects doorF F)
  (connects doorG G)
  (connects doorH H)
  (connects doorI I)
  (connects doorJ J)
  (connects doorK K)


  ;(opened doorA false)
  (opened doorA true)
  (opened doorB true)
  (opened doorC true)
  (opened doorD true)
  (opened doorE true)
  (opened doorF true)
  (opened doorG true)
  (opened doorH true)
  (opened doorI true)
  (opened doorJ true)
  (opened doorK true)

  (unlocked doorA true)
  (unlocked doorB true)
  (unlocked doorC true)
  (unlocked doorD true)
  (unlocked doorE true)
  (unlocked doorF true)
  (unlocked doorG true)
  (unlocked doorH true)
  (unlocked doorI true)
  (unlocked doorJ true)
  (unlocked doorK true)
  )
;----------------------------------------------------------
(comment

  "To add in below"

  WITH PROTECT Operations
  "This operator is our attempt at solving the dead end problem, but is a start in the
  right direction. This ensures that the agent doesnt do the same thing again."
  (def operations
    "A map of operations that the agent can perform in the world"
    '{
      protect
      {
       :name protect-obj
       :achieves (protect ?obj)
       :when ((protected ??picked))
       :add ((protected ??picked ?obj))
       :del ((protected ??picked))
       }
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
               (:guard (not= (? room1) (? room2)))
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
               (in ?obj ?room1)
               (holdable ?obj)
               (agent ?agent)
               (room ?room1)
               (protected ??picked)
               (:guard (not (some #(= % (? obj)) (? picked))))
               (:guard (not (println "THE OBJECT " (? obj) " IS IN ROOM " (? room1))))
               )
       :post ((protect ?obj)
               (holds ?agent ??x)
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
      move-obj
      {
       :name move-obj
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



  WITH GUARD operations
  "The guard verifies that we dont do the same room twice."
  (def operations
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
               (:guard (not= (? room1) (? room2)))
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
               (in ?obj ?room1)
               (holdable ?obj)
               (agent ?agent)
               (room ?room1)
               (:guard (not (some #(= % (? obj)) (? x))))
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
      move-obj
      {
       :name move-obj
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

)

(def operations
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
     (holds R nil)

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




;-------------------------------------------------------------------
;-------------------------------------------------------------------
;--------------------------------tests-----------------------------------
;-------------------------------------------------------------------
;-------------------------------------------------------------------


(defn move-to-d []
  "Making the agent move to D"
  (time (planner state '(in R D) operations))
  )

(defn move-to-j []
  "Making the agent move to J"
  (time (planner state '(in R J) operations))
  )

(defn pick-up-dog []
  "Agent picks up the dog which is in the same room"
  (time (planner state '(holds R dog) operations))
  )

(defn pickup-key []
  "Agent moves to the correct room where the key is and then picks it up"
  (time (planner state '(holds R key) operations))
  )

(defn drop-key []
  "Agent moves to the correct room where the key is and then picks it up, and then drops is again"
  (time (planner state '(in key E) operations))
  )

(defn drop-dog []
  "Agent will pick up the dog and then drop the dog in the same room"
  (time (planner state '(in dog F) operations))
  )

(defn move-key []
  "Agent moves to the correct room, picks up the key and moves it to another room"
  (time (planner state '(in key B) operations))
  )

(defn open-door []
  "Agent opens a door"
  (time (planner state '(opened doorA true) operations))
  )

(defn open-door-and-move []
  "Agent opens a door and moves to another room"
  (time (planner state '(in R C) operations))
  )

(defn open-door-move-and-move-obj []
  "Open a door, move and move an object"
  (time (planner state '(in dog C) operations))
  )
