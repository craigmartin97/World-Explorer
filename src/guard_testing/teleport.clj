(ns guard-testing.teleport
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]))

(comment "Tests to see the differences in ops search performance when a guard is used in the operator preconditions.
          The teleport operator can be guarded so that the two rooms and teleporters are ensured to be different.
          This will reduce the number of paths taken but the guard check may have its own impact on runtime.")

(def op-teleport-base
  "The base operator for teleport with no guard condition."
  '{teleport
    {:pre ((agent ?agent)
            (teleporter ?tele1)
            (teleporter ?tele2)
            (powerable ?tele1)
            (powerable ?tele2)
            (in ?agent ?room1)
            (in ?tele1 ?room1)
            (in ?tele2 ?room2)
            (teleporter-connection ?tele1 ?tele2)
            (powered ?tele1 true)
            (powered ?tele2 true)
            )
     :add ((in ?agent ?room2)
            )
     :del ((in ?agent ?room1)
            )
     :txt ((?agent has transported from ?tele1 in ?room1 to ?tele2 in ?room2)
            )
     }
    }
  )

(def op-teleport-guarded-one
  "Teleport with two separate guards included. After testing this operator, multiple guards does not seem to work with
  ops search. op-teleport-guarded-two handles this issue."
  '{teleport
    {:pre ((agent ?agent)
            (teleporter ?tele1)
            (teleporter ?tele2)
            (powerable ?tele1)
            (powerable ?tele2)
            (in ?agent ?room1)
            (in ?tele1 ?room1)
            (in ?tele2 ?room2)
            (:guard (not= (? room1) (? room2)))
            (teleporter-connection ?tele1 ?tele2)
            (powered ?tele1 true)
            (powered ?tele2 true)
            (:guard (not= (? tele1) (? tele2)))
            )
     :add ((in ?agent ?room2)
            )
     :del ((in ?agent ?room1)
            )
     :txt ((?agent has transported from ?tele1 in ?room1 to ?tele2 in ?room2)
            )
     }
    }
  )

(def op-teleport-guarded-two
  "Teleport with a single guard that combines the room and teleporter checks into an 'and' operator."
  '{teleport
    {:pre ((agent ?agent)
            (teleporter ?tele1)
            (teleporter ?tele2)
            (powerable ?tele1)
            (powerable ?tele2)
            (in ?agent ?room1)
            (in ?tele1 ?room1)
            (in ?tele2 ?room2)
            (teleporter-connection ?tele1 ?tele2)
            (powered ?tele1 true)
            (powered ?tele2 true)
            (:guard (and (not= (? room1) (? room2))
                         (not= (? tele1) (? tele2))))
            )
     :add ((in ?agent ?room2)
            )
     :del ((in ?agent ?room1)
            )
     :txt ((?agent has transported from ?tele1 in ?room1 to ?tele2 in ?room2)
            )
     }
    }
  )

(def op-teleport-guarded-three
  "Similar to op-teleport-guarded-three but can now work with the teleporter connections in both orders.
  This operator will likely be less efficient but it will mean less teleporter connections have to be added in states.
  See the alternative states for examples of this."
  '{teleport
    {:pre ((agent ?agent)
            (teleporter ?tele1)
            (teleporter ?tele2)
            (powerable ?tele1)
            (powerable ?tele2)
            (in ?agent ?room1)
            (in ?tele1 ?room1)
            (in ?tele2 ?room2)
            (teleporter-connection ?tele-con1 ?tele-con2)
            (:guard (and (not= (? room1) (? room2))
                         (not= (? tele1) (? tele2))
                         (or (and (= (? tele-con1) (? tele1)) (= (? tele-con2) (? tele2)))
                             (and (= (? tele-con2) (? tele1)) (= (? tele-con1) (? tele2))))
                         ))
            (powered ?tele1 true)
            (powered ?tele2 true)
            )
     :add ((in ?agent ?room2)
            )
     :del ((in ?agent ?room1)
            )
     :txt ((?agent has transported from ?tele1 in ?room1 to ?tele2 in ?room2)
            )
     }
    }
  )

(def state-teleport-small
  "Small state where the agent can use two teleporters."
  '#{(agent R)
     (in R A)

     (room A)
     (room B)

     (teleporter tele-A)
     (teleporter tele-B)
     (powerable tele-A)
     (powerable tele-B)
     (powered tele-A true)
     (powered tele-B true)
     (teleporter-connection tele-A tele-B)
     (teleporter-connection tele-B tele-A)

     (in tele-A A)
     (in tele-B B)
     }
  )

