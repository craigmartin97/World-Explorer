;--------------------------------------------
;--------------------------------------------
;------------------Planner--------------------------
;--------------------------------------------
;--------------------------------------------

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

;---------------------------------------------------------------------
;---------------------------------------------------------------------
;------------------------------planner implementation---------------------------------------
;---------------------------------------------------------------------
;---------------------------------------------------------------------


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


;--------------------------------------------------------------------
;--------------------------------------------------------------------
;-------------------------------Opssearch implementation-------------------------------------
;--------------------------------------------------------------------
;--------------------------------------------------------------------

(def ops-search-operations
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
            :del ((holds ?agent ??x))
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

;---------------------------------------------------------------
;---------------------------------------------------------------
;--------------------------Small State-------------------------------------
;---------------------------------------------------------------
;---------------------------------------------------------------

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

;---------------------------------------------------------------------------
;---------------------------------------------------------------------------
;--------------------------------------Tests for ops-search-------------------------------------
;--------------------------------------------Basic move-------------------------------
;---------------------------------------------------------------------------

(defn ops-move-to-c []
  "Elapsed time:17.0598 msecs"
  (time (ops-search state '((in R C)) ops-search-operations))
  )

(defn ops-move-to-k []
  "Elapsed time:32.6117 msecs"
  (time (ops-search state '((in R K)) ops-search-operations))
  )

(defn planner-move-to-c []
  "Elapsed time:3.0696 msecs"
  (time (planner state '(in R C) planner-operations))
  )

(defn planner-move-to-k []
  "Elapsed time:12.1147 msecs"
  (time (planner state '(in R K) planner-operations))
  )