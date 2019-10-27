(ns tuple-order-planner.drop
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            [clojure.set :refer :all]
            [planner.planner :refer :all]
            [tuple-order-planner.state :refer :all]
            )
  )

;-------------------------------------------------------------
;-------------------------------------------------------------
;---------------------------Operations----------------------------------
;-------------------------------------------------------------
;-------------------------------------------------------------

(def planner-operations-v1
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
     :post (
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

(def planner-operations-v2
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
     :post (
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
             (holdable ?obj)
             (agent ?agent)
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

(def planner-operations-v3
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
     :post (
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
             (holdable ?obj)
             (room ?room1)
             (agent ?agent)
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

(def planner-operations-v4
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
     :post (
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
             (holdable ?obj)
             (room ?room1)
             (agent ?agent)
             )
     :post (
             (in ?agent ?room1)
             (holds ?agent ?obj)
             )
     :pre ()
     :add ((in ?obj ?room1))
     :del ((holds ?agent ?obj))
     :txt (?agent dropped ?obj in ?room1)
     }

    }
  )


;---------------------------------------------------------------
;---------------------------------------------------------------
;------------------------------Tests---------------------------------
;---------------------------------------------------------------
;---------------------------------------------------------------

;(time (planner state-many-objs '(in key A) planner-operations))

(defn test-one []
  "Agent moves the dog to room F using operations 1"
  (time (planner state-many-objs '(in dog F) planner-operations-v1))
  )

(defn test-two []
  "Agent moves the dog to room F using operations 1"
  (time (planner state-many-objs '(in dog F) planner-operations-v2))
  )

(defn test-three []
  "Agent moves the dog to room F using operations 1"
  (time (planner state-many-objs '(in dog F) planner-operations-v3))
  )

(defn test-four []
  "Agent moves the dog to room F using operations 1"
  (time (planner state-many-objs '(in dog F) planner-operations-v4))
  )