(def state-teleport-small-alt
  "Alternate version of state-teleport-small where only 1 teleporter-connection is defined."
  '#{(agent R)
     (in R A)

     (room A)
     (room B)

     (teleporter tele-A)
     (teleporter tele-B)
     (powerable tele-A)
     (powerable tele-B)
     (powered tele-A true)
     (powered tele-B true)
     (teleporter-connection tele-A tele-B)

     (in tele-A A)
     (in tele-B B)
     }
  )

(def state-teleport-medium
  "Extends state-teleport-small to include 4 more teleporters."
  '#{(agent R)
     (in R A)

     (room A)
     (room B)
     (room C)
     (room D)
     (room E)

     (teleporter tele-A)
     (teleporter tele-B)
     (teleporter tele-C)
     (teleporter tele-D)
     (teleporter tele-E)
     (powerable tele-A)
     (powerable tele-B)
     (powerable tele-C)
     (powerable tele-D)
     (powerable tele-E)
     (powered tele-A true)
     (powered tele-B true)
     (powered tele-C true)
     (powered tele-D true)
     (powered tele-E true)
     (teleporter-connection tele-A tele-B)
     (teleporter-connection tele-B tele-A)
     (teleporter-connection tele-B tele-C)
     (teleporter-connection tele-C tele-B)
     (teleporter-connection tele-C tele-D)
     (teleporter-connection tele-D tele-C)
     (teleporter-connection tele-D tele-E)
     (teleporter-connection tele-E tele-D)

     (in tele-A A)
     (in tele-B B)
     (in tele-C E)
     (in tele-D D)
     (in tele-E C)
     }
  )

(def state-teleport-medium-alt
  "Alternate version of state-teleport-small where only 1 teleporter-connection is defined for each teleporter."
  '#{(agent R)
     (in R A)

     (room A)
     (room B)
     (room C)
     (room D)
     (room E)

     (teleporter tele-A)
     (teleporter tele-B)
     (teleporter tele-C)
     (teleporter tele-D)
     (teleporter tele-E)
     (powerable tele-A)
     (powerable tele-B)
     (powerable tele-C)
     (powerable tele-D)
     (powerable tele-E)
     (powered tele-A true)
     (powered tele-B true)
     (powered tele-C true)
     (powered tele-D true)
     (powered tele-E true)
     (teleporter-connection tele-A tele-B)
     (teleporter-connection tele-B tele-C)
     (teleporter-connection tele-C tele-D)
     (teleporter-connection tele-D tele-E)

     (in tele-A A)
     (in tele-B B)
     (in tele-C E)
     (in tele-D D)
     (in tele-E C)
     }
  )

(def state-teleport-large
  "Extends state-teleport-medium to include 5 more teleporters."
  '#{(agent R)
     (in R A)

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

     (teleporter tele-A)
     (teleporter tele-B)
     (teleporter tele-C)
     (teleporter tele-D)
     (teleporter tele-E)
     (teleporter tele-F)
     (teleporter tele-G)
     (teleporter tele-H)
     (teleporter tele-I)
     (teleporter tele-J)
     (powerable tele-A)
     (powerable tele-B)
     (powerable tele-C)
     (powerable tele-D)
     (powerable tele-E)
     (powerable tele-F)
     (powerable tele-G)
     (powerable tele-H)
     (powerable tele-I)
     (powerable tele-J)
     (powered tele-A true)
     (powered tele-B true)
     (powered tele-C true)
     (powered tele-D true)
     (powered tele-E true)
     (powered tele-F true)
     (powered tele-G true)
     (powered tele-H true)
     (powered tele-I true)
     (powered tele-J true)
     (teleporter-connection tele-A tele-B)
     (teleporter-connection tele-B tele-A)
     (teleporter-connection tele-B tele-C)
     (teleporter-connection tele-C tele-B)
     (teleporter-connection tele-C tele-D)
     (teleporter-connection tele-D tele-C)
     (teleporter-connection tele-D tele-E)
     (teleporter-connection tele-E tele-D)
     (teleporter-connection tele-E tele-F)
     (teleporter-connection tele-F tele-E)
     (teleporter-connection tele-F tele-G)
     (teleporter-connection tele-G tele-F)
     (teleporter-connection tele-G tele-H)
     (teleporter-connection tele-H tele-G)
     (teleporter-connection tele-H tele-I)
     (teleporter-connection tele-I tele-H)
     (teleporter-connection tele-I tele-J)
     (teleporter-connection tele-J tele-I)

     (in tele-A A)
     (in tele-B B)
     (in tele-C E)
     (in tele-D D)
     (in tele-E C)
     (in tele-F F)
     (in tele-G J)
     (in tele-H I)
     (in tele-I H)
     (in tele-J G)
     }
  )

