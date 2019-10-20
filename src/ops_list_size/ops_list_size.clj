(ns ops-list-size.ops-list-size
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]))


(def very-short-ops
  '{open {:pre ((agent ?agent)
                 (room ?room1)
                 (room ?room2)
                 (door ?door)
                 (opened ?door false)
                 (unlocked ?door true)
                 (connects ?door ?room1)
                 (connects ?door ?room2)
                 (in ?agent ?room1)
                 )
          :add ((opened ?door true))
          :del ((opened ?door false))
          :txt (?agent has opened ?door)
          }
    move {:pre ((agent ?agent)
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

(def short-ops
  "A map of operations that the agent can perform in the world"
  (merge very-short-ops
         '{unlock {:pre ((agent ?agent)
                          (room ?room1)
                          (room ?room2)
                          (door ?door)
                          (key ?key)
                          (holdable ?key)
                          (holds ?agent ?key)
                          (unlocks ?key ?door)
                          (opened ?door false)
                          (unlocked ?door false)
                          (in ?agent ?room1)
                          (connects ?door ?room1)
                          )
                   :add ((unlocked ?door true))
                   :del ((unlocked ?door false))
                   :txt (?agent has unlocked ?door)
                   }
           pickup {:pre ((agent ?agent)
                          (room ?room1)
                          (holdable ?obj)
                          (in ?agent ?room1)
                          (holds ?agent nil)
                          (in ?obj ?room1)
                          )
                   :add ((holds ?agent ?obj))
                   :del ((holds ?agent nil)
                          (in ?obj ?room1))
                   :txt (?agent picked up ?obj from ?room1)
                   }
           })
  )

(def base-ops
  (merge short-ops
         '{closed {:pre ((agent ?agent)
                          (room ?room1)
                          (room ?room2)
                          (door ?door)
                          (opened ?door true)
                          (unlocked ?door true)
                          (connects ?door ?room1)
                          (connects ?door ?room2)
                          (in ?agent ?room1)
                          )
                   :add ((opened ?door false))
                   :del ((opened ?door true))
                   :txt (?agent has closed ?door)
                   }
           lock {:pre ((agent ?agent)
                        (room ?room1)
                        (room ?room2)
                        (door ?door)
                        (key ?key)
                        (holdable ?key)
                        (holds ?agent ?key)
                        (unlocks ?key ?door)
                        (opened ?door false)
                        (unlocked ?door true)
                        (in ?agent ?room1)
                        (connects ?door ?room1)
                        )
                 :add ((unlocked ?door false))
                 :del ((unlocked ?door true))
                 :txt (?agent has locked ?door)
                 }
           drop {:pre ((agent ?agent)
                        (room ?room1)
                        (holdable ?obj)
                        (in ?agent ?room1)
                        (holds ?agent ?obj)
                        )
                 :add ((holds ?agent nil)
                        (in ?obj ?room1))
                 :del ((holds ?agent ?obj))
                 :txt (?agent dropped ?obj in ?room1)
                 }
           }
         )
  )

(def large-ops
  (merge base-ops
         '{destroy {:pre ((agent ?agent)
                           (holdable ?obj)
                           (holds ?agent ?obj)
                           )
                    :add ((holds ?agent nil))
                    :del ((holds ?agent ?obj)
                           (holdable ?obj))
                    :txt (?agent has destroyed ?obj)
                    }
           throw {:pre ((agent ?agent)
                         (holdable ?obj)
                         (room ?room1)
                         (room ?room2)
                         (door ?door)
                         (holds ?agent ?obj)
                         (in ?agent ?room1)
                         (connects ?door ?room1)
                         (connects ?door ?room2)
                         (unlocked ?door true)
                         (opened ?door true))
                  :add ((holds ?agent nil)
                         (in ?obj ?room2))
                  :del ((holds ?agent ?obj))
                  :txt ((?agent throws the ?obj into ?room2))}
           }
         )
  )

