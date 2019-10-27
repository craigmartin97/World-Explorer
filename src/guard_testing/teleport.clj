(ns guard-testing.teleport
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]))

(comment "Guard that room 1 and room 2 aren't the same room.
          Also guard that tele 1 and tele 2 aren't the same tele.
          Does having 2 separate guards work better than using an AND?
          Can guard be used to specify 'teleporter-connection ?tele1 ?tele2' OR 'teleporter-connection ?tele2 ?tele2'?")

(def op-teleport-base
  '{teleport {:pre ((agent ?agent)
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
              :add ((in ?agent ?room2))
              :del ((in ?agent ?room1))
              :txt ((?agent has transported from ?tele1 in ?room1 to ?tele2 in ?room2))
              }
    }
  )

(def op-teleport-guarded-one
  '{teleport {:pre ((agent ?agent)
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
              :add ((in ?agent ?room2))
              :del ((in ?agent ?room1))
              :txt ((?agent has transported from ?tele1 in ?room1 to ?tele2 in ?room2))
              }
    }
  )

(def op-teleport-guarded-two
  '{teleport {:pre ((agent ?agent)
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
              :add ((in ?agent ?room2))
              :del ((in ?agent ?room1))
              :txt ((?agent has transported from ?tele1 in ?room1 to ?tele2 in ?room2))
              }
    }
  )

(def op-teleport-guarded-three
  '{teleport {:pre ((agent ?agent)
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
              :add ((in ?agent ?room2))
              :del ((in ?agent ?room1))
              :txt ((?agent has transported from ?tele1 in ?room1 to ?tele2 in ?room2))
              }
    }
  )

(def state-teleport-small
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



(defn test-teleport-base-small []
  (time (ops-search state-teleport-small '((in R B)) op-teleport-base))
  )

(defn test-teleport-base-medium []
  (time (ops-search state-teleport-medium '((in R C)) op-teleport-base))
  )

(defn test-teleport-base-large []
  (time (ops-search state-teleport-large '((in R G)) op-teleport-base))
  )


(defn test-teleport-guarded-one-small []
  (time (ops-search state-teleport-small '((in R B)) op-teleport-guarded-one))
  )

(defn test-teleport-guarded-one-medium []
  (time (ops-search state-teleport-medium '((in R C)) op-teleport-guarded-one))
  )

(defn test-teleport-guarded-one-large []
  (time (ops-search state-teleport-large '((in R G)) op-teleport-guarded-one))
  )


(defn test-teleport-guarded-two-small []
  (time (ops-search state-teleport-small '((in R B)) op-teleport-guarded-two))
  )

(defn test-teleport-guarded-two-medium []
  (time (ops-search state-teleport-medium '((in R C)) op-teleport-guarded-two))
  )

(defn test-teleport-guarded-two-large []
  (time (ops-search state-teleport-large '((in R G)) op-teleport-guarded-two))
  )


(defn test-teleport-guarded-three-small []
  (time (ops-search state-teleport-small '((in R B)) op-teleport-guarded-three))
  )

(defn test-teleport-guarded-three-medium []
  (time (ops-search state-teleport-medium '((in R C)) op-teleport-guarded-three))
  )

(defn test-teleport-guarded-three-large []
  (time (ops-search state-teleport-large '((in R G)) op-teleport-guarded-three))
  )