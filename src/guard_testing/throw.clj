(ns guard-testing.throw
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]))

(comment "Tests to see the differences in ops search performance when a guard is used in the operator preconditions.
          The throw operator can be guarded so that the two rooms are ensured to be different. This will reduce
          the number of paths taken but the guard check may have its own impact on runtime.")

(def op-throw-base
  "Base operator for throw with no guard condition."
  '{throw
    {:pre ((agent ?agent)
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
     :add ((in ?obj ?room2)
            )
     :del ((holds ?agent ?obj)
            )
     :txt ((?agent throws the ?obj into ?room2)
            )
     }
    }
  )

(def op-throw-guarded-one
  "Extends op-throw-base to include a guard condition ensuring that the two rooms are different."
  '{throw
    {:pre ((agent ?agent)
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
     :add ((in ?obj ?room2)
            )
     :del ((holds ?agent ?obj)
            )
     :txt ((?agent throws the ?obj into ?room2)
            )
     }
    }
  )

(def op-throw-guarded-two
  "Similar to op-throw-guarded-one but the guard has been moved right below where room1 and room2 are specified."
  '{throw
    {:pre ((agent ?agent)
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
     :add ((in ?obj ?room2)
            )
     :del ((holds ?agent ?obj)
            )
     :txt ((?agent throws the ?obj into ?room2)
            )
     }
    }
  )



(def state-throw-small
  "Basic state for agent to throw an item."
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
  "Extends state-throw-small for agent to throw 4 more items."
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
  "Extends state-throw-medium for agent to throw 5 more items."
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


;;;;;;;;;;;;;;;;;;;
;;; SMALL TESTS ;;;
;;;;;;;;;;;;;;;;;;;

;These small state tests will attempt to throw 1 item into B. This requires a search depth of 1.

(defn test-throw-base-small []
  "Average time: 6.17818ms"
  (time (ops-search state-throw-small '((in key B)) op-throw-base))
  )

(defn test-throw-guarded-one-small []
  "Average time: 6.9898ms"
  (time (ops-search state-throw-small '((in key B)) op-throw-guarded-one))
  )

(defn test-throw-guarded-two-small []
  "Average time: 7.64086ms"
  (time (ops-search state-throw-small '((in key B)) op-throw-guarded-two))
  )


;;;;;;;;;;;;;;;;;;;;
;;; MEDIUM TESTS ;;;
;;;;;;;;;;;;;;;;;;;;

;These medium state tests will attempt to throw 5 items into B. This requires a search depth of 5.

(defn test-throw-base-medium []
  "Average time: 1161.00726ms"
  (time (ops-search state-throw-medium '((in key B) (in lever B) (in rock B) (in sledgehammer B) (in chocolate B)) op-throw-base))
  )

(defn test-throw-guarded-one-medium []
  "Average time: 373.92064ms"
  (time (ops-search state-throw-medium '((in key B) (in lever B) (in rock B) (in sledgehammer B) (in chocolate B)) op-throw-guarded-one))
  )

(defn test-throw-guarded-two-medium []
  "Average time: 270.07456ms"
  (time (ops-search state-throw-medium '((in key B) (in lever B) (in rock B) (in sledgehammer B) (in chocolate B)) op-throw-guarded-two))
  )


;;;;;;;;;;;;;;;;;;;
;;; LARGE TESTS ;;;
;;;;;;;;;;;;;;;;;;;

;These large state tests will attempt to throw 10 items into B. This requires a search depth of 10.

(defn test-throw-base-large []
  "Average time: STACK OVERFLOW"
  (time (ops-search state-throw-large '((in key B) (in lever B) (in rock B) (in sledgehammer B) (in chocolate B) (in phone B) (in ladder B) (in knife B) (in torch B) (in journal B)) op-throw-base))
  )

(defn test-throw-guarded-one-large []
  "Average time: 21390.09436ms"
  (time (ops-search state-throw-large '((in key B) (in lever B) (in rock B) (in sledgehammer B) (in chocolate B) (in phone B) (in ladder B) (in knife B) (in torch B) (in journal B)) op-throw-guarded-one))
  )

(defn test-throw-guarded-two-large []
  "Average time: 10797.50554ms"
  (time (ops-search state-throw-large '((in key B) (in lever B) (in rock B) (in sledgehammer B) (in chocolate B) (in phone B) (in ladder B) (in knife B) (in torch B) (in journal B)) op-throw-guarded-two))
  )