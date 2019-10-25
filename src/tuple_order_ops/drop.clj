(ns tuple-order_ops.drop
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            )
  )

(def op-drop-one
  '{drop {:pre ((agent ?agent)
                 (room ?room1)
                 (holdable ?obj)
                 (in ?agent ?room1)
                 (holds ?agent ?obj)
                 )
          :add ((in ?obj ?room1))
          :del ((holds ?agent ?obj))
          :txt (?agent dropped ?obj in ?room1)
          }
    }
  )

(def op-drop-two
  '{drop {:pre ((agent ?agent)
                 (in ?agent ?room1)
                 (room ?room1)
                 (holds ?agent ?obj)
                 (holdable ?obj)
                 )
          :add ((in ?obj ?room1))
          :del ((holds ?agent ?obj))
          :txt (?agent dropped ?obj in ?room1)
          }
    }
  )

(def op-drop-three
  '{drop {:pre ((in ?agent ?room1)
                 (holds ?agent ?obj)
                 (agent ?agent)
                 (room ?room1)
                 (holdable ?obj)
                 )
          :add ((in ?obj ?room1))
          :del ((holds ?agent ?obj))
          :txt (?agent dropped ?obj in ?room1)
          }
    }
  )

(def op-drop-four
  '{drop {:pre ((holds ?agent ?obj)
                 (in ?agent ?room1)
                 (agent ?agent)
                 (holdable ?obj)
                 (room ?room1)
                 )
          :add ((in ?obj ?room1))
          :del ((holds ?agent ?obj))
          :txt (?agent dropped ?obj in ?room1)
          }
    }
  )


(def state-drop-small
  '#{(agent R)
     (room A)
     (in R A)

     (holdable key)
     (holds R key)
     })

(def state-drop-medium
  '#{(agent R)
     (room A)
     (in R A)

     (holdable key)
     (holdable lever)
     (holdable rock)
     (holdable sledgehammer)
     (holdable chocolate)
     (holds R key)
     (holds R lever)
     (holds R rock)
     (holds R sledgehammer)
     (holds R chocolate)
     })

(def state-drop-large
  '#{(agent R)
     (room A)
     (in R A)

     (holdable key)
     (holdable lever)
     (holdable rock)
     (holdable sledgehammer)
     (holdable chocolate)
     (holdable phone)
     (holdable ladder)
     (holdable knife)
     (holdable torch)
     (holdable journal)
     (holds R key)
     (holds R lever)
     (holds R rock)
     (holds R sledgehammer)
     (holds R chocolate)
     (holds R phone)
     (holds R ladder)
     (holds R knife)
     (holds R torch)
     (holds R journal)
     })


(defn test-drop-one-small []
  (time (ops-search state-drop-small '((in key A)) op-drop-one))
  )

(defn test-drop-one-medium []
  (time (ops-search state-drop-medium '((in key A)(in lever A)(in rock A)(in sledgehammer A)(in chocolate A)) op-drop-one))
  )

(defn test-drop-one-large []
  (time (ops-search state-drop-large '((in key A)(in lever A)(in rock A)(in sledgehammer A)(in chocolate A)(in phone A)(in ladder A)(in knife A)(in torch A)(in journal A)) op-drop-one))
  )


(defn test-drop-two-small []
  (time (ops-search state-drop-small '((in key A)) op-drop-two))
  )

(defn test-drop-two-medium []
  (time (ops-search state-drop-medium '((in key A)(in lever A)(in rock A)(in sledgehammer A)(in chocolate A)) op-drop-two))
  )

(defn test-drop-two-large []
  (time (ops-search state-drop-large '((in key A)(in lever A)(in rock A)(in sledgehammer A)(in chocolate A)(in phone A)(in ladder A)(in knife A)(in torch A)(in journal A)) op-drop-two))
  )


(defn test-drop-three-small []
  (time (ops-search state-drop-small '((in key A)) op-drop-three))
  )

(defn test-drop-three-medium []
  (time (ops-search state-drop-medium '((in key A)(in lever A)(in rock A)(in sledgehammer A)(in chocolate A)) op-drop-three))
  )

(defn test-drop-three-large []
  (time (ops-search state-drop-large '((in key A)(in lever A)(in rock A)(in sledgehammer A)(in chocolate A)(in phone A)(in ladder A)(in knife A)(in torch A)(in journal A)) op-drop-three))
  )


(defn test-drop-four-small []
  (time (ops-search state-drop-small '((in key A)) op-drop-four))
  )

(defn test-drop-four-medium []
  (time (ops-search state-drop-medium '((in key A)(in lever A)(in rock A)(in sledgehammer A)(in chocolate A)) op-drop-four))
  )

(defn test-drop-four-large []
  (time (ops-search state-drop-large '((in key A)(in lever A)(in rock A)(in sledgehammer A)(in chocolate A)(in phone A)(in ladder A)(in knife A)(in torch A)(in journal A)) op-drop-four))
  )