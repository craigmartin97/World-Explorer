(ns tuple-size-individual.core
  (:require
            [clojure.set :refer :all]
            [planner.planner :refer :all]
            [matcher-starter.ops-search-base-ops :refer :all]
            )
  )

;---------------------------------
;---------------------------------
;---------------Planner ops------------------
;---------------------------------
;---------------------------------

(def planner-operations-two
  "A map of operations that the agent can perform in the world"
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
    pickup
    {
     :name pickup-obj
     :achieves (holds ?agent ?obj)
     :when (
            (agent ?agent)
            (room ?room1)
            (holdable ?obj)
            (in ?obj ?room1)
            )
     :post (
            (in ?agent ?room1))
     :pre ()
     :add (
           (holds ?agent ?obj)
           )
     :del (
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

;---------------------------------------
;---------------------------------------
;------------------Ops-search ops---------------------
;---------------------------------------
;---------------------------------------

(def operations-multiple-tuples
  "A map of operations that the agent can perform in the world"
  '{
    move
    {
     :pre
          (
           (agent ?agent)
           (room ?room1)
           (room ?room2)
           (in ?agent ?room1)
           )
     :add
          (
           (in ?agent ?room2)
           )
     :del
          (
           (in ?agent ?room1)
           )
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
           (in ?obj ?room1)
           )
     :add
          (
           (holds ?agent ?obj)
           )
     :del
          (
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


;------------------------------------
;------------------------------------
;-----------------State-------------------
;------------------------------------
;------------------------------------


(def state
     '#{
        (agent R)
        (in R A)
        (holds R)

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
        (room L)
        (room M)
        (room N)
        (room O)
        (room P)
        (room Q)
        (room R)

        (holdable key)
        (in key A)

        (holdable badge)
        (in badge D)

        (holdable gun)
        (in gun F)

        (holdable knife)
        (in knife R)

        (holdable sword)
        (in sword M)

        (holdable shoe)
        (in shoe K)

        (holdable meth)
        (in meth B)

        (holdable coke)
        (in coke C)
        }
     )

(def state-same-room
  '#{
     (agent R)
     (in R A)
     (holds R)

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
     (room L)
     (room M)
     (room N)
     (room O)
     (room P)
     (room Q)
     (room R)

     (holdable key)
     (in key A)

     (holdable badge)
     (in badge A)

     (holdable gun)
     (in gun A)

     (holdable knife)
     (in knife A)

     (holdable sword)
     (in sword A)

     (holdable shoe)
     (in shoe A)

     (holdable meth)
     (in meth A)

     (holdable coke)
     (in coke A)
     }
  )

;----------------------------------------
;----------------------------------------
;-------------------Tests---------------------
;----------------------------------------
;----------------------------------------

(defn planner-test-one []
  "Pickup one item planner

  Elapsed time: 4.0323 msecs"
  (time (planner state '(holds R key) planner-operations-two))
  )

(defn planner-test-two []
  "Planner tries to pick up two items
  This is invalid synatx in this case as the ops list doesnt allow the agent to hold multiple items"
  (time (planner state '(holds R key knife) planner-operations-two))
  )

(defn planner-test-three []
  "Planner tries to pick up two items
  This is invalid synatx in this case as planner doesnt allow multiple items in the goal state"
  (time (planner state '((holds R key) (holds R knife)) planner-operations-two))
  )

;-------------------------------------------
;-------------------------------------------
;--------------------Ops-search multiple tuples-----------------------
;-------------------------------------------
;-------------------------------------------

(defn ops-test-one []
  "Ops search picks up one item
  Elapsed time: 134.9706 msecs"
  (time (ops-search state '((holds R key)) operations-multiple-tuples))
  )

(defn ops-test-one-multi []                                 ;faster
  "Ops search picks up one item
  Elapsed time: 70.296 msecs"
  (time (ops-search state '((holds R key)) ops-search-compare-operations))
  )

;-----------------------------------------------------

(defn ops-test-two []
  "Ops search picks up two items
  Elapsed time: 3703.7924 msecs"
  (time (ops-search state '((holds R key) (holds R knife)) operations-multiple-tuples))
  )

(defn ops-test-two-multi []                                 ;faster
  "Ops search tries to pick up two items
  Elapsed time: 2035.1713 msecs "
  (time (ops-search state '((holds R key knife)) ops-search-compare-operations))
  )

;------------------------------------------------------------------------------

(defn ops-test-three []
  "Planner tries to pick up three items
  Elapsed time: 37347.4663 msecs"
  (time (ops-search state '((holds R key) (holds R knife) (holds R badge)) operations-multiple-tuples))
  )

(defn ops-test-three-multi []                               ;faster
  "Ops search tries too pick up three items
  Elapsed time: 31034.9759 msecs "
  (time (ops-search state '((holds R key knife badge)) ops-search-compare-operations))
  )

;-------------------------------------------------------------------------------

(defn ops-test-five []
  "Planner tries to pick up four items
  Elapsed time: unsolvable took too long "
  (time (ops-search state '((holds R key) (holds R knife) (holds R badge) (holds R gun)) operations-multiple-tuples))
  )

(defn ops-test-five-multi []
  "Ops search tries to pick up four items
  Elapsed time: unsolvable took too long "
  (time (ops-search state '((holds R key knife badge gun)) ops-search-compare-operations))
  )

;-------------------------------------------
;-------------------------------------------
;--------------------Ops-search individual tuples-----------------------
;-------------------------------------------
;-------------------------------------------


(defn ops-test-one-individual-same-room []
  "Ops search tries to pick up four items
  Elapsed time: Unable to solve stack overflow exception "
  (time (ops-search state-same-room '((holds R key knife badge gun sword)) ops-search-compare-operations))
  )

(defn ops-test-one-multi-same-room []
  "Ops search tries to pick up 5 items
  Elapsed time: Unable to solve stack overflow exception "
  (time (ops-search state-same-room '((holds R key) (holds R knife) (holds R badge) (holds R gun) (holds R sword)) operations-multiple-tuples))
  )

;-----------------------------------------------------------------

(defn ops-test-two-multi-same-room []
  "Ops search tries to pick up 5 items
  Elapsed time: 2371.095 msecs "
  (time (ops-search state-same-room '((holds R key) (holds R knife)) operations-multiple-tuples))
  )

(defn ops-test-two-individual-same-room []                  ;faster
  "Ops search tries to pick up four items
  Elapsed time: 1283.1548 msecs "
  (time (ops-search state-same-room '((holds R key knife)) ops-search-compare-operations))
  )

;--------------------------------------------------------------------

(defn ops-test-three-multi-same-room []
  "Ops search tries to pick up 5 items
  Elapsed time: 12153.3155 msecs "
  (time (ops-search state-same-room '((holds R key) (holds R knife) (holds R gun)) operations-multiple-tuples))
  )

(defn ops-test-three-individual-same-room []                ;faster
  "Ops search tries to pick up four items
  Elapsed time: 7735.1829 msecs "
  (time (ops-search state-same-room '((holds R key knife gun)) ops-search-compare-operations))
  )

;----------------------------------------------------------------------
(defn ops-test-four-multi-same-room []                      ;faster
  "Ops search tries to pick up 5 items
  Elapsed time: 27898.826 msecs "
  (time (ops-search state-same-room '((holds R key) (holds R knife) (holds R badge) (holds R gun)) operations-multiple-tuples))
  )

(defn ops-test-four-individual-same-room []
  "Ops search tries to pick up four items
  Elapsed time: 53244.2923 msecs "
  (time (ops-search state-same-room '((holds R key knife badge gun)) ops-search-compare-operations))
  )