(def state-teleport-large-alt
  "Alternate version of state-teleport-small where only 1 teleporter-connection is defined for each teleporter."
  '#{(agent R)
     (in R A)

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

     (teleporter tele-A)
     (teleporter tele-B)
     (teleporter tele-C)
     (teleporter tele-D)
     (teleporter tele-E)
     (teleporter tele-F)
     (teleporter tele-G)
     (teleporter tele-H)
     (teleporter tele-I)
     (teleporter tele-J)
     (powerable tele-A)
     (powerable tele-B)
     (powerable tele-C)
     (powerable tele-D)
     (powerable tele-E)
     (powerable tele-F)
     (powerable tele-G)
     (powerable tele-H)
     (powerable tele-I)
     (powerable tele-J)
     (powered tele-A true)
     (powered tele-B true)
     (powered tele-C true)
     (powered tele-D true)
     (powered tele-E true)
     (powered tele-F true)
     (powered tele-G true)
     (powered tele-H true)
     (powered tele-I true)
     (powered tele-J true)
     (teleporter-connection tele-A tele-B)
     (teleporter-connection tele-B tele-C)
     (teleporter-connection tele-C tele-D)
     (teleporter-connection tele-D tele-E)
     (teleporter-connection tele-E tele-F)
     (teleporter-connection tele-F tele-G)
     (teleporter-connection tele-G tele-H)
     (teleporter-connection tele-H tele-I)
     (teleporter-connection tele-I tele-J)

     (in tele-A A)
     (in tele-B B)
     (in tele-C E)
     (in tele-D D)
     (in tele-E C)
     (in tele-F F)
     (in tele-G J)
     (in tele-H I)
     (in tele-I H)
     (in tele-J G)
     }
  )



;;;;;;;;;;;;;;;;;;;
;;; SMALL TESTS ;;;
;;;;;;;;;;;;;;;;;;;

;These small state tests will attempt to move the agent from room A to room B using teleporters.
;This requires a search depth of 1.

(defn test-teleport-base-small []
  "Average time: 6.90788ms"
  (time (ops-search state-teleport-small '((in R B)) op-teleport-base))
  )

(defn test-teleport-guarded-one-small []
  "Average time: NIL"
  (time (ops-search state-teleport-small '((in R B)) op-teleport-guarded-one))
  )

(defn test-teleport-guarded-two-small []
  "Average time: 8.5805ms"
  (time (ops-search state-teleport-small '((in R B)) op-teleport-guarded-two))
  )

(defn test-teleport-guarded-three-small []
  "Average time: 14.2201ms"
  (time (ops-search state-teleport-small-alt '((in R B)) op-teleport-guarded-three))
  )


;;;;;;;;;;;;;;;;;;;;
;;; MEDIUM TESTS ;;;
;;;;;;;;;;;;;;;;;;;;

;These medium state tests will attempt to move the agent from room A to room C using teleporters.
;This requires a search depth of 4.

(defn test-teleport-base-medium []
  "Average time: 125.99574ms"
  (time (ops-search state-teleport-medium '((in R C)) op-teleport-base))
  )

(defn test-teleport-guarded-one-medium []
  "Average time: NIL"
  (time (ops-search state-teleport-medium '((in R C)) op-teleport-guarded-one))
  )

(defn test-teleport-guarded-two-medium []
  "Average time: 132.55644ms"
  (time (ops-search state-teleport-medium '((in R C)) op-teleport-guarded-two))
  )

(defn test-teleport-guarded-three-medium []
  "Average time: 363.51454ms"
  (time (ops-search state-teleport-medium-alt '((in R C)) op-teleport-guarded-three))
  )


;;;;;;;;;;;;;;;;;;;
;;; LARGE TESTS ;;;
;;;;;;;;;;;;;;;;;;;

;These medium state tests will attempt to move the agent from room A to room G using teleporters.
;This requires a search depth of 9.

(defn test-teleport-base-large []
  "Average time: 1156.3655ms"
  (time (ops-search state-teleport-large '((in R G)) op-teleport-base))
  )

(defn test-teleport-guarded-one-large []
  "Average time: NIL"
  (time (ops-search state-teleport-large '((in R G)) op-teleport-guarded-one))
  )

(defn test-teleport-guarded-two-large []
  "Average time: 1160.25032ms"
  (time (ops-search state-teleport-large '((in R G)) op-teleport-guarded-two))
  )

(defn test-teleport-guarded-three-large []
  "Average time: 2946.07286ms"
  (time (ops-search state-teleport-large-alt '((in R G)) op-teleport-guarded-three))
  )