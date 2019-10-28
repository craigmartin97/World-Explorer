(ns tuple-order_ops.pickup
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            )
  )

(def operations
  "A map of operations that the agent can perform in the world"
  '{pickup
    {:pre ((agent ?agent)
            (room ?room1)
            (holdable ?obj)
            (in ?agent ?room1)
            (holds ?agent ??x)
            (in ?obj ?room1)
            )
     :add ((holds ?agent ?obj)
            (holds ?agent ??x)
            )
     :del ((holds ?agent ??x)
            (in ?obj ?room1)
            )
     :txt (?agent picked up ?obj from ?room1)
     }
    }
  )

(def operations-two
  "A map of operations that the agent can perform in the world"
  '{pickup
    {:pre ((in ?agent ?room1)
            (agent ?agent)
            (room ?room1)
            (holdable ?obj)
            (holds ?agent ??x)
            (in ?obj ?room1)
            )
     :add ((holds ?agent ?obj)
            (holds ?agent ??x)

            )
     :del ((holds ?agent ??x)
            (in ?obj ?room1)
            )
     :txt (?agent picked up ?obj from ?room1)
     }
    }
  )

(def operations-three
  "A map of operations that the agent can perform in the world"
  '{pickup
    {:pre ((in ?agent ?room1)
            (holds ?agent ??x)
            (in ?obj ?room1)
            (agent ?agent)
            (room ?room1)
            (holdable ?obj)
            )
     :add ((holds ?agent ?obj)
            (holds ?agent ??x)
            )
     :del ((holds ?agent ??x)
            (in ?obj ?room1)
            )
     :txt (?agent picked up ?obj from ?room1)
     }
    }
  )

(def operations-four
  "A map of operations that the agent can perform in the world"
  '{pickup
    {:pre ((in ?obj ?room1)
            (holdable ?obj)
            (in ?agent ?room1)
            (holds ?agent ??x)
            (agent ?agent)
            (room ?room1)
            )
     :add ((holds ?agent ?obj)
            (holds ?agent ??x)
            )
     :del ((holds ?agent ??x)
            (in ?obj ?room1)
            )
     :txt (?agent picked up ?obj from ?room1)
     }
    }
  )

;---------------------------------------------------------------
;---------------------------------------------------------------
;---------------------------States------------------------------------
;---------------------------------------------------------------
;---------------------------------------------------------------

(def state-pickup-one
  "Small state"
  '#{
     (agent R)
     (room A)
     (in R A)
     (holds R nil)

     (holdable key)
     (in key A)
     })

(def state-pickup-two
  "State with 4 objects"
  '#{
     (agent R)
     (room A)
     (in R A)
     (holds R nil)

     (holdable key)
     (in key A)

     (holdable dog)
     (in dog A)

     (holdable cat)
     (in cat A)

     (holdable mouse)
     (in mouse A)
     })

(def state-pickup-three
  "State with 6 objects"
  '#{
     (agent R)
     (room A)
     (in R A)
     (holds R nil)

     (holdable key)
     (in key A)

     (holdable dog)
     (in dog A)

     (holdable cat)
     (in cat A)

     (holdable mouse)
     (in mouse A)

     (holdable case)
     (in case A)

     (holdable remote)
     (in remote A)
     })

(def state-pickup-four
  "State with 8 objects"
  '#{
     (agent R)
     (room A)
     (in R A)
     (holds R nil)

     (holdable key)
     (in key A)

     (holdable dog)
     (in dog A)

     (holdable cat)
     (in cat A)

     (holdable mouse)
     (in mouse A)

     (holdable case)
     (in case A)

     (holdable remote)
     (in remote A)

     (holdable hammer)
     (in hammer A)

     (holdable watch)
     (in watch A)
     })

(def state-pickup-five
  "state with 12 objects"
  '#{
     (agent R)
     (room A)
     (in R A)
     (holds R nil)

     (holdable key)
     (in key A)

     (holdable dog)
     (in dog A)

     (holdable cat)
     (in cat A)

     (holdable mouse)
     (in mouse A)

     (holdable case)
     (in case A)

     (holdable remote)
     (in remote A)

     (holdable hammer)
     (in hammer A)

     (holdable watch)
     (in watch A)

     (holdable chips)
     (in chips A)

     (holdable mango)
     (in mango A)

     (holdable apple)
     (in apple A)

     (holdable olive)
     (in olive A)
     })

