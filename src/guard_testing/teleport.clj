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

(def op-teleport-guarded-1
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
                    (:guard (not= (? room1) (? room2)))
                    (:guard (not= (? tele1) (? tele2)))
                    )
              :add ((in ?agent ?room2))
              :del ((in ?agent ?room1))
              :txt ((?agent has transported from ?tele1 in ?room1 to ?tele2 in ?room2))
              }
    }
  )

(def op-teleport-guarded-2
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
                    (:guard (and (not= (? room1) (? room2)) (not= (? tele1) (? tele2))))
                    )
              :add ((in ?agent ?room2))
              :del ((in ?agent ?room1))
              :txt ((?agent has transported from ?tele1 in ?room1 to ?tele2 in ?room2))
              }
    }
  )

(def op-teleport-guarded-3
  '{teleport {:pre ((agent ?agent)
                    (teleporter ?tele1)
                    (teleporter ?tele2)
                    (:guard (not= (? tele1) (? tele2)))
                    (powerable ?tele1)
                    (powerable ?tele2)
                    (in ?agent ?room1)
                    (in ?tele1 ?room1)
                    (in ?tele2 ?room2)
                    (:guard (not= (? room1) (? room2)))
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

(def op-teleport-guarded-4
  '{teleport {:pre ((agent ?agent)
                    (teleporter ?tele1)
                    (teleporter ?tele2)
                    (:guard (not= (? tele1) (? tele2)))
                    (powerable ?tele1)
                    (powerable ?tele2)
                    (in ?agent ?room1)
                    (in ?tele1 ?room1)
                    (in ?tele2 ?room2)
                    (:guard (not= (? room1) (? room2)))
                    (teleporter-connection ?tele-con1 ?tele-con2)
                    (:guard (or (and (= (? tele-con1) (? tele1)) (= (? tele-con2) (? tele2)))
                                (and (= (? tele-con2) (? tele1)) (= (? tele-con1) (? tele2)))
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