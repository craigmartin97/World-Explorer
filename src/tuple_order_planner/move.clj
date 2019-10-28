(ns tuple-order-planner.move
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            [clojure.set :refer :all]
            [planner.planner :refer :all]
            [tuple-order-planner.state :refer :all]
            )
  )

;--------------------------------------
;--------------------------------------
;-----------------Planner operations---------------------
;--------------------------------------
;--------------------------------------


(def planner-operations-one
  "A map of operations that the agent can perform in the world
  This is the first iteration of the planner operations list."
  '{
    move
    {:name     move-agent
     :achieves (in ?agent ?room2)
     :when     ((agent ?agent)
                 (room ?room1)
                 (room ?room2)
                 (in ?agent ?room1)
                 )
     :post     ()
     :pre      ()
     :add      ((in ?agent ?room2)
                 )
     :del      ((in ?agent ?room1)
                 )
     :txt      (agent ?agent has moved from ?room1 to ?room2)
     }

    }
  )


(def planner-operations-two
  "A map of operations that the agent can perform in the world
  This is the first iteration of the planner operations list."
  '{
    move
    {:name     move-agent
     :achieves (in ?agent ?room2)
     :when     ((in ?agent ?room1)
                 (agent ?agent)
                 (room ?room1)
                 (room ?room2)
                 )
     :post     ()
     :pre      ()
     :add      ((in ?agent ?room2)
                 )
     :del      ((in ?agent ?room1)
                 )
     :txt      (agent ?agent has moved from ?room1 to ?room2)
     }

    }
  )


(def planner-operations-three
  "A map of operations that the agent can perform in the world
  This is the third iteration of the planners ops list. The (in ?agent ?room) has been adjusted
  and moved be (in ?agent ?room1)"
  '{
    move
    {:name     move-agent
     :achieves (in ?agent ?room2)
     :when     ((agent ?agent)
                 (in ?agent ?room1)
                 (room ?room1)
                 (room ?room2)
                 )
     :post     ()
     :pre      ()
     :add      ((in ?agent ?room2)
                 )
     :del      ((in ?agent ?room1)
                 )
     :txt      (agent ?agent has moved from ?room1 to ?room2)
     }

    }
  )


(def ops-operations
  "Ops search most efficent order"
  '{move {:pre ((agent ?agent)
                 (in ?agent ?room1)
                 (room ?room1)
                 (room ?room2)
                 )
          :add ((in ?agent ?room2))
          :del ((in ?agent ?room1))
          :txt (?agent has moved from ?room1 to ?room2)
          }

    })

;-------------------------------------------------------
;-------------------------------------------------------
;------------------------Tests-------------------------------
;-------------------------------------------------------
;-------------------------------------------------------

(defn test-one []
  "First test for the move operation for the planner.
  Elapsed time: 3.6572 msecs
  "
  (time (planner state-many-objs '(in R J) planner-operations-one))
  )

(defn test-two []
  "First test for the move operation for the planner.
  Elapsed time: 3.405 msecs
  "
  (time (planner state-many-objs '(in R J) planner-operations-two))
  )

(defn test-three []
  "First test for the move operation for the planner.
  Elapsed time: 2.5529 msecs
  "
  (time (planner state-many-objs '(in R J) planner-operations-three))
  )

(defn test-four-ops []
  "Ops search test in the most efficent order, same as planners

  Elapsed time: 5.6284 msecs"
  (time (ops-search state-many-objs '((in R J)) ops-operations))
  )
