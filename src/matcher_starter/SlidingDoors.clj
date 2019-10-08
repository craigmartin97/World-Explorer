(ns sliding-doors
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]))



;; operations
(def ops
  '{
    open {
          :pre(
               (agent ?agent)                              ;; this agent
               (locked ?door false)                         ;;unlocked door
               (open ?door false)                          ;; closed door
               ;(at ?agent ?door)                           ;; agent at door
               (in ?agent ?room)
               (connects-to ?door ?room)
              )
          :add(
               (open ?door true)                           ;; door now open
              )
          :del(
               (open ?door false)                          ;; door no longer closed
              )
          :txt (?agent opens ?door)
          }
    close {
           :pre(
                (agent ?agent)
                (locked ?door false)
                (open ?door true)
                ;(at ?agent ?door)
                (in ?agent ?room)
                (connects-to ?door ?room)
               )
           :add(
                (open ?door false)
               )
           :del(
                (open ?door true)
               )
           :txt (?agent closes ?door)
          }
    lock {
          :pre(
               (agent ?agent)
               (key ?key)
               (manipulable ?key)
               (holds ?agent ?key)
               (locked ?door false)
               (open ?door false)
               ;(at ?agent ?door)
               (in ?agent ?room)
               (connects-to ?door ?room)
               (locks ?key ?door)
              )
          :add(
                (locked ?door true)
                (unlocks ?key ?door)
              )
          :del(
                (locked ?door false)
                (locks ?key ?door)
              )
          :txt (?agent locks ?door)
         }
    unlocks {
             :pre(
                  (agent ?agent)
                  (key ?key)
                  (manipulable ?key)
                  (holds ?agent ?key)
                  (locked ?door true)
                  (open ?door false)
                  ;(at ?agent ?door)
                  (in ?agent ?room)
                  (connects-to ?door ?room)
                  (unlocks ?key ?door)                  ;unlocks the door
                 )
             :add(
                  (locked ?door false)
                  (locks ?key ?door)
                 )
             :del(
                  (locked ?door true)
                  (unlocks ?key ?door)
                 )
             :txt((?agent unlocks ?door))
            }
    move {
          :pre(
                (agent ?agent)
                (in ?agent ?room1)
                (connects-to ?door ?room1)
                (connects-to ?door ?room2)
                ;(locked ?door false)                        ; door unlocked
                (open ?door true)                           ; door open
              )
          :add(
                (in ?agent ?room2)
              )
          :del(
               (in ?agent ?room1)
              )
          :cmd [
                 (print-out ?agent)
                ]
          :txt (?agent  moves from the ?room1 to ?room2)
         }
    }
  )

(defn print-out [state]
  (map (fn [x]
         (println x)
       )
       state
  )
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; state for opening the door
(def open-door-state
  '#{
     (agent R)
     (locked door false)
     (open door false)
     ;(at R door)
     (in R red-room)
     (connects-to door red-room)
    }
)

;; caller function for opening the door
(defn open-door []
  (ops-search open-door-state '((open door true))  ops)
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; state for closing the door
(def close-door-state
  '#{
     (agent R)
     (locked door false)
     (open door true)
     ;(at R door)
     (in R red-room)
     (connects-to door red-room)
     }
  )

;; caller function for closing the door
(defn close-door []
  (ops-search close-door-state '((open door false))  ops)
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; state for locking a door
(def lock-door-state
  '#{
     (agent R)
     (key red-key)
     (manipulable red-key)
     (holds R red-key)
     (locked door false)
     (open door false)
     ;(at R door)
     (in R red-room)
     (connects-to door red-room)
     (locks red-key door)
    }
)

;; caller function for locking a door
(defn lock-door []
  (ops-search lock-door-state '((locked door true))  ops)
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; state for locking a door
(def unlock-door-state
  '#{
     (agent R)
     (key red-key)
     (manipulable red-key)
     (holds R red-key)
     (locked door true)
     (open door false)
     ;(at R door)
     (in R red-room)
     (connects-to door red-room)
     (unlocks red-key door)
     }
)

;; caller function for locking a door
(defn unlock-door []
  (ops-search unlock-door-state '((locked door false))  ops)
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; state for locking a door
(def move-state
  '#{
     (agent R)
     (locked door true)
     (open door false)
     (in R red-room)
     (connects-to door red-room)
     (connects-to door blue-room)

     ;; add this to just open and walk through
     ;(locked door false)

     ;; add this to unlock, open and walk through
     (key key)
     (manipulable key)
     (holds R key)
     (unlocks key door)

    }
)



;; caller function for locking a door
(defn move-room []
  (ops-search move-state '(  (in R blue-room) (locked door true) ) ops)
)



