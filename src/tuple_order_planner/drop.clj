(ns tuple-order-planner.drop
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            [clojure.set :refer :all]
            [planner.planner :refer :all]
            [tuple-order-planner.state :refer :all]
            [tuple-order_ops.drop :refer :all]
            )
  )

(def planner-operations-v1
  "A map of operations that the agent can perform in the world"
  '{
    drop-obj
    {:name     drop-obj
     :achieves (in ?obj ?room1)
     :when     ((agent ?agent)
                 (holdable ?obj)
                 (room ?room1)
                 )
     :post     ((holds ?agent ?obj)
                 (in ?agent ?room1)
                 )
     :pre      ()
     :add      ((in ?obj ?room1)
                 )
     :del      ((holds ?agent ?obj)
                 )
     :txt      (?agent dropped ?obj in ?room1)
     }

    }
  )

(def planner-operations-v2
  "A map of operations that the agent can perform in the world"
  '{
    drop-obj
    {:name     drop-obj
     :achieves (in ?obj ?room1)
     :when     ((holdable ?obj)
                 (agent ?agent)
                 (room ?room1)
                 )
     :post     ((holds ?agent ?obj)
                 (in ?agent ?room1)
                 )
     :pre      ()
     :add      ((in ?obj ?room1)
                 )
     :del      ((holds ?agent ?obj)
                 )
     :txt      (?agent dropped ?obj in ?room1)
     }

    }
  )

(def planner-operations-v3
  "A map of operations that the agent can perform in the world"
  '{
    drop-obj
    {:name     drop-obj
     :achieves (in ?obj ?room1)
     :when     ((holdable ?obj)
                 (room ?room1)
                 (agent ?agent)
                 )
     :post     ((holds ?agent ?obj)
                 (in ?agent ?room1)
                 )
     :pre      ()
     :add      ((in ?obj ?room1)
                 )
     :del      ((holds ?agent ?obj)
                 )
     :txt      (?agent dropped ?obj in ?room1)
     }

    }
  )

(def planner-operations-v4
  "A map of operations that the agent can perform in the world"
  '{
    drop-obj
    {:name     drop-obj
     :achieves (in ?obj ?room1)
     :when     ((holdable ?obj)
                 (room ?room1)
                 (agent ?agent)
                 )
     :post     ((in ?agent ?room1)
                 (holds ?agent ?obj)
                 )
     :pre      ()
     :add      ((in ?obj ?room1)
                 )
     :del      ((holds ?agent ?obj)
                 )
     :txt      (?agent dropped ?obj in ?room1)
     }

    }
  )

;---------------------------------------------------------------
;---------------------------------------------------------------
;------------------------------Tests when the agent doesnt hold the item---------------------------------
;---------------------------------------------------------------
;---------------------------------------------------------------

;(time (planner state-many-objs '(in key A) planner-operations))

(defn test-one []
  "Agent moves the dog to room F using operations 1

  Planner is broken for this test as its impossible for it to drop
  the dog in room F as there is no move op specified but planner is
  still saying its possible

  Elapsed time: 7.9439 msecs"
  (time (planner state-many-objs '(in dog F) planner-operations-v1))
  )

(defn test-two []
  "Agent moves the dog to room F using operations 2

  Planner is broken for this test as its impossible for it to drop
  the dog in room F as there is no move op specified but planner is
  still saying its possible

  Elapsed time: 8.1238 msecs"
  (time (planner state-many-objs '(in dog F) planner-operations-v2))
  )

(defn test-three []
  "Agent moves the dog to room F using operations 3

  Planner is broken for this test as its impossible for it to drop
  the dog in room F as there is no move op specified but planner is
  still saying its possible

  Elapsed time: 7.3365 msecs"
  (time (planner state-many-objs '(in dog F) planner-operations-v3))
  )

(defn test-four []
  "Agent moves the dog to room F using operations 4

  Planner is broken for this test as its impossible for it to drop
  the dog in room F as there is no move op specified but planner is
  still saying its possible

  Elapsed time: 7.1532 msecs"
  (time (planner state-many-objs '(in dog F) planner-operations-v4))
  )

;--------------------------------------------------------------------
;--------------------------------------------------------------------
;------------------------------Tests when the agent holds the item--------------------------------------
;--------------------------------------------------------------------
;--------------------------------------------------------------------

(defn ops-test-one []
  "Agent moves the dog to room F using operations 1

  Ops search has correctly determined that this is impossible
  as it can't move room to drop the item"
  (time (ops-search state-many-objs '((in dog F)) op-drop-four))
  )