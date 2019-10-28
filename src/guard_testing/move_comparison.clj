(ns guard-testing.move-comparison
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
  [org.clojars.cognesence.matcher.core :refer :all]
  [org.clojars.cognesence.ops-search.core :refer :all]
  [planner.planner :refer :all]))

(def op-move-base-search
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


(def state-small
  '#{(agent R)
     (visited R A)

     (room A)
     (room B)

     (in R A)
     }
  )

(def state-medium
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

(defn test-move-small-base-search []
  (time (ops-search state-small '((visited R A B)) op-move-base-search))
  )

(defn test-move-small-guarded-one-search []
  (time (ops-search state-small '((visited R A B)) op-move-guarded-one-search))
  )

(defn test-move-small-guarded-two-search []
  (time (ops-search state-small '((visited R A B)) op-move-guarded-two-search))
  )

(defn test-move-small-base-planner []
  (time (planner state-small '(visited R A B) op-move-base-planner))
  )

(defn test-move-small-guarded-one-planner []
  (time (planner state-small '(visited R A B) op-move-guarded-one-planner))
  )

(defn test-move-small-guarded-two-planner []
  (time (planner state-small '(visited R A B) op-move-guarded-two-planner))
  )

;-----------------------------------------------------------------

(defn test-move-small-two-base-search []
  (time (ops-search state-medium '((visited R A B C D E)) op-move-base-search))
  )

(defn test-move-small-two-guarded-one-search []
  (time (ops-search state-medium '((visited R A B C D E)) op-move-guarded-one-search))
  )

(defn test-move-small-two-guarded-two-search []
  (time (ops-search state-medium '((visited R A B C D E)) op-move-guarded-two-search))
  )

(defn test-move-small-two-base-planner []
  (time (planner state-medium '(visited R A B C D E) op-move-base-planner))
  )

(defn test-move-small-two-guarded-one-planner []
  (time (planner state-medium '(visited R A B C D E) op-move-guarded-one-planner))
  )

(defn test-move-small-two-guarded-two-planner []
  (time (planner state-medium '(visited R A B C D E) op-move-guarded-two-planner))
  )

;-----------------------------------------------------------------

(defn test-move-medium-base-search []
  (time (ops-search state-medium '((visited R A B C D E F G)) op-move-base-search))
  )

(defn test-move-medium-guarded-one-search []
  (time (ops-search state-medium '((visited R A B C D E F G)) op-move-guarded-one-search))
  )

(defn test-move-medium-guarded-two-search []
  (time (ops-search state-medium '((visited R A B C D E F G)) op-move-guarded-two-search))
  )

(defn test-move-medium-base-planner []
  (time (planner state-medium '(visited R A B C D E F G) op-move-base-planner))
  )

(defn test-move-medium-guarded-one-planner []
  (time (planner state-medium '(visited R A B C D E F G) op-move-guarded-one-planner))
  )

(defn test-move-medium-guarded-two-planner []
  (time (planner state-medium '(visited R A B C D E F G) op-move-guarded-two-planner))
  )

;-----------------------------------------------------------------

(defn test-move-large-base-search []
  (time (ops-search state-large '((visited R A B C D E F G H I J K L M N O)) op-move-base-search))
  )

(defn test-move-large-guarded-one-search []
  (time (ops-search state-large '((visited R A B C D E F G H I J K L M N O)) op-move-guarded-one-search))
  )

(defn test-move-large-guarded-two-search []
  (time (ops-search state-large '((visited R A B C D E F G H I J K L M N O)) op-move-guarded-two-search))
  )

(defn test-move-large-base-planner []
  (time (planner state-large '(visited R A B C D E F G H I J K L M N O) op-move-base-planner))
  )

(defn test-move-large-guarded-one-planner []
  (time (planner state-large '(visited R A B C D E F G H I J K L M N O) op-move-guarded-one-planner))
  )

(defn test-move-large-guarded-two-planner []
  (time (planner state-large '(visited R A B C D E F G H I J K L M N O) op-move-guarded-two-planner))
  )




















