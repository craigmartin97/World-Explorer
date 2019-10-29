(ns tuple-order-planner.pickup
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            [clojure.set :refer :all]
            [planner.planner :refer :all]
            [tuple-order-planner.state :refer :all]
            [tuple-order_ops.pickup :refer :all]
            )
  )

;--------------------------------------------------
;--------------------------------------------------
;----------------------Operations----------------------------
;--------------------------------------------------
;--------------------------------------------------

(def planner-operations-v1
  "A map of operations that the agent can perform in the world"
  '{
    pickup
    {:name     pickup-obj
     :achieves (holds ?agent ??x ?obj)
     :when     ((agent ?agent)
                 (room ?room1)
                 (holdable ?obj)
                 (in ?obj ?room1)
                 )
     :post     ((holds ?agent ??x)
                 (in ?agent ?room1)
                 )
     :pre      ()
     :add      ((holds ?agent ??x ?obj)
                 )
     :del      ((holds ?agent ??x)
                 (in ?obj ?room1)
                 )
     :txt      (?agent picked up ?obj from ?room1)

     }

    }
  )

(def planner-operations-v2
  "A map of operations that the agent can perform in the world"
  '{
    pickup
    {:name     pickup-obj
     :achieves (holds ?agent ??x ?obj)
     :when     ((room ?room1)
                 (agent ?agent)
                 (holdable ?obj)
                 (in ?obj ?room1)
                 )
     :post     ((holds ?agent ??x)
                 (in ?agent ?room1)
                 )
     :pre      ()
     :add      ((holds ?agent ??x ?obj)
                 )
     :del      ((holds ?agent ??x)
                 (in ?obj ?room1)
                 )
     :txt      (?agent picked up ?obj from ?room1)

     }

    }
  )

(def planner-operations-v3
  "A map of operations that the agent can perform in the world"
  '{
    pickup
    {
     :name     pickup-obj
     :achieves (holds ?agent ??x ?obj)
     :when     (
                 (holdable ?obj)
                 (room ?room1)
                 (agent ?agent)
                 (in ?obj ?room1)
                 )
     :post     ((holds ?agent ??x)
                 (in ?agent ?room1))
     :pre      ()
     :add      (
                 (holds ?agent ??x ?obj)
                 ;(holds ?agent ?obj)
                 )
     :del      (
                 (holds ?agent ??x)
                 (in ?obj ?room1)
                 )
     :txt      (?agent picked up ?obj from ?room1)

     }

    }
  )

(def planner-operations-v4
  "A map of operations that the agent can perform in the world"
  '{
    pickup
    {:name     pickup-obj
     :achieves (holds ?agent ??x ?obj)
     :when     ((room ?room1)
                 (holdable ?obj)
                 (agent ?agent)
                 (in ?obj ?room1)
                 )
     :post     ((holds ?agent ??x)
                 (in ?agent ?room1)
                 )
     :pre      ()
     :add      ((holds ?agent ??x ?obj)
                 )
     :del      ((holds ?agent ??x)
                 (in ?obj ?room1)
                 )
     :txt      (?agent picked up ?obj from ?room1)

     }

    }
  )

(def planner-operations-v5
  "A map of operations that the agent can perform in the world"
  '{
    pickup
    {:name     pickup-obj
     :achieves (holds ?agent ??x ?obj)
     :when     ((room ?room1)
                 (holdable ?obj)
                 (in ?obj ?room1)
                 (agent ?agent)
                 )
     :post     ((holds ?agent ??x)
                 (in ?agent ?room1)
                 )
     :pre      ()
     :add      ((holds ?agent ??x ?obj)
                 )
     :del      ((holds ?agent ??x)
                 (in ?obj ?room1)
                 )
     :txt      (?agent picked up ?obj from ?room1)

     }

    }
  )

;---------------------------------------------------------------
;---------------------------------------------------------------
;------------------------------Tests for the agent to pick up key in other room---------------------------------
;---------------------------------------------------------------
;---------------------------------------------------------------

;(time (planner state-many-objs '(in key A) planner-operations))

(defn planner-test-one []
  "Agent moves the dog to room F using operations 1

  Planner is broken for this test as its impossible for it to pickup
  the key in room D as there is no move op specified but planner is
  still saying its possible

  Elapsed time: 3.2348 msecs"
  (time (planner state-many-objs '(holds R key) planner-operations-v1))
  )

(defn planner-test-two []
  "Agent moves the dog to room F using operations 2

  Planner is broken for this test as its impossible for it to pickup
  the key in room D as there is no move op specified but planner is
  still saying its possible

  Elapsed time: 3.6274 msecs"
  (time (planner state-many-objs '(holds R key) planner-operations-v2))
  )

(defn planner-test-three []
  "Agent moves the dog to room F using operations 3

  Planner is broken for this test as its impossible for it to pickup
  the key in room D as there is no move op specified but planner is
  still saying its possible

  Elapsed time: 4.0487 msecs"
  (time (planner state-many-objs '(holds R key) planner-operations-v3))
  )

(defn planner-test-four []
  "Agent moves the dog to room F using operations 4

  Planner is broken for this test as its impossible for it to pickup
  the key in room D as there is no move op specified but planner is
  still saying its possible

  Elapsed time: 3.5741 msecs"
  (time (planner state-many-objs '(holds R key) planner-operations-v4))
  )

;---------------------------------------------------------------
;---------------------------------------------------------------
;------------------------------Tests for the agent to pick up in same room---------------------------------
;---------------------------------------------------------------
;---------------------------------------------------------------

;(time (planner state-many-objs '(in key A) planner-operations))

(defn planner-test-one-dog []
  "Agent moves the dog to room F using operations 1

  Elapsed time: 5.078 msecs"
  (time (planner state-many-objs '(holds R dog) planner-operations-v1))
  )

(defn planner-test-two-dog []
  "Agent moves the dog to room F using operations 2

  Elapsed time: 6.9205 msecs"
  (time (planner state-many-objs '(holds R dog) planner-operations-v2))
  )

(defn planner-test-three-dog []
  "Agent moves the dog to room F using operations 3

  Elapsed time: 4.6035 msecs"
  (time (planner state-many-objs '(holds R dog) planner-operations-v3))
  )

(defn planner-test-four-dog []
  "Agent moves the dog to room F using operations 4

  Elapsed time: 5.4198 msecs"
  (time (planner state-many-objs '(holds R dog) planner-operations-v4))
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
  (time (ops-search state-many-objs '((holds R key)) operations-four))
  )

(defn ops-test-two []
  "Agent moves the dog to room F using operations 1

  Elapsed time: 8.8274 msecs"
  (time (ops-search state-many-objs '((holds R dog)) operations-four))
  )

;;(time (planner state-many-objs '(in key A) planner-operations))