(def very-large-ops
  (merge large-ops
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
                     }
           power-up {:pre ((agent ?agent)
                            (room ?room)
                            (powerable ?powobj)
                            (holdable ?holdobj)
                            (charger ?holdobj)
                            (in ?agent ?room)
                            (powered ?powobj false)
                            (holds ?agent ?holdobj))
                     :add ((powered ? powobj true))
                     :del ((powered ?powobj false))
                     :txt ((?agent uses the ?holdobj to charge the ?powobj))
                     }
           power-down {:pre ((agent ?agent)
                              (room ?room)
                              (powerable ?powobj)
                              (holdable ?holdobj)
                              (charger ?holdobj)
                              (in ?agent ?room)
                              (powered ?powobj true)
                              (holds ?agent ?holdobj))
                       :add ((powered ? powobj false))
                       :del ((powered ?powobj true))
                       :txt ((?agent uses the ?holdobj to uncharge the ?powobj))
                       }
           pick-lock {:pre ((agent ?agent)
                             (holdable ?obj)
                             (lockpick ?obj)
                             (room ?room)
                             (door ?door)
                             (holds ?agent ?obj)
                             (in room ?agent)
                             (connects ?door ?room)
                             (opened ?door false)
                             (unlocked ?door false))
                      :add ((unlocked ?door true)
                             (holds ?agent nil))
                      :del ((unlocked ?door false)
                             (holds ?agent ?obj)
                             (holdable ?obj)
                             (lockpick ?obj))
                      :txt (?agent has picked the lock on ?door, breaking the ?obj)
                      }
           })
  )


;;;;;;;;;;;;;;
;;; TEST 1 ;;;
;;;;;;;;;;;;;;

(def test-state-1
  "Simple test using a very basic world. For ops-search, this should show that a larger operator list size will
  take longer to process even if those operators aren't applicable. All operation sets, excluding very-short-ops, aren't
  necessary for this scenarios optimal solution of moving from A-B.

  The impact of additional operations may be hard to see in this test as ops-search will return as soon as a match is
  found. This means that if the move operator is applied first then ops-search has no reason to check further."
  '#{(agent R)
     (room room-a)
     (room room-b)
     (door door-ab)
     (opened door-ab false)
     (unlocked door-ab true)
     (connects door-ab room-a)
     (connects door-ab room-b)
     (in R room-a)
     }
  )

