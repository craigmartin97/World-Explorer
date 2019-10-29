(ns guard-testing.move-comparison
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
  [org.clojars.cognesence.matcher.core :refer :all]
  [org.clojars.cognesence.ops-search.core :refer :all]
  [planner.planner :refer :all]))

(comment "Comparing planner and ops search performance when guards are used in operators.

          These tests use a slightly modified version of the move operator which logs rooms that agents have visited.
          This is to allow the planner to move through multiple rooms with it's one goal state limitation.

          The move operator can be guarded so that the two rooms are ensured to be different. This will reduce
          the number of paths taken but the guard check may have its own impact on runtime.")

(def op-move-base-search
  "Base implementation of the modified move operator in ops search with no guard."
  '{move
    {:pre ((agent ?agent)
            (visited ?agent ??visited)
            (room ?room1)
            (room ?room2)
            (in ?agent ?room1)
            )
     :add ((in ?agent ?room2)
            (visited ?agent ??visited ?room2)
            )
     :del ((in ?agent ?room1)
            (visited ?agent ??visited)
            )
     :txt (?agent has moved from ?room1 to ?room2)
     }
    }
  )

(def op-move-guarded-one-search
  "Extends op-move-base-search to include a guard ensuring that room1 and room2 are different."
  '{move
    {:pre ((agent ?agent)
            (visited ?agent ??visited)
            (room ?room1)
            (room ?room2)
            (in ?agent ?room1)
            (:guard (not= (? room1) (? room2)))
            )
     :add ((in ?agent ?room2)
            (visited ?agent ??visited ?room2)
            )
     :del ((in ?agent ?room1)
            (visited ?agent ??visited)
            )
     :txt (?agent has moved from ?room1 to ?room2)
     }
    }
  )

(def op-move-guarded-two-search
  "Extends op-move-guarded-one-search guard condition to ensure the checked room has not already been visited."
  '{move
    {:pre ((agent ?agent)
            (room ?room1)
            (room ?room2)
            (visited ?agent ??visited)
            (in ?agent ?room1)
            (:guard (and (not= (? room1) (? room2))
                         (not (some #(= % (? room2)) (? visited)))
                         ))
            )
     :add ((in ?agent ?room2)
            (visited ?agent ??visited ?room2)
            )
     :del ((in ?agent ?room1)
            (visited ?agent ??visited)
            )
     :txt (?agent has moved from ?room1 to ?room2)
     }
    }
  )




(def op-move-base-planner
  "Base implementation of the modified move operator in planner with no guard."
  '{
    move
    {:name move
     :achieves (visited ?agent ??vis ?room1 ?room2)
     :when ((agent ?agent)
             (room ?room1)
             (room ?room2)
             (visited ?agent ??cur-vis)
             )
     :post ((visited ?agent ??vis ?room1))
     :pre ()
     :add ((visited ?agent ??vis ?room1 ?room2)
            (in ?agent ?room2))
     :del ((visited ?agent ??vis ?room1)
            (in ?agent ?room1))
     :txt (?agent has moved from ?room1 to ?room2)
     }
    }
  )

(def op-move-guarded-one-planner
  "Extends op-move-base-search to include a guard ensuring that room1 and room2 are different."
  '{
    move
    {:name move
     :achieves (visited ?agent ??vis ?room1 ?room2)
     :when ((agent ?agent)
             (room ?room1)
             (room ?room2)
             (visited ?agent ??cur-vis)
             (:guard (not= (? room1) (? room2)))
             )
     :post ((visited ?agent ??vis ?room1))
     :pre ()
     :add ((visited ?agent ??vis ?room1 ?room2)
            (in ?agent ?room2))
     :del ((visited ?agent ??vis ?room1)
            (in ?agent ?room1))
     :txt (?agent has moved from ?room1 to ?room2)
     }
    }
  )

(def op-move-guarded-two-planner
  "Extends op-move-guarded-one-search guard condition to ensure the checked room has not already been visited."
  '{
    move
    {:name move
     :achieves (visited ?agent ??vis ?room1 ?room2)
     :when ((agent ?agent)
             (room ?room1)
             (room ?room2)
             (visited ?agent ??cur-vis)
             (:guard (and (not= (? room1) (? room2))
                          (not (some #(= % (? room2)) (? cur-vis)))
                          ))
             )
     :post ((visited ?agent ??vis ?room1))
     :pre ()
     :add ((visited ?agent ??vis ?room1 ?room2)
            (in ?agent ?room2))
     :del ((visited ?agent ??vis ?room1)
            (in ?agent ?room1))
     :txt (?agent has moved from ?room1 to ?room2)
     }
    }
  )


(def state-very-small
  "Basic state containing an agent and 2 connected rooms."
  '#{(agent R)
     (visited R A)

     (room A)
     (room B)

     (in R A)
     }
  )

(def state-small
  "Extends state-move-very-small by adding 3 more rooms to the scenario."
  '#{(agent R)
     (visited R A)

     (room A)
     (room B)
     (room C)
     (room D)
     (room E)

     (in R A)
     }
  )

(def state-medium
  "Extends state-move-small by adding 2 more rooms to the scenario."
  '#{(agent R)
     (visited R A)

     (room A)
     (room B)
     (room C)
     (room D)
     (room E)
     (room F)
     (room G)

     (in R A)
     }
  )

(def state-large
  "Extends state-move-medium by adding 8 more rooms to the scenario."
  '#{(agent R)
     (visited R A)

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

     (in R A)
     }
  )


