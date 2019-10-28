(ns tuple-order_ops.unlock
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            )
  )

(def op-unlock-one
  '{unlock
    {:pre ((agent ?agent)
            (room ?room1)
            (door ?door)
            (opened ?door false)
            (unlocked ?door false)
            (in ?agent ?room1)
            (connects ?door ?room1)
            (key ?key)
            (holdable ?key)
            (unlocks ?key ?door)
            (holds ?agent ?key)
            )
     :add ((unlocked ?door true))
     :del ((unlocked ?door false))
     :txt (?agent has unlocked ?door)
     }
    }
  )

(def op-unlock-two
  '{lock
    {:pre ((in ?agent ?room1)
            (agent ?agent)
            (room ?room1)
            (holds ?agent ?key)
            (key ?key)
            (holdable ?key)
            (connects ?door ?room1)
            (door ?door)
            (opened ?door false)
            (unlocked ?door false)
            (unlocks ?key ?door)
            )
     :add ((unlocked ?door true))
     :del ((unlocked ?door false))
     :txt (?agent has unlocked ?door)
     }
    }
  )

(def op-unlock-three
  '{lock
    {:pre ((holds ?agent ?key)
            (agent ?agent)
            (key ?key)
            (holdable ?key)
            (in ?agent ?room1)
            (room ?room1)
            (connects ?door ?room1)
            (door ?door)
            (unlocks ?key ?door)
            (unlocked ?door false)
            (opened ?door false)
            )
     :add ((unlocked ?door true))
     :del ((unlocked ?door false))
     :txt (?agent has unlocked ?door)
     }
    }
  )

(def op-unlock-four
  '{lock
    {:pre ((holds ?agent ?key)
            (in ?agent ?room1)
            (unlocks ?key ?door)
            (connects ?door ?room1)
            (unlocked ?door false)
            (opened ?door false)
            (agent ?agent)
            (key ?key)
            (holdable ?key)
            (room ?room1)
            (door ?door)
            )
     :add ((unlocked ?door true))
     :del ((unlocked ?door false))
     :txt (?agent has unlocked ?door)
     }
    }
  )

(def op-unlock-five
  "Changing the tuple order like this brings a few things into question. It's slower than four, but will ensure earlier
  that the agent is holding a key. If we added the ability to hold other things, this saves against pointless checks.
  Are some checks irrelevant if the scenario is extended? Why does the key have to be unlocking a door and not a chest
  or other object? "
  '{lock
    {:pre ((holds ?agent ?key)
            (agent ?agent)
            (key ?key)
            (holdable ?key)
            (in ?agent ?room1)
            (unlocks ?key ?door)
            (connects ?door ?room1)
            (unlocked ?door false)
            (opened ?door false)
            (room ?room1)
            (door ?door)
            )
     :add ((unlocked ?door true))
     :del ((unlocked ?door false))
     :txt (?agent has unlocked ?door)
     }
    }
  )



(def state-unlock-small
  '#{
     (agent R)
     (room A)
     (room B)
     (door A-B)

     (connects A-B A)
     (connects A-B B)
     (opened A-B false)
     (unlocked A-B false)

     (in R A)

     (key key-A-B)
     (holdable key-A-B)
     (unlocks key-A-B A-B)
     (holds R key-A-B)
     }
  )

(def state-unlock-medium
  '#{
     (agent R)
     (room A)
     (room B)
     (room C)
     (room D)
     (room E)

     (door A-B)
     (door A-C)
     (door A-D)
     (door A-E)

     (connects A-B A)
     (connects A-B B)
     (connects A-C A)
     (connects A-C C)
     (connects A-D A)
     (connects A-D D)
     (connects A-E A)
     (connects A-E E)

     (opened A-B false)
     (opened A-C false)
     (opened A-D false)
     (opened A-E false)

     (unlocked A-B false)
     (unlocked A-C false)
     (unlocked A-D false)
     (unlocked A-E false)

     (in R A)

     (key key-A-B)
     (key key-A-C)
     (key key-A-D)
     (key key-A-E)
     (holdable key-A-B)
     (holdable key-A-C)
     (holdable key-A-D)
     (holdable key-A-E)
     (unlocks key-A-B A-B)
     (unlocks key-A-C A-C)
     (unlocks key-A-D A-D)
     (unlocks key-A-E A-E)
     (holds R key-A-B)
     (holds R key-A-C)
     (holds R key-A-D)
     (holds R key-A-E)
     }
  )