(defn test-1-very-short []
  (time (ops-search test-state-1 '((in R room-b)) very-short-ops))
  )

(defn test-1-short []
  (time (ops-search test-state-1 '((in R room-b)) short-ops))
  )

(defn test-1-base []
  (time (ops-search test-state-1 '((in R room-b)) base-ops))
  )

(defn test-1-large []
  (time (ops-search test-state-1 '((in R room-b)) large-ops))
  )

(defn test-1-very-large []
  (time (ops-search test-state-1 '((in R room-b)) very-large-ops))
  )

;;;;;;;;;;;;;;
;;; TEST 2 ;;;
;;;;;;;;;;;;;;

(def test-state-2
  "This test handles the issue from test 1 where the impact of additional operations can be negated if the move operator
  is applied first. Ops-search will have to reach a depth of 4 before finding a solution, allowing each test to apply
  their whole operations list at least 3 times.

  Only the move, close, and open operators can be used in this scenario. These used operations will have a far
  greater impact on the time taken to find a solution than unused operators. It is true that all operators are checked
  for matches, but only operations that have matches will create additional paths in the search. This theory is tested
  and explained further in test 3.

  For this scenario, very-short-ops is the first operations list that can provide the optimal solution of moving from A-E."
  '#{(agent R)
     (room room-a)
     (room room-b)
     (room room-c)
     (room room-d)
     (room room-e)
     (door door-ab)
     (door door-bc)
     (door door-cd)
     (door door-de)
     (opened door-ab false)
     (opened door-bc false)
     (opened door-cd false)
     (opened door-de false)
     (unlocked door-ab true)
     (unlocked door-bc true)
     (unlocked door-cd true)
     (unlocked door-de true)
     (connects door-ab room-a)
     (connects door-ab room-b)
     (connects door-bc room-b)
     (connects door-bc room-c)
     (connects door-cd room-c)
     (connects door-cd room-d)
     (connects door-de room-d)
     (connects door-de room-e)
     (in R room-a)
     }
  )

(defn test-2-very-short []
  (time (ops-search test-state-2 '((in R room-e)) very-short-ops))
  )

(defn test-2-short []
  (time (ops-search test-state-2 '((in R room-e)) short-ops))
  )

(defn test-2-base []
  (time (ops-search test-state-2 '((in R room-e)) base-ops))
  )

(defn test-2-large []
  (time (ops-search test-state-2 '((in R room-e)) large-ops))
  )

(defn test-2-very-large []
  (time (ops-search test-state-2 '((in R room-e)) very-large-ops))
  )

;;;;;;;;;;;;;;
;;; TEST 3 ;;;
;;;;;;;;;;;;;;

(def test-state-3
  "Adds a single key to the scenario in test-state-2. All operation sets, excluding very-short, take
  far longer to create a correct answer in ops-search. This is because the pickup/drop/lock/unlock operators
  can now be applied which can add many additional depths before the agent reaches a goal state.

  This highlights that although additional operations do make ops-search take longer, the main time
  increase comes from the number of additional paths an operation allows ops-search to take."
  '#{(agent R)
     (room room-a)
     (room room-b)
     (room room-c)
     (room room-d)
     (room room-e)

     (door door-ab)
     (door door-bc)
     (door door-cd)
     (door door-de)
     (opened door-ab false)
     (opened door-bc false)
     (opened door-cd false)
     (opened door-de false)
     (unlocked door-ab true)
     (unlocked door-bc true)
     (unlocked door-cd true)
     (unlocked door-de true)

     (connects door-ab room-a)
     (connects door-ab room-b)
     (connects door-bc room-b)
     (connects door-bc room-c)
     (connects door-cd room-c)
     (connects door-cd room-d)
     (connects door-de room-d)
     (connects door-de room-e)

     (in R room-a)
     (holds R nil)

     (key key-ab)
     (holdable key-ab)
     (in key-ab room-a)
     }
  )

(defn test-3-very-short []
  (time (ops-search test-state-3 '((in R room-e)) very-short-ops))
  )

(defn test-3-short []
  (time (ops-search test-state-3 '((in R room-e)) short-ops))
  )

(defn test-3-base []
  (time (ops-search test-state-3 '((in R room-e)) base-ops))
  )

(defn test-3-large []
  (time (ops-search test-state-3 '((in R room-e)) large-ops))
  )

(defn test-3-very-large []
  (time (ops-search test-state-3 '((in R room-e)) very-large-ops))
  )



;;;;;;;;;;;;;;
;;; TEST 4 ;;;
;;;;;;;;;;;;;;

(def test-state-4
  '#{(agent R)
     (room room-a)
     (room room-b)
     (room room-c)
     (room room-d)
     (room room-e)
     (room room-f)
     (room room-g)
     (room room-h)
     (room room-i)
     (room room-j)

     (door door-ab)
     (door door-bc)
     (door door-cd)
     (door door-de)
     (door door-ef)
     (door door-fg)
     (door door-gh)
     (door door-hi)
     (door door-ij)
     (opened door-ab false)
     (opened door-bc false)
     (opened door-cd false)
     (opened door-de false)
     (opened door-ef false)
     (opened door-fg false)
     (opened door-gh false)
     (opened door-hi false)
     (opened door-ij false)
     (unlocked door-ab true)
     (unlocked door-bc true)
     (unlocked door-cd true)
     (unlocked door-de true)
     (unlocked door-ef true)
     (unlocked door-fg true)
     (unlocked door-gh true)
     (unlocked door-hi true)
     (unlocked door-ij true)

     (connects door-ab room-a)
     (connects door-ab room-b)
     (connects door-bc room-b)
     (connects door-bc room-c)
     (connects door-cd room-c)
     (connects door-cd room-d)
     (connects door-de room-d)
     (connects door-de room-e)
     (connects door-ef room-e)
     (connects door-ef room-f)
     (connects door-fg room-f)
     (connects door-fg room-g)
     (connects door-gh room-g)
     (connects door-gh room-h)
     (connects door-hi room-h)
     (connects door-hi room-i)
     (connects door-ij room-i)
     (connects door-ij room-j)

     (in R room-a)
     (holds R nil)

     (key key-ab)
     (holdable key-ab)
     (in key-ab room-a)
     }
  )

(defn test-4-very-short []
  (time (ops-search test-state-4 '((in R room-j)) very-short-ops))
  )

(defn test-4-short []
  (time (ops-search test-state-4 '((in R room-j)) short-ops))
  )

(defn test-4-base []
  (time (ops-search test-state-4 '((in R room-j)) base-ops))
  )

(defn test-4-large []
  (time (ops-search test-state-4 '((in R room-j)) large-ops))
  )

(defn test-4-very-large []
  (time (ops-search test-state-4 '((in R room-j)) very-large-ops))
  )