(ns guard-testing.move
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]))


(comment "Tests to see the differences in ops search performance when a guard is used in the operator preconditions.
          The move operator can be guarded so that the two rooms are ensured to be different. This will reduce
          the number of paths taken but the guard check may have its own impact on runtime.")

(def op-move-base
  "The operator for move that we originally created with no guard condition."
  '{move
    {:pre ((agent ?agent)
            (room ?room1)
            (room ?room2)
            (door ?door)
            (opened ?door true)
            (unlocked ?door true)
            (connects ?door ?room1)
            (connects ?door ?room2)
            (in ?agent ?room1)
            )
     :add ((in ?agent ?room2)
            )
     :del ((in ?agent ?room1)
            )
     :txt (?agent has moved from ?room1 to ?room2)
     }
    }
  )

(def op-move-guarded-one
  "Similar to op-move-base but now contains a guard to ensure room1 and room2 aren't the same room."
  '{move
    {:pre ((agent ?agent)
            (room ?room1)
            (room ?room2)
            (door ?door)
            (opened ?door true)
            (unlocked ?door true)
            (connects ?door ?room1)
            (connects ?door ?room2)
            (in ?agent ?room1)
            (:guard (not= (? room1) (? room2)))
            )
     :add ((in ?agent ?room2)
            )
     :del ((in ?agent ?room1)
            )
     :txt (?agent has moved from ?room1 to ?room2)
     }
    }
  )

(def op-move-guarded-two
  "Similar to op-move-guarded-one but the guard has been moved right below where room1 and room2 are specified."
  '{move
    {:pre ((agent ?agent)
            (room ?room1)
            (room ?room2)
            (:guard (not= (? room1) (? room2)))
            (door ?door)
            (opened ?door true)
            (unlocked ?door true)
            (connects ?door ?room1)
            (connects ?door ?room2)
            (in ?agent ?room1)
            )
     :add ((in ?agent ?room2)
            )
     :del ((in ?agent ?room1)
            )
     :txt (?agent has moved from ?room1 to ?room2)
     }
    }
  )

(def state-move-small
  "Basic state containing an agent and 2 connected rooms."
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
     }
  )

(def state-move-medium
  "Extends state-move-small by adding 3 more rooms to the scenario."
  '#{
     (agent R)
     (room A)
     (room B)
     (room C)
     (room D)
     (room E)

     (door A-B)
     (door B-C)
     (door C-D)
     (door D-E)

     (connects A-B A)
     (connects A-B B)
     (connects B-C B)
     (connects B-C C)
     (connects C-D C)
     (connects C-D D)
     (connects D-E D)
     (connects D-E E)

     (opened A-B true)
     (opened B-C true)
     (opened C-D true)
     (opened D-E true)

     (unlocked A-B true)
     (unlocked B-C true)
     (unlocked C-D true)
     (unlocked D-E true)

     (in R A)
     }
  )

(def state-move-large
  "Extends state-move-medium by adding 5 more rooms to the scenario."
  '#{
     (agent R)
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

     (door A-B)
     (door B-C)
     (door C-D)
     (door D-E)
     (door E-F)
     (door F-G)
     (door G-H)
     (door H-I)
     (door I-J)

     (connects A-B A)
     (connects A-B B)
     (connects B-C B)
     (connects B-C C)
     (connects C-D C)
     (connects C-D D)
     (connects D-E D)
     (connects D-E E)
     (connects E-F E)
     (connects E-F F)
     (connects F-G F)
     (connects F-G G)
     (connects G-H G)
     (connects G-H H)
     (connects H-I H)
     (connects H-I I)
     (connects I-J I)
     (connects I-J J)

     (opened A-B true)
     (opened B-C true)
     (opened C-D true)
     (opened D-E true)
     (opened E-F true)
     (opened F-G true)
     (opened G-H true)
     (opened H-I true)
     (opened I-J true)

     (unlocked A-B true)
     (unlocked B-C true)
     (unlocked C-D true)
     (unlocked D-E true)
     (unlocked E-F true)
     (unlocked F-G true)
     (unlocked G-H true)
     (unlocked H-I true)
     (unlocked I-J true)

     (in R A)
     }
  )


;;;;;;;;;;;;;;;;;;;
;;; SMALL TESTS ;;;
;;;;;;;;;;;;;;;;;;;

;These small state tests will attempt to move the agent from room A to room B. This requires a search depth of 1.

(defn test-move-base-small []
  "Average time: 5.3936ms"
  (time (ops-search state-move-small '((in R B)) op-move-base))
  )

(defn test-move-guarded-one-small []
  "Average time: 6.9865"
  (time (ops-search state-move-small '((in R B)) op-move-guarded-one))
  )

(defn test-move-guarded-two-small []
  "Average time: 6.9464"
  (time (ops-search state-move-small '((in R B)) op-move-guarded-two))
  )


;;;;;;;;;;;;;;;;;;;;
;;; MEDIUM TESTS ;;;
;;;;;;;;;;;;;;;;;;;;

;These medium state tests will attempt to move the agent from room A to room E. This requires a search depth of 4.

(defn test-move-base-medium []
  "Average time: 280.1636ms"
  (time (ops-search state-move-medium '((in R E)) op-move-base))
  )

(defn test-move-guarded-one-medium []
  "Average time: 282.29778ms"
  (time (ops-search state-move-medium '((in R E)) op-move-guarded-one))
  )

(defn test-move-guarded-two-medium []
  "Average time: 335.4809ms"
  (time (ops-search state-move-medium '((in R E)) op-move-guarded-two))
  )


;;;;;;;;;;;;;;;;;;;
;;; LARGE TESTS ;;;
;;;;;;;;;;;;;;;;;;;

;These large state tests will attempt to move the agent from room A to room J. This requires a search depth of 9.

(defn test-move-base-large []
  "Average time: 6512.58514ms"
  (time (ops-search state-move-large '((in R J)) op-move-base))
  )

(defn test-move-guarded-one-large []
  "Average time: 6457.90434ms"
  (time (ops-search state-move-large '((in R J)) op-move-guarded-one))
  )

(defn test-move-guarded-two-large []
  "Average time: 6096.84584ms"
  (time (ops-search state-move-large '((in R J)) op-move-guarded-two))
  )