(def state-pickup-six
  "state with 24 objects"
  '#{
     (agent R)
     (room A)
     (in R A)
     (holds R nil)

     (holdable key)
     (in key A)

     (holdable dog)
     (in dog A)

     (holdable cat)
     (in cat A)

     (holdable mouse)
     (in mouse A)

     (holdable case)
     (in case A)

     (holdable remote)
     (in remote A)

     (holdable hammer)
     (in hammer A)

     (holdable watch)
     (in watch A)

     (holdable chips)
     (in chips A)

     (holdable mango)
     (in mango A)

     (holdable apple)
     (in apple A)

     (holdable olive)
     (in olive A)

     (holdable guitar)
     (in guitar A)

     (holdable drum)
     (in drum A)

     (holdable phone)
     (in phone A)

     (holdable cpu)
     (in cpu A)

     (holdable poster)
     (in poster A)

     (holdable pen)
     (in pen A)

     (holdable calc)
     (in calc A)

     (holdable pc)
     (in pc A)

     (holdable glass)
     (in glass A)

     (holdable coffee)
     (in coffee A)

     (holdable tea)
     (in tea A)

     (holdable football)
     (in football A)
     })

;----------------------------------------------------------------
;----------------------------------------------------------------
;---------------------------tests-------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------

;----------------------------------------------------------------
;-----------------------------tests with base operations-----------------------------------
;----------------------------------------------------------------


(defn test-one []
  "Elapsed time: 3.6934 msecs

  Agent picks up 1 object"
  (time (ops-search state-pickup-one '((holds R key)) operations :debug true))
  )

(defn test-two []
  "Elapsed time: 7661.6265 msecs

  Agent picks up 4 objects"
  (time (ops-search state-pickup-two '((holds R key)
                                        (holds R dog)
                                        (holds R cat)
                                        (holds R mouse)
                                        ) operations :debug true))
  )

(defn test-three []
  "
  Elapsed time: unsolvable msecs

  Agent picks up 6 objects"
  (time (ops-search state-pickup-three '(
                                          (holds R key)
                                          (holds R dog)
                                          (holds R cat)
                                          (holds R mouse)
                                          (holds R case)
                                          (holds R remote)
                                          ) operations :debug true))
  )


(defn test-four []
  "Elapsed time: unsolvable msecs

  Agent picks up 8 objects"
  (time (ops-search state-pickup-four '(
                                         (holds R key)
                                         (holds R dog)
                                         (holds R cat)
                                         (holds R mouse)
                                         (holds R case)
                                         (holds R remote)
                                         (holds R hammer)
                                         (holds R watch)
                                         ) operations :debug true))
  )

(defn test-five []
  "Elapsed time: unsolvable msecs

  Agent picks up 12 objects"
  (time (ops-search state-pickup-five '(
                                         (holds R key)
                                         (holds R dog)
                                         (holds R cat)
                                         (holds R mouse)
                                         (holds R case)
                                         (holds R remote)
                                         (holds R hammer)
                                         (holds R watch)
                                         (holds R chips)
                                         (holds R mango)
                                         (holds R apple)
                                         (holds R olive)
                                         ) operations :debug true))
  )


(defn test-six []
  "Elapsed time: unsolvable msecs

  Agent picks up 24 objects"
  (time (ops-search state-pickup-six '(
                                        (holds R key)
                                        (holds R dog)
                                        (holds R cat)
                                        (holds R mouse)
                                        (holds R case)
                                        (holds R remote)
                                        (holds R hammer)
                                        (holds R watch)
                                        (holds R chips)
                                        (holds R mango)
                                        (holds R apple)
                                        (holds R olive)
                                        (holds R guitar)
                                        (holds R drum)
                                        (holds R phone)
                                        (holds R cpu)
                                        (holds R poster)
                                        (holds R pen)
                                        (holds R calc)
                                        (holds R pc)
                                        (holds R glass)
                                        (holds R coffee)
                                        (holds R tea)
                                        (holds R football)
                                        ) operations :debug true))
  )