(def state-unlock-large
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
     (door A-C)
     (door A-D)
     (door A-E)
     (door A-F)
     (door A-G)
     (door A-H)
     (door A-I)
     (door A-J)

     (connects A-B A)
     (connects A-B B)
     (connects A-C A)
     (connects A-C C)
     (connects A-D A)
     (connects A-D D)
     (connects A-E A)
     (connects A-E E)
     (connects A-F A)
     (connects A-F F)
     (connects A-G A)
     (connects A-G G)
     (connects A-H A)
     (connects A-H H)
     (connects A-I A)
     (connects A-I I)
     (connects A-J A)
     (connects A-J J)

     (opened A-B false)
     (opened A-C false)
     (opened A-D false)
     (opened A-E false)
     (opened A-F false)
     (opened A-G false)
     (opened A-H false)
     (opened A-I false)
     (opened A-J false)

     (unlocked A-B false)
     (unlocked A-C false)
     (unlocked A-D false)
     (unlocked A-E false)
     (unlocked A-F false)
     (unlocked A-G false)
     (unlocked A-H false)
     (unlocked A-I false)
     (unlocked A-J false)

     (in R A)

     (key key-A-B)
     (key key-A-C)
     (key key-A-D)
     (key key-A-E)
     (key key-A-F)
     (key key-A-G)
     (key key-A-H)
     (key key-A-I)
     (key key-A-J)
     (holdable key-A-B)
     (holdable key-A-C)
     (holdable key-A-D)
     (holdable key-A-E)
     (holdable key-A-F)
     (holdable key-A-G)
     (holdable key-A-H)
     (holdable key-A-I)
     (holdable key-A-J)
     (unlocks key-A-B A-B)
     (unlocks key-A-C A-C)
     (unlocks key-A-D A-D)
     (unlocks key-A-E A-E)
     (unlocks key-A-F A-F)
     (unlocks key-A-G A-G)
     (unlocks key-A-H A-H)
     (unlocks key-A-I A-I)
     (unlocks key-A-J A-J)
     (holds R key-A-B)
     (holds R key-A-C)
     (holds R key-A-D)
     (holds R key-A-E)
     (holds R key-A-F)
     (holds R key-A-G)
     (holds R key-A-H)
     (holds R key-A-I)
     (holds R key-A-J)
     }
  )



(defn test-unlock-one-small []
  (time (ops-search state-unlock-small '((unlocked A-B true)) op-unlock-one))
  )

(defn test-unlock-one-medium []
  (time (ops-search state-unlock-medium '((unlocked A-B true) (unlocked A-C true) (unlocked A-D true) (unlocked A-E true)) op-unlock-one))
  )

(defn test-unlock-one-large []
  (time (ops-search state-unlock-large '((unlocked A-B true) (unlocked A-C true) (unlocked A-D true) (unlocked A-E true) (unlocked A-F true) (unlocked A-G true) (unlocked A-H true) (unlocked A-I true) (unlocked A-J true)) op-unlock-one))
  )



(defn test-unlock-two-small []
  (time (ops-search state-unlock-small '((unlocked A-B true)) op-unlock-two))
  )

(defn test-unlock-two-medium []
  (time (ops-search state-unlock-medium '((unlocked A-B true) (unlocked A-C true) (unlocked A-D true) (unlocked A-E true)) op-unlock-two))
  )

(defn test-unlock-two-large []
  (time (ops-search state-unlock-large '((unlocked A-B true) (unlocked A-C true) (unlocked A-D true) (unlocked A-E true) (unlocked A-F true) (unlocked A-G true) (unlocked A-H true) (unlocked A-I true) (unlocked A-J true)) op-unlock-two))
  )



(defn test-unlock-three-small []
  (time (ops-search state-unlock-small '((unlocked A-B true)) op-unlock-three))
  )

(defn test-unlock-three-medium []
  (time (ops-search state-unlock-medium '((unlocked A-B true) (unlocked A-C true) (unlocked A-D true) (unlocked A-E true)) op-unlock-three))
  )

(defn test-unlock-three-large []
  (time (ops-search state-unlock-large '((unlocked A-B true) (unlocked A-C true) (unlocked A-D true) (unlocked A-E true) (unlocked A-F true) (unlocked A-G true) (unlocked A-H true) (unlocked A-I true) (unlocked A-J true)) op-unlock-three))
  )



(defn test-unlock-four-small []
  (time (ops-search state-unlock-small '((unlocked A-B true)) op-unlock-four))
  )

(defn test-unlock-four-medium []
  (time (ops-search state-unlock-medium '((unlocked A-B true) (unlocked A-C true) (unlocked A-D true) (unlocked A-E true)) op-unlock-four))
  )

(defn test-unlock-four-large []
  (time (ops-search state-unlock-large '((unlocked A-B true) (unlocked A-C true) (unlocked A-D true) (unlocked A-E true) (unlocked A-F true) (unlocked A-G true) (unlocked A-H true) (unlocked A-I true) (unlocked A-J true)) op-unlock-four))
  )



(defn test-unlock-five-small []
  (time (ops-search state-unlock-small '((unlocked A-B true)) op-unlock-five))
  )

(defn test-unlock-five-medium []
  (time (ops-search state-unlock-medium '((unlocked A-B true) (unlocked A-C true) (unlocked A-D true) (unlocked A-E true)) op-unlock-five))
  )

(defn test-unlock-five-large []
  (time (ops-search state-unlock-large '((unlocked A-B true) (unlocked A-C true) (unlocked A-D true) (unlocked A-E true) (unlocked A-F true) (unlocked A-G true) (unlocked A-H true) (unlocked A-I true) (unlocked A-J true)) op-unlock-five))
  )