(ns guard-testing.throw
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]))

(comment "Guard that room 1 and room 2 aren't the same room.")

(def op-throw-base
  '{throw {:pre ((agent ?agent)
                 (room ?room1)
                 (room ?room2)
                 (door ?door)
                 (holdable ?obj)
                 (in ?agent ?room1)
                 (holds ?agent ?obj)
                 (connects ?door ?room1)
                 (connects ?door ?room2)
                 (unlocked ?door true)
                 (opened ?door true))
           :add ((in ?obj ?room2))
           :del ((holds ?agent ?obj))
           :txt ((?agent throws the ?obj into ?room2))
           }
      }
  )

(def op-throw-guarded-one
  '{throw {:pre ((agent ?agent)
                 (room ?room1)
                 (room ?room2)
                 (door ?door)
                 (holdable ?obj)
                 (in ?agent ?room1)
                 (holds ?agent ?obj)
                 (connects ?door ?room1)
                 (connects ?door ?room2)
                 (unlocked ?door true)
                 (opened ?door true)
                 (:guard (not= (? room1) (? room2)))
                 )
           :add ((in ?obj ?room2))
           :del ((holds ?agent ?obj))
           :txt ((?agent throws the ?obj into ?room2))
           }
    }
)

(def op-throw-guarded-two
  '{throw {:pre ((agent ?agent)
                 (room ?room1)
                 (room ?room2)
                 (:guard (not= (? room1) (? room2)))
                 (door ?door)
                 (holdable ?obj)
                 (in ?agent ?room1)
                 (holds ?agent ?obj)
                 (connects ?door ?room1)
                 (connects ?door ?room2)
                 (unlocked ?door true)
                 (opened ?door true)
                 )
           :add ((in ?obj ?room2))
           :del ((holds ?agent ?obj))
           :txt ((?agent throws the ?obj into ?room2))
           }
    }
)



(def state-throw-small
  "basic state for agent to move from one room to next"
  '#{
     (agent R)

     (room A)
     (room B)

     (door A-B)

     (connects A-B A)
     (connects A-B B)

     (opened A-B true)
     (unlocked A-B true)

     (in R A)

     (holdable key)
     (holds R key)
     }
  )

(def state-throw-medium
  "basic state for agent to move from one room to next"
  '#{
     (agent R)

     (room A)
     (room B)

     (door A-B)

     (connects A-B A)
     (connects A-B B)

     (opened A-B true)
     (unlocked A-B true)

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
     }
  )

(def state-throw-large
  "basic state for agent to move from one room to next"
  '#{(agent R)

     (room A)
     (room B)

     (door A-B)

     (connects A-B A)
     (connects A-B B)

     (opened A-B true)
     (unlocked A-B true)

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
     }
  )



(defn test-throw-base-small []
  (time (ops-search state-throw-small '((in key B)) op-throw-base))
  )

(defn test-throw-base-medium []
  (time (ops-search state-throw-medium '((in key B)(in lever B)(in rock B)(in sledgehammer B)(in chocolate B)) op-throw-base))
  )

(defn test-throw-base-large []
  (time (ops-search state-throw-large '((in key B)(in lever B)(in rock B)(in sledgehammer B)(in chocolate B)(in phone B)(in ladder B)(in knife B)(in torch B)(in journal B)) op-throw-base))
  )


(defn test-throw-guarded-one-small []
  (time (ops-search state-throw-small '((in key B)) op-throw-guarded-one))
  )

(defn test-throw-guarded-one-medium []
  (time (ops-search state-throw-medium '((in key B)(in lever B)(in rock B)(in sledgehammer B)(in chocolate B)) op-throw-guarded-one))
  )

(defn test-throw-guarded-one-large []
  (time (ops-search state-throw-large '((in key B)(in lever B)(in rock B)(in sledgehammer B)(in chocolate B)(in phone B)(in ladder B)(in knife B)(in torch B)(in journal B)) op-throw-guarded-one))
  )


(defn test-throw-guarded-two-small []
  (time (ops-search state-throw-small '((in key B)) op-throw-guarded-two))
  )

(defn test-throw-guarded-two-medium []
  (time (ops-search state-throw-medium '((in key B)(in lever B)(in rock B)(in sledgehammer B)(in chocolate B)) op-throw-guarded-two))
  )

(defn test-throw-guarded-two-large []
  (time (ops-search state-throw-large '((in key B)(in lever B)(in rock B)(in sledgehammer B)(in chocolate B)(in phone B)(in ladder B)(in knife B)(in torch B)(in journal B)) op-throw-guarded-two))
  )