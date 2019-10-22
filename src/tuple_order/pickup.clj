(ns tuple-order.pickup
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            )
  )

(def operation-pickup-one
  '{
    pickup
    {
     :pre
          (
            (agent ?agent)
            (room ?room1)
            (holdable ?obj)
            (in ?agent ?room1)
            (holds ?agent ??x)
            (in ?obj ?room1)
            )
     :add
          (
            (holds ?agent ??x ?obj)
            )
     :del
          (
            (holds ?agent ??x)
            (in ?obj ?room1)
            )
     :txt (?agent picked up ?obj from ?room1)
     }
   })

(def operation-pickup-two
  '{
    pickup
    {
     :pre
          (
            (agent ?agent)
            (in ?agent ?room1)
            (room ?room1)
            (holdable ?obj)
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
    })

(def operation-pickup-three
  '{
    pickup
    {
     :pre
          (
            (agent ?agent)
            (in ?agent ?room1)
            (room ?room1)
            (holds ?agent nil)
            (holdable ?obj)
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
    })

(def operation-pickup-four
  '{
    pickup
    {
     :pre
          (
            (agent ?agent)
            (in ?agent ?room1)
            (room ?room1)
            (in ?obj ?room1)
            (holds ?agent ?obj)
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
    })

(def small-state
  '#{
     (agent R)
     (room A)
     (in R A)
     (holds R nil)

     (in key A)
     (holdable key)

    }
  )

(def medium-state
  '#{
     (agent R)
     (room A)
     (in R A)
     (holds R nil)


     (key key-A-B)
     (key key-A-C)
     (key key-A-D)
     (key key-C-D)
     (key key-C-E)
     (key key-D-E)


     (holdable key-A-B)
     (holdable key-A-C)
     (holdable key-A-D)
     (holdable key-C-D)
     (holdable key-C-E)
     (holdable key-D-E)


     (in key-A-B A)
     (in key-A-C A)
     (in key-A-D A)
     (in key-C-D A)
     (in key-C-E A)
     (in key-D-E A)


     }
  )

(def large-state
  '#{
     (agent R)
     (room A)
     (in R A)
     (holds R nil)

     (key key-A-B)
     (key key-A-C)
     (key key-A-D)
     (key key-C-D)
     (key key-C-E)
     (key key-D-E)
     (key key-D-J)
     (key key-D-F)
     (key key-B-F)
     (key key-D-G)
     (key key-F-G)
     (key key-E-H)
     (key key-G-H)
     (key key-G-I)
     (key key-I-J)
     (key key-H-J)

     (holdable key-A-B)
     (holdable key-A-C)
     (holdable key-A-D)
     (holdable key-C-D)
     (holdable key-C-E)
     (holdable key-D-E)
     (holdable key-D-J)
     (holdable key-D-F)
     (holdable key-B-F)
     (holdable key-D-G)
     (holdable key-F-G)
     (holdable key-E-H)
     (holdable key-G-H)
     (holdable key-G-I)
     (holdable key-I-J)
     (holdable key-H-J)

     (in key-A-B A)
     (in key-A-C A)
     (in key-A-D A)
     (in key-C-D A)
     (in key-C-E A)
     (in key-D-E A)
     (in key-D-J A)
     (in key-D-F A)
     (in key-B-F A)
     (in key-D-G A)
     (in key-F-G A)
     (in key-E-H A)
     (in key-G-H A)
     (in key-G-I A)
     (in key-I-J A)
     (in key-H-J A)
     }
  )

(defn test-small-one []
  "The agent picks up a single key
  Elapsed Time: 5.8 msecs"
  (time (ops-search small-state '((holds R key)) operation-pickup-one :debug true))
  )

(defn test-small-two []
  "The agent picks up a single key
  Elapsed Time: 5.8 msecs"
  (time (ops-search small-state '((holds R key)) operation-pickup-two :debug true))
  )

(defn test-small-three []
  "The agent picks up a single key
  Elapsed Time: 5.8 msecs"
  (time (ops-search small-state '((holds R key)) operation-pickup-three :debug true))
  )

(defn test-small-four []
  "The agent picks up a single key
  Elapsed Time: 5.8 msecs"
  (time (ops-search small-state '((holds R key)) operation-pickup-four :debug true))
  )



;---------------------------------------------------------
;---------------------------------------------------------
;---------------------------------------------------------
;---------------------------------------------------------
;---------------------------------------------------------

(defn test-medium-one []
  "The agent picks up a single key
  Elapsed Time: 5.8 msecs"
  (time (ops-search medium-state '((holds R key-A-B)) operation-pickup-one :debug true))
  )

(defn test-medium-two []
  "The agent picks up a single key
  Elapsed Time: 5.8 msecs"
  (time (ops-search medium-state '((holds R key) (holds R sledgehammer) (holds R knife) (holds R rock) (holds R lever) (holds R torch)) operation-pickup-two :debug true))
  )

(defn test-medium-three []
  "The agent picks up a single key
  Elapsed Time: 5.8 msecs"
  (time (ops-search medium-state '((holds R key) (holds R sledgehammer) (holds R knife) (holds R rock) (holds R lever) (holds R torch)) operation-pickup-three :debug true))
  )

(defn test-medium-four []
  "The agent picks up a single key
  Elapsed Time: 5.8 msecs"
  (time (ops-search medium-state '((holds R key) (holds R sledgehammer) (holds R knife) (holds R rock) (holds R lever) (holds R torch)) operation-pickup-four :debug true))
  )

;---------------------------------------------------------
;---------------------------------------------------------
;---------------------------------------------------------
;---------------------------------------------------------
;---------------------------------------------------------

(defn test-large-one []
  "The agent picks up a single key
  Elapsed Time: 5.8 msecs"
  (time (ops-search large-state '((holds R key-A-B)) operation-pickup-one :debug true))
  )

(defn test-large-two []
  "The agent picks up a single key
  Elapsed Time: 5.8 msecs"
  (time (ops-search large-state '((holds R key)) operation-pickup-two :debug true))
  )

(defn test-large-three []
  "The agent picks up a single key
  Elapsed Time: 5.8 msecs"
  (time (ops-search large-state '((holds R key)) operation-pickup-three :debug true))
  )

(defn test-large-four []
  "The agent picks up a single key
  Elapsed Time: 5.8 msecs"
  (time (ops-search large-state '((holds R key)) operation-pickup-four :debug true))
  )