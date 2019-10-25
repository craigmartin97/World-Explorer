(ns guard-testing.move
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]))

(comment "Guard that room 1 and room 2 aren't the same room.")

(def op-move-base
    '{move {:pre ((agent ?agent)
                (room ?room1)
                (room ?room2)
                (door ?door)
                (opened ?door true)
                (unlocked ?door true)
                (connects ?door ?room1)
                (connects ?door ?room2)
                (in ?agent ?room1)
                )
            :add ((in ?agent ?room2))
            :del ((in ?agent ?room1))
            :txt (?agent has moved from ?room1 to ?room2)
          }
    }
  )

(def op-move-guarded-1
     '{move {:pre ((agent ?agent)
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
             :add ((in ?agent ?room2))
             :del ((in ?agent ?room1))
             :txt (?agent has moved from ?room1 to ?room2)
             }
       }
     )

(def op-move-guarded-2
  '{move {:pre ((agent ?agent)
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
          :add ((in ?agent ?room2))
          :del ((in ?agent ?room1))
          :txt (?agent has moved from ?room1 to ?room2)
          }
    }
  )

(def state-move-small
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
     }
  )

(def state-move-medium
  "basic state for agent to move from one room to next"
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
  "basic state for agent to move from one room to next"
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



(defn test-move-base-small []
  (time (ops-search state-move-small '((in R B)) op-move-base))
  )

(defn test-move-guarded-small []
  (time (ops-search state-move-small '((in R B)) op-move-guarded))
  )


(defn test-move-base-medium []
  (time (ops-search state-move-medium '((in R E)) op-move-base))
  )

(defn test-move-guarded-medium []
  (time (ops-search state-move-medium '((in R E)) op-move-guarded))
  )


(defn test-move-base-large []
  (time (ops-search state-move-large '((in R J)) op-move-base))
  )

(defn test-move-guarded-large []
  (time (ops-search state-move-large '((in R J)) op-move-guarded))
  )