;----------------------------------------------------------------
;-----------------------------tests with  operations two-----------------------------------
;----------------------------------------------------------------

(defn test-seven []
  "Elapsed time: 3.3458 msecs

  Agent picks up 1 object"
  (time (ops-search state-pickup-one '((holds R key)) operations-two :debug true))
  )

(defn test-eight []
  "Elapsed time: 7661.6265 msecs

  Agent picks up 4 objects"
  (time (ops-search state-pickup-two '((holds R key)
                                        (holds R dog)
                                        (holds R cat)
                                        (holds R mouse)
                                        ) operations-two :debug true))
  )

(defn test-nine []
  "
  Elapsed time: unsolvable msecs

  Agent picks up 6 objects"
  (time (ops-search state-pickup-three '(
                                          (holds R key)
                                          (holds R dog)
                                          (holds R cat)
                                          (holds R mouse)
                                          (holds R case)
                                          (holds R remote)
                                          ) operations-two :debug true))
  )


(defn test-ten []
  "Elapsed time: unsolvable msecs

  Agent picks up 8 objects"
  (time (ops-search state-pickup-four '(
                                         (holds R key)
                                         (holds R dog)
                                         (holds R cat)
                                         (holds R mouse)
                                         (holds R case)
                                         (holds R remote)
                                         (holds R hammer)
                                         (holds R watch)
                                         ) operations-two :debug true))
  )

(defn test-eleven []
  "Elapsed time: unsolvable msecs

  Agent picks up 12 objects"
  (time (ops-search state-pickup-five '(
                                         (holds R key)
                                         (holds R dog)
                                         (holds R cat)
                                         (holds R mouse)
                                         (holds R case)
                                         (holds R remote)
                                         (holds R hammer)
                                         (holds R watch)
                                         (holds R chips)
                                         (holds R mango)
                                         (holds R apple)
                                         (holds R olive)
                                         ) operations-two :debug true))
  )


(defn test-twelve []
  "Elapsed time: unsolvable msecs

  Agent picks up 24 objects"
  (time (ops-search state-pickup-six '(
                                        (holds R key)
                                        (holds R dog)
                                        (holds R cat)
                                        (holds R mouse)
                                        (holds R case)
                                        (holds R remote)
                                        (holds R hammer)
                                        (holds R watch)
                                        (holds R chips)
                                        (holds R mango)
                                        (holds R apple)
                                        (holds R olive)
                                        (holds R guitar)
                                        (holds R drum)
                                        (holds R phone)
                                        (holds R cpu)
                                        (holds R poster)
                                        (holds R pen)
                                        (holds R calc)
                                        (holds R pc)
                                        (holds R glass)
                                        (holds R coffee)
                                        (holds R tea)
                                        (holds R football)
                                        ) operations-two :debug true))
  )

;----------------------------------------------------------------
;-----------------------------tests with  operations three-----------------------------------
;----------------------------------------------------------------

(defn test-thirteen []
  "Elapsed time: 3.3458 msecs

  Agent picks up 1 object"
  (time (ops-search state-pickup-one '((holds R key)) operations-three :debug true))
  )

(defn test-fourteen []
  "Elapsed time: 7661.6265 msecs

  Agent picks up 4 objects"
  (time (ops-search state-pickup-two '((holds R key)
                                        (holds R dog)
                                        (holds R cat)
                                        (holds R mouse)
                                        ) operations-three :debug true))
  )

(defn test-fifteen []
  "
  Elapsed time: unsolvable msecs

  Agent picks up 6 objects"
  (time (ops-search state-pickup-three '(
                                          (holds R key)
                                          (holds R dog)
                                          (holds R cat)
                                          (holds R mouse)
                                          (holds R case)
                                          (holds R remote)
                                          ) operations-three :debug true))
  )


(defn test-sixteen []
  "Elapsed time: unsolvable msecs

  Agent picks up 8 objects"
  (time (ops-search state-pickup-four '(
                                         (holds R key)
                                         (holds R dog)
                                         (holds R cat)
                                         (holds R mouse)
                                         (holds R case)
                                         (holds R remote)
                                         (holds R hammer)
                                         (holds R watch)
                                         ) operations-three :debug true))
  )