;;;;;;;;;;;;;;;;;;;;;;;;
;;; VERY SMALL TESTS ;;;
;;;;;;;;;;;;;;;;;;;;;;;;

;These very small state tests will get the agent to visit rooms A and B. This requires a search depth of 1.

(defn test-move-very-small-base-search []
  "Average time: 1.04536ms"
  (time (ops-search state-very-small '((visited R A B)) op-move-base-search))
  )

(defn test-move-very-small-guarded-one-search []
  "Average time: 1.67094ms"
  (time (ops-search state-very-small '((visited R A B)) op-move-guarded-one-search))
  )

(defn test-move-very-small-guarded-two-search []
  "Average time: 2.6011ms"
  (time (ops-search state-very-small '((visited R A B)) op-move-guarded-two-search))
  )

(defn test-move-very-small-base-planner []
  "Average time: 2.6199ms"
  (time (planner state-very-small '(visited R A B) op-move-base-planner))
  )

(defn test-move-very-small-guarded-one-planner []
  "Average time: 2.82068ms"
  (time (planner state-very-small '(visited R A B) op-move-guarded-one-planner))
  )

(defn test-move-very-small-guarded-two-planner []
  "Average time: 3.40284ms"
  (time (planner state-very-small '(visited R A B) op-move-guarded-two-planner))
  )


;;;;;;;;;;;;;;;;;;;
;;; SMALL TESTS ;;;
;;;;;;;;;;;;;;;;;;;

;These small state tests will get the agent to visit rooms A, B, C, D, and E. This requires a search depth of 4.

(defn test-move-small-base-search []
  "Average time: 1095.62528ms"
  (time (ops-search state-small '((visited R A B C D E)) op-move-base-search))
  )

(defn test-move-small-guarded-one-search []
  "Average time: 4503.65318ms"
  (time (ops-search state-small '((visited R A B C D E)) op-move-guarded-one-search))
  )

(defn test-move-small-guarded-two-search []
  "Average time: 5224.7315ms"
  (time (ops-search state-small '((visited R A B C D E)) op-move-guarded-two-search))
  )

(defn test-move-small-base-planner []
  "Average time: 8.25704ms"
  (time (planner state-small '(visited R A B C D E) op-move-base-planner))
  )

(defn test-move-small-guarded-one-planner []
  "Average time: 23.06842ms"
  (time (planner state-small '(visited R A B C D E) op-move-guarded-one-planner))
  )

(defn test-move-small-guarded-two-planner []
  "Average time: 29.11646ms"
  (time (planner state-small '(visited R A B C D E) op-move-guarded-two-planner))
  )


;;;;;;;;;;;;;;;;;;;
;;; MEDIUM TESTS ;;;
;;;;;;;;;;;;;;;;;;;

;These medium state tests will get the agent to visit rooms A, B, C, D, E, F, and G. This requires a search depth of 6.

(defn test-move-medium-base-search []
  "Average time: STACK OVERFLOW"
  (time (ops-search state-medium '((visited R A B C D E F G)) op-move-base-search))
  )

(defn test-move-medium-guarded-one-search []
  "Average time: STACK OVERFLOW"
  (time (ops-search state-medium '((visited R A B C D E F G)) op-move-guarded-one-search))
  )

(defn test-move-medium-guarded-two-search []
  "Average time: 56969.71452"
  (time (ops-search state-medium '((visited R A B C D E F G)) op-move-guarded-two-search))
  )

(defn test-move-medium-base-planner []
  "Average time: 17.45756ms"
  (time (planner state-medium '(visited R A B C D E F G) op-move-base-planner))
  )

(defn test-move-medium-guarded-one-planner []
  "Average time: 94.99406ms"
  (time (planner state-medium '(visited R A B C D E F G) op-move-guarded-one-planner))
  )

(defn test-move-medium-guarded-two-planner []
  "Average time: 116.66758ms"
  (time (planner state-medium '(visited R A B C D E F G) op-move-guarded-two-planner))
  )


;;;;;;;;;;;;;;;;;;;
;;; LARGE TESTS ;;;
;;;;;;;;;;;;;;;;;;;

;These large state tests will get the agent to visit rooms A, B, C, D, E, F, G, H, I, J, K, L, M, N, O. This requires a search depth of 4.

(defn test-move-large-base-search []
  "Average time: STACK OVERFLOW"
  (time (ops-search state-large '((visited R A B C D E F G H I J K L M N O)) op-move-base-search))
  )

(defn test-move-large-guarded-one-search []
  "Average time: STACK OVERFLOW"
  (time (ops-search state-large '((visited R A B C D E F G H I J K L M N O)) op-move-guarded-one-search))
  )

(defn test-move-large-guarded-two-search []
  "Average time: STACK OVERFLOW"
  (time (ops-search state-large '((visited R A B C D E F G H I J K L M N O)) op-move-guarded-two-search))
  )

(defn test-move-large-base-planner []
  "Average time: 28.78154ms"
  (time (planner state-large '(visited R A B C D E F G H I J K L M N O) op-move-base-planner))
  )

(defn test-move-large-guarded-one-planner []
  "Average time: 57.8468394ms"
  (time (planner state-large '(visited R A B C D E F G H I J K L M N O) op-move-guarded-one-planner))
  )

(defn test-move-large-guarded-two-planner []
  "Average time: 78.3828998ms"
  (time (planner state-large '(visited R A B C D E F G H I J K L M N O) op-move-guarded-two-planner))
  )




















