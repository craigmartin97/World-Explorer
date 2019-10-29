(ns planner.connected-planner
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            [clojure.set :refer :all]
            [planner.planner :refer :all]

            )
  )

;(planner move-A-D-all-unlocked '(in R D) operations)
;(planner move-A-D-all-unlocked '(opened A-B true) operations)

(def josh-ops
  '{
    protect
    {:name     protect
     :achieves (protected ?x)
     :when     ((protected ??visited))
     :add      ((protected ??visited ?x))
     :del      ((protected ??visited))
     }
    move
    {:name     move-agent
     :achieves (in ?agent ?room2)
     :when     ((agent ?agent)
                 (door ?door)
                 (room ?room1)
                 (room ?room2)
                 (:guard (not= (? room1) (? room2)))
                 (protected ??visited)
                 (:guard (and (not (some #(= % (? room1)) (? visited)))
                              (not (some #(= % (? room2)) (? visited)))
                              )
                   )
                 (connects ?door ?room1)
                 (connects ?door ?room2)
                 )
     :post     ((protected ?room2)
                 (in ?agent ?room1)
                 )
     :pre      ()
     :add      ((protected ??visited)
                 (in ?agent ?room2)
                 )
     :del      ((protected ??_)
                 (in ?agent ?room1)
                 )
     :txt      (?agent has moved from ?room1 to ?room2)
     }
    }
  )

(def operations
  "A map of operations that the agent can perform in the world"
  '{
    move
    {:name     move-agent
     :achieves (in ?agent ?room2)
     :when     ((agent ?agent)
                 (room ?room1)
                 (room ?room2)
                 (door ?door)
                 (connects ?door ?room1)
                 (connects ?door ?room2)
                 (protected ??visited-rooms)
                 (:guard (not= (? room1) (? room2)))
                 (:guard (not
                           (some (fn [x]
                                   (println (str "PRT X: " x "  AND R2: " (? room2)))
                                   (= (compare x (? room2)) 0)
                                   )

                                 (? visited-rooms)
                                 )
                           )
                   )

                 (comment
                   one tuple that will have one protected tuple, which stores all the
                   protected rooms that have been visited using the double ?? matcher

                   add a guard that will check that the protected tuple doesnt contain the room
                   you are trying to move to

                   when picking up key del current protected tuple and add empty tuple in its place.

                   )

                 )
     :post     (
                 (in ?agent ?room1)
                 (opened ?door true)
                 (unlocked ?door true)
                 )
     :pre      ()
     :add      (
                 (in ?agent ?room2)
                 (protected ??visited-rooms ?room1)
                 )
     :del      (
                 (in ?agent ?room1)
                 )
     :txt      (agent ?agent has moved from ?room1 to ?room2)
     }
    }
  )


(def operations2

  "A map of operations that the agent can perform in the world"

  '{

    :move
    {
     :name     move-agent
     :achieves (in ?agent ?room2)
     :when     (
                 (agent ?agent)
                 (room ?room1)
                 (room ?room2)
                 (door ?door)

                 (connects ?door ?room1)
                 (connects ?door ?room2)
                 (protected ??visited-rooms)
                 ;(:guard (not= (? room1) (? room2)))
                 (:guard (and
                           (not= (? room1) (? room2))
                           (not
                             (and
                               (some (fn [x]
                                       (println (str "PRT X: " x "  AND R2: " (? room2) " VISI: " (? visited-rooms)))
                                       (= (compare x (? room2)) 0)
                                       )

                                     (? visited-rooms)
                                     )


                               (some (fn [x]
                                       (println (str "PRT X: " x "  AND R1: " (? room1) " VISI: " (? visited-rooms)))
                                       (= (compare x (? room1)) 0)
                                       )

                                     (? visited-rooms)
                                     )
                               )
                             )
                           )
                   )
                 )

     :post     (
                 (in ?agent ?room1)
                 (opened ?door true)
                 (unlocked ?door true)
                 )

     :pre      ()
     :add      (
                 (protected ??visited-rooms ?room1)
                 (in ?agent ?room2)
                 )

     :del      (
                 (in ?agent ?room1)
                 )

     :txt      (agent ?agent has moved from ?room1 to ?room2)

     }
    }

  )

;test one - (time (ops-search move-A-D-all-unlocked '((in R K)) operations))
;test two - (time (ops-search move-A-D-all-unlocked '((in R K) (opened )) operations))
(def move-A-D-all-unlocked
  "A more advanced scenario"
  '#{
     ;define protection for rooms
     (protected)
     ;define agentLL
     (agent R)
     ;define rooms
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

     ;define doors
     (door A-B)
     (door A-C)
     (door A-D)
     (door B-E)
     (door C-F)
     (door D-K)
     (door E-G)
     (door G-H)
     (door E-I)
     (door I-J)

     ;define connections (connects door room)
     (connects A-B A)
     (connects A-B B)
     (connects A-C A)
     (connects A-C C)
     (connects A-D A)
     (connects A-D D)
     (connects B-E B)
     (connects B-E E)
     (connects C-F C)
     (connects C-F F)
     (connects D-K D)
     (connects D-K K)
     (connects E-G E)
     (connects E-G G)
     (connects E-I E)
     (connects E-I I)
     (connects G-H G)
     (connects G-H H)
     (connects I-J I)
     (connects I-J J)
     ;define where agent is in which room
     (in R A)
     ;define the state of the doors, open or closed
     (opened A-B true)
     (opened A-C true)
     (opened A-D true)
     (opened B-E true)
     (opened C-F true)
     (opened D-K true)
     (opened E-G true)
     (opened G-H true)
     (opened E-I true)
     (opened I-J true)
     ;define if the doors are locked or unlocked
     (unlocked A-B true)
     (unlocked A-C true)
     (unlocked A-D true)
     (unlocked B-E true)
     (unlocked C-F true)
     (unlocked D-K true)
     (unlocked E-G true)
     (unlocked G-H true)
     (unlocked E-I true)
     (unlocked I-J true)
     ;specify keys for the doors
     ;(key key-B-E)
     ;(holdable key-B-E)
     ;(unlocks key-B-E B-E)
     ;(in key-B-E B)

     ;(key key-A-D)
     ;(holdable key-A-D)
     ;(unlocks key-A-D A-D)
     ;(in key-A-D A)

     ;test 1-4
     ;(holds R key-A-D)

     ;test 5
     ;(holds R nil)
     }
  )

(def basic

  '#{
     (room A)
     (room B)
     (room C)
     (room D)

     (door A-B)
     (door B-C)
     (door C-D)

     })



;----------------------------------------------------
;----------------------------------------------------
;-------------------------Tests---------------------------
;----------------------------------------------------
;----------------------------------------------------

(defn test-move-to-d []
  "The agent moves from room A to room D
  This is a depth of one"
  (planner move-A-D-all-unlocked '(in R D) operations2)
  )

(defn test-move-to-b []
  "The agent moves from room A to room B
  This is a depth of one"
  (planner move-A-D-all-unlocked '(in R B) operations2)
  )

(defn test-move-to-c []
  "The agent moves from room A to room C
  This is a depth of one"
  (planner move-A-D-all-unlocked '(in R C) operations2)
  )

(defn test-move-to-f []
  "The agent moves from room A to room F
  This is a depth of two"
  (planner move-A-D-all-unlocked '(in R F) operations2)
  )


;-----------------------------------------------------
;-----------------------------------------------------
;-----------------------Tests with refactored code------------------------------
;-----------------------------------------------------
;-----------------------------------------------------

(defn test-move-to-d-refac []
  "The agent moves from room A to room D
  This is a depth of one"
  (planner move-A-D-all-unlocked '(in R D) josh-ops)
  )

(defn test-move-to-b-refac []
  "The agent moves from room A to room B
  This is a depth of one"
  (planner move-A-D-all-unlocked '(in R B) josh-ops)
  )

(defn test-move-to-c-refac []
  "The agent moves from room A to room C
  This is a depth of one"
  (planner move-A-D-all-unlocked '(in R C) josh-ops)
  )

(defn test-move-to-f-refac []
  "The agent moves from room A to room F
  This is a depth of two"
  (planner move-A-D-all-unlocked '(in R F) josh-ops)
  )