(defn test-seventeen []
  "Elapsed time: unsolvable msecs

  Agent picks up 12 objects"
  (time (ops-search state-pickup-five '(
                                         (holds R key)
                                         (holds R dog)
                                         (holds R cat)
                                         (holds R mouse)
                                         (holds R case)
                                         (holds R remote)
                                         (holds R hammer)
                                         (holds R watch)
                                         (holds R chips)
                                         (holds R mango)
                                         (holds R apple)
                                         (holds R olive)
                                         ) operations-three :debug true))
  )


(defn test-eighteen []
  "Elapsed time: unsolvable msecs

  Agent picks up 24 objects"
  (time (ops-search state-pickup-six '(
                                        (holds R key)
                                        (holds R dog)
                                        (holds R cat)
                                        (holds R mouse)
                                        (holds R case)
                                        (holds R remote)
                                        (holds R hammer)
                                        (holds R watch)
                                        (holds R chips)
                                        (holds R mango)
                                        (holds R apple)
                                        (holds R olive)
                                        (holds R guitar)
                                        (holds R drum)
                                        (holds R phone)
                                        (holds R cpu)
                                        (holds R poster)
                                        (holds R pen)
                                        (holds R calc)
                                        (holds R pc)
                                        (holds R glass)
                                        (holds R coffee)
                                        (holds R tea)
                                        (holds R football)
                                        ) operations-three :debug true))
  )

;----------------------------------------------------------------
;-----------------------------tests with  operations four-----------------------------------
;----------------------------------------------------------------

(defn test-nineteen []
  "Elapsed time: 3.3458 msecs

  Agent picks up 1 object"
  (time (ops-search state-pickup-one '((holds R key)) operations-four :debug true))
  )

(defn test-twenty []
  "Elapsed time: 7661.6265 msecs

  Agent picks up 4 objects"
  (time (ops-search state-pickup-two '((holds R key)
                                        (holds R dog)
                                        (holds R cat)
                                        (holds R mouse)
                                        ) operations-four :debug true))
  )

(defn test-twenty-one []
  "
  Elapsed time: 1423077.7891 msecs msecs

  Agent picks up 6 objects"
  (time (ops-search state-pickup-three '(
                                          (holds R key)
                                          (holds R dog)
                                          (holds R cat)
                                          (holds R mouse)
                                          (holds R case)
                                          (holds R remote)
                                          ) operations-four :debug true))
  )


(defn test-twenty-two []
  "Elapsed time: unsolvable msecs

  Agent picks up 8 objects"
  (time (ops-search state-pickup-four '(
                                         (holds R key)
                                         (holds R dog)
                                         (holds R cat)
                                         (holds R mouse)
                                         (holds R case)
                                         (holds R remote)
                                         (holds R hammer)
                                         (holds R watch)
                                         ) operations-four :debug true))
  )

(defn test-twenty-three []
  "Elapsed time: unsolvable msecs

  Agent picks up 12 objects"
  (time (ops-search state-pickup-five '(
                                         (holds R key)
                                         (holds R dog)
                                         (holds R cat)
                                         (holds R mouse)
                                         (holds R case)
                                         (holds R remote)
                                         (holds R hammer)
                                         (holds R watch)
                                         (holds R chips)
                                         (holds R mango)
                                         (holds R apple)
                                         (holds R olive)
                                         ) operations-four :debug true))
  )


(defn test-twenty-four []
  "Elapsed time: unsolvable msecs

  Agent picks up 24 objects"
  (time (ops-search state-pickup-six '(
                                        (holds R key)
                                        (holds R dog)
                                        (holds R cat)
                                        (holds R mouse)
                                        (holds R case)
                                        (holds R remote)
                                        (holds R hammer)
                                        (holds R watch)
                                        (holds R chips)
                                        (holds R mango)
                                        (holds R apple)
                                        (holds R olive)
                                        (holds R guitar)
                                        (holds R drum)
                                        (holds R phone)
                                        (holds R cpu)
                                        (holds R poster)
                                        (holds R pen)
                                        (holds R calc)
                                        (holds R pc)
                                        (holds R glass)
                                        (holds R coffee)
                                        (holds R tea)
                                        (holds R football)
                                        ) operations-four :debug true))
  )