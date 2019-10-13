(ns tuple-order
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            [clj-memory-meter.core :as mm]

            )
  )

(comment
  "I am going to create multiple sets of operators with the tuples
  and operators in different orders to see if it improves the speed and efficency of the ops-searcher
  "
)

(def operations-one
  "A map of operations that the agent can perform in the world
  This is the first basic development of the operators list with items in any order"
  '{
    open
    {
     :pre
          (
           (agent ?agent)
           (room ?room1)
           (room ?room2)
           (door ?door)
           (opened ?door false)
           (unlocked ?door true)
           (connects ?door ?room1)
           (connects ?door ?room2)
           (in ?agent ?room1)
           )
     :add
          (
           (opened ?door true)
           )
     :del
          (
           (opened ?door false)
           )
     :txt (?agent has opened ?door)
     }

    closed
    {
     :pre
          (
           (agent ?agent)
           (room ?room1)
           (room ?room2)
           (door ?door)
           (opened ?door true)
           (unlocked ?door true)
           (in ?agent ?room1)
           (connects ?door ?room1)
           (connects ?door ?room2)
           )
     :add
          (
           (opened ?door false)
           )
     :del
          (
           (opened ?door true)
           )
     :txt (?agent has closed ?door)
     }

    lock
    {
     :pre
          (
           (agent ?agent)
           (room ?room1)
           (room ?room2)
           (door ?door)
           (opened ?door false)
           (unlocked ?door true)
           (in ?agent ?room1)
           (connects ?door ?room1)


           (key ?key)
           (holdable ?key)
           (unlocks ?key ?door)
           (holds ?agent ?key)
           )
     :add
          (
           (unlocked ?door false)
           )
     :del
          (
           (unlocked ?door true)
           )
     :txt (?agent has locked ?door)
     }

    unlock
    {
     :pre
          (
           (agent ?agent)
           (room ?room1)
           (room ?room2)
           (door ?door)
           (opened ?door false)
           (unlocked ?door false)
           (in ?agent ?room1)
           (connects ?door ?room1)


           (key ?key)
           (holdable ?key)
           (unlocks ?key ?door)
           (holds ?agent ?key)
           )
     :add
          (
           (unlocked ?door true)
           )
     :del
          (
           (unlocked ?door false)
           )
     :txt (?agent has unlocked ?door)
     }

    move
    {
     :pre
          (
           (agent ?agent)
           (room ?room1)
           (room ?room2)
           (door ?door)
           (opened ?door true)
           (unlocked ?door true)
           (connects ?door ?room1)
           (connects ?door ?room2)
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
           (holds ?agent nil)
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

(def operations-two
  "A map of operations that the agent can perform in the world
  Reordered the tuples into different orders.
  I've put the specifiers (agent ?agent) to the bottom and the operators list
  to see if it affects the effects the performance of the search engine"
  '{
    open
    {
     :pre
          (
           (opened ?door false)
           (unlocked ?door true)
           (connects ?door ?room1)
           (connects ?door ?room2)
           (in ?agent ?room1)
           (agent ?agent)
           (room ?room1)
           (room ?room2)
           (door ?door)
           )
     :add
          (
           (opened ?door true)
           )
     :del
          (
           (opened ?door false)
           )
     :txt (?agent has opened ?door)
     }

    closed
    {
     :pre
          (
           (opened ?door true)
           (unlocked ?door true)
           (in ?agent ?room1)
           (connects ?door ?room1)
           (connects ?door ?room2)
           (agent ?agent)
           (room ?room1)
           (room ?room2)
           (door ?door)
           )
     :add
          (
           (opened ?door false)
           )
     :del
          (
           (opened ?door true)
           )
     :txt (?agent has closed ?door)
     }

    lock
    {
     :pre
          (
           (opened ?door false)
           (unlocked ?door true)
           (in ?agent ?room1)
           (connects ?door ?room1)
           (holdable ?key)
           (unlocks ?key ?door)
           (holds ?agent ?key)
           (key ?key)
           (agent ?agent)
           (room ?room1)
           (room ?room2)
           (door ?door)
           )
     :add
          (
           (unlocked ?door false)
           )
     :del
          (
           (unlocked ?door true)
           )
     :txt (?agent has locked ?door)
     }

    unlock
    {
     :pre
          (
           (opened ?door false)
           (unlocked ?door false)
           (in ?agent ?room1)
           (connects ?door ?room1)
           (holdable ?key)
           (unlocks ?key ?door)
           (holds ?agent ?key)
           (key ?key)
           (agent ?agent)
           (room ?room1)
           (room ?room2)
           (door ?door)
           )
     :add
          (
           (unlocked ?door true)
           )
     :del
          (
           (unlocked ?door false)
           )
     :txt (?agent has unlocked ?door)
     }

    move
    {
     :pre
          (

           (opened ?door true)
           (unlocked ?door true)
           (connects ?door ?room1)
           (connects ?door ?room2)
           (in ?agent ?room1)
           (agent ?agent)
           (room ?room1)
           (room ?room2)
           (door ?door)
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
           (in ?agent ?room1)
           (holds ?agent nil)
           (in ?obj ?room1)
           (agent ?agent)
           (room ?room1)
           (holdable ?obj)
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
           (in ?agent ?room1)
           (holds ?agent ?obj)
           (agent ?agent)
           (room ?room1)
           (holdable ?obj)
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


(def intermediate-state-three
  "A more advanced scenario"
  '#{
     (agent R)
     (room A)
     (room B)
     (room C)
     (room D)
     (room E)
     (room F)
     (door A-B)
     (door A-C)
     (door A-D)
     (door B-E)
     (door C-F)
     (connects A-B A)
     (connects A-B B)
     (connects A-C A)
     (connects A-C C)
     (connects A-D A)
     (connects A-D D)
     (connects B-E B)
     (connects B-E E)
     (connects C-F C)
     (connects C-F F)
     (in R A)
     (opened A-B false)
     (opened A-C false)
     (opened A-D false)
     (opened B-E false)
     (opened C-F false)
     (unlocked A-B true)
     (unlocked A-C true)
     (unlocked A-D true)
     (unlocked B-E false)
     (unlocked C-F true)
     (key key-B-E)
     (holdable key-B-E)
     (unlocks key-B-E B-E)
     (holds R nil)
     (in key-B-E B)
     }
  )

(defn total-memory [obj]
  "Helper method to show memory of obj"
  (let [baos (java.io.ByteArrayOutputStream.)]
    (with-open [oos (java.io.ObjectOutputStream. baos)]
      (.writeObject oos obj))
    (count (.toByteArray baos))))

(defn test-one []
  "Test six opens two doors"
  (time (ops-search intermediate-state-three '((opened A-D true)) operations-one  :debug true))
  )

(defn test-two []
  "Test six opens two doors"
  (time (ops-search intermediate-state-three '((opened A-D true)) operations-two  :debug true))
  )