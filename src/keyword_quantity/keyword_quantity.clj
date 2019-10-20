(ns keyword-quantity.keyword-quantity
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]))

(def base-ops
  "A map of operations that the agent can perform in the world"
  '{
    open
    {
     :pre
          (
            (agent ?agent)
            (room ?room1)
            (room ?room2)
            (door ?door)
            (opened ?door false)
            (unlocked ?door true)
            (connects ?door ?room1)
            (connects ?door ?room2)
            (in ?agent ?room1)
            )
     :add
          (
            (opened ?door true)
            )
     :del
          (
            (opened ?door false)
            )
     :txt (?agent has opened ?door)
     }

    closed
    {
     :pre
          (
            (agent ?agent)
            (room ?room1)
            (room ?room2)
            (door ?door)
            (opened ?door true)
            (unlocked ?door true)
            (connects ?door ?room1)
            (connects ?door ?room2)
            (in ?agent ?room1)
            )
     :add
          (
            (opened ?door false)
            )
     :del
          (
            (opened ?door true)
            )
     :txt (?agent has closed ?door)
     }

    lock
    {
     :pre
          (
            (agent ?agent)
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
     :add
          (
            (unlocked ?door false)
            )
     :del
          (
            (unlocked ?door true)
            )
     :txt (?agent has locked ?door)
     }

    unlock
    {
     :pre
          (
            (agent ?agent)
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
     :add
          (
            (unlocked ?door true)
            )
     :del
          (
            (unlocked ?door false)
            )
     :txt (?agent has unlocked ?door)
     }

    move
    {
     :pre
          (
            (agent ?agent)
            (room ?room1)
            (room ?room2)
            (door ?door)
            (opened ?door true)
            (unlocked ?door true)
            (connects ?door ?room1)
            (connects ?door ?room2)
            (in ?agent ?room1)
            )
     :add
          (
            (in ?agent ?room2)
            )
     :del
          (
            (in ?agent ?room1)
            )
     :txt (?agent has moved from ?room1 to ?room2)
     }

    pickup
    {
     :pre
          (
            (agent ?agent)
            (room ?room1)
            (holdable ?obj)
            (in ?agent ?room1)
            (holds ?agent nil)
            (in ?obj ?room1)
            )
     :add
          (
            (holds ?agent ?obj)
            )
     :del
          (
            (holds ?agent nil)
            (in ?obj ?room1)
            )
     :txt (?agent picked up ?obj from ?room1)
     }

    drop
    {
     :pre
          (
            (agent ?agent)
            (room ?room1)
            (holdable ?obj)
            (in ?agent ?room1)
            (holds ?agent ?obj)
            )
     :add
          (
            (holds ?agent nil)
            (in ?obj ?room1)
            )
     :del
          (
            (holds ?agent ?obj)
            )
     :txt (?agent dropped ?obj in ?room1)
     }
    }
  )

(def extended-keyword-ops-a
  "Similar to base-ops, but with the following changes to keywords:
  (Opened ?door true/false) -> (Opened ?door) (Closed ?door)
  (Unlocked ?door true/false) -> (Unlocked ?door) (Locked ?door)"
  '{
    open
    {
     :pre
          (
            (agent ?agent)
            (room ?room1)
            (room ?room2)
            (door ?door)
            (closed ?door)
            (unlocked ?door)
            (connects ?door ?room1)
            (connects ?door ?room2)
            (in ?agent ?room1)
            )
     :add
          (
            (opened ?door)
            )
     :del
          (
            (closed ?door)
            )
     :txt (?agent has opened ?door)
     }

    closed
    {
     :pre
          (
            (agent ?agent)
            (room ?room1)
            (room ?room2)
            (door ?door)
            (opened ?door)
            (unlocked ?door)
            (connects ?door ?room1)
            (connects ?door ?room2)
            (in ?agent ?room1)
            )
     :add
          (
            (closed ?door)
            )
     :del
          (
            (opened ?door)
            )
     :txt (?agent has closed ?door)
     }

    lock
    {
     :pre
          (
            (agent ?agent)
            (room ?room1)
            (room ?room2)
            (door ?door)
            (key ?key)
            (holdable ?key)
            (holds ?agent ?key)
            (unlocks ?key ?door)
            (closed ?door)
            (unlocked ?door)
            (in ?agent ?room1)
            (connects ?door ?room1)
            )
     :add
          (
            (locked ?door)
            )
     :del
          (
            (unlocked ?door)
            )
     :txt (?agent has locked ?door)
     }

    unlock
    {
     :pre
          (
            (agent ?agent)
            (room ?room1)
            (room ?room2)
            (door ?door)
            (key ?key)
            (holdable ?key)
            (holds ?agent ?key)
            (unlocks ?key ?door)
            (closed ?door)
            (locked ?door)
            (in ?agent ?room1)
            (connects ?door ?room1)
            )
     :add
          (
            (unlocked ?door)
            )
     :del
          (
            (locked ?door)
            )
     :txt (?agent has unlocked ?door)
     }

    move
    {
     :pre
          (
            (agent ?agent)
            (room ?room1)
            (room ?room2)
            (door ?door)
            (opened ?door)
            (unlocked ?door)
            (connects ?door ?room1)
            (connects ?door ?room2)
            (in ?agent ?room1)
            )
     :add
          (
            (in ?agent ?room2)
            )
     :del
          (
            (in ?agent ?room1)
            )
     :txt (?agent has moved from ?room1 to ?room2)
     }

    pickup
    {
     :pre
          (
            (agent ?agent)
            (room ?room1)
            (holdable ?obj)
            (in ?agent ?room1)
            (holds ?agent nil)
            (in ?obj ?room1)
            )
     :add
          (
            (holds ?agent ?obj)
            )
     :del
          (
            (holds ?agent nil)
            (in ?obj ?room1)
            )
     :txt (?agent picked up ?obj from ?room1)
     }

    drop
    {
     :pre
          (
            (agent ?agent)
            (room ?room1)
            (holdable ?obj)
            (in ?agent ?room1)
            (holds ?agent ?obj)
            )
     :add
          (
            (holds ?agent nil)
            (in ?obj ?room1)
            )
     :del
          (
            (holds ?agent ?obj)
            )
     :txt (?agent dropped ?obj in ?room1)
     }
    }
  )

(def extended-keyword-ops-b
  "Alternate version of extended-keyword-ops-a to ensure tuple lengths are the same:
  (Opened ?door) -> (Opened ?door true)
  (Close ?door) -> (Closed ?door true)
  (Unlocked ?door) -> (Unlocked ?door true)
  (Locked ?door) -> (Locked ?door true)"
  '{
    open
    {
     :pre
          (
            (agent ?agent)
            (room ?room1)
            (room ?room2)
            (door ?door)
            (closed ?door true)
            (unlocked ?door true)
            (connects ?door ?room1)
            (connects ?door ?room2)
            (in ?agent ?room1)
            )
     :add
          (
            (opened ?door true)
            )
     :del
          (
            (closed ?door true)
            )
     :txt (?agent has opened ?door)
     }

    closed
    {
     :pre
          (
            (agent ?agent)
            (room ?room1)
            (room ?room2)
            (door ?door)
            (opened ?door true)
            (unlocked ?door true)
            (connects ?door ?room1)
            (connects ?door ?room2)
            (in ?agent ?room1)
            )
     :add
          (
            (closed ?door true)
            )
     :del
          (
            (opened ?door true)
            )
     :txt (?agent has closed ?door)
     }

    lock
    {
     :pre
          (
            (agent ?agent)
            (room ?room1)
            (room ?room2)
            (door ?door)
            (key ?key)
            (holdable ?key)
            (holds ?agent ?key)
            (unlocks ?key ?door)
            (closed ?door true)
            (unlocked ?door true)
            (in ?agent ?room1)
            (connects ?door ?room1)
            )
     :add
          (
            (locked ?door true)
            )
     :del
          (
            (unlocked ?door true)
            )
     :txt (?agent has locked ?door)
     }

    unlock
    {
     :pre
          (
            (agent ?agent)
            (room ?room1)
            (room ?room2)
            (door ?door)
            (key ?key)
            (holdable ?key)
            (holds ?agent ?key)
            (unlocks ?key ?door)
            (closed ?door true)
            (locked ?door true)
            (in ?agent ?room1)
            (connects ?door ?room1)
            )
     :add
          (
            (unlocked ?door true)
            )
     :del
          (
            (locked ?door true)
            )
     :txt (?agent has unlocked ?door)
     }

    move
    {
     :pre
          (
            (agent ?agent)
            (room ?room1)
            (room ?room2)
            (door ?door)
            (opened ?door true)
            (unlocked ?door true)
            (connects ?door ?room1)
            (connects ?door ?room2)
            (in ?agent ?room1)
            )
     :add
          (
            (in ?agent ?room2)
            )
     :del
          (
            (in ?agent ?room1)
            )
     :txt (?agent has moved from ?room1 to ?room2)
     }

    pickup
    {
     :pre
          (
            (agent ?agent)
            (room ?room1)
            (holdable ?obj)
            (in ?agent ?room1)
            (holds ?agent nil)
            (in ?obj ?room1)
            )
     :add
          (
            (holds ?agent ?obj)
            )
     :del
          (
            (holds ?agent nil)
            (in ?obj ?room1)
            )
     :txt (?agent picked up ?obj from ?room1)
     }

    drop
    {
     :pre
          (
            (agent ?agent)
            (room ?room1)
            (holdable ?obj)
            (in ?agent ?room1)
            (holds ?agent ?obj)
            )
     :add
          (
            (holds ?agent nil)
            (in ?obj ?room1)
            )
     :del
          (
            (holds ?agent ?obj)
            )
     :txt (?agent dropped ?obj in ?room1)
     }
    }
  )

;;;;;;;;;;;;;;
;;; TEST 1 ;;;
;;;;;;;;;;;;;;

(def base-move-state
  '#{(agent R)
     (room room-a)
     (room room-b)
     (door door)
     (opened door false)
     (unlocked door true)
     (connects door room-a)
     (connects door room-b)
     (in R room-a)
     }
  )

(defn test-base-1 []
  (time (ops-search base-move-state '((in R room-b)) base-ops))
  )

(def extended-keyword-move-state
  '#{(agent R)
     (room room-a)
     (room room-b)
     (door door)
     (closed door)
     (unlocked door)
     (connects door room-a)
     (connects door room-b)
     (in R room-a)
     }
  )

(defn test-ext-1 []
  (time (ops-search extended-keyword-move-state '((in R room-b)) extended-keyword-ops-a))
  )

;;;;;;;;;;;;;;;
;;; TEST 2A ;;;
;;;;;;;;;;;;;;;

(def base-room-chain-state
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

(defn test-base-2 []
  (time (ops-search base-room-chain-state '((in R room-e)) base-ops))
  )

(def extended-keyword-room-chain-state-a
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
     (closed door-ab)
     (closed door-bc)
     (closed door-cd)
     (closed door-de)
     (unlocked door-ab)
     (unlocked door-bc)
     (unlocked door-cd)
     (unlocked door-de)
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

(defn test-ext-2a []
  (time (ops-search extended-keyword-room-chain-state-a '((in R room-e)) extended-keyword-ops-a))
  )

;;;;;;;;;;;;;;;
;;; TEST 2B ;;;
;;;;;;;;;;;;;;;

(def extended-keyword-room-chain-state-b
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
     (closed door-ab true)
     (closed door-bc true)
     (closed door-cd true)
     (closed door-de true)
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

(defn test-ext-2b []
  (time (ops-search extended-keyword-room-chain-state-b '((in R room-e)) extended-keyword-ops-b))
  )

;;;;;;;;;;;;;;;
;;; TEST 3 ;;;
;;;;;;;;;;;;;;;

(def base-room-long-chain-state
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
     }
  )

(defn test-base-3 []
  (time (ops-search base-room-long-chain-state '((in R room-j)) base-ops))
  )

(def extended-keyword-room-long-chain-state-a
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
     (closed door-ab)
     (closed door-bc)
     (closed door-cd)
     (closed door-de)
     (closed door-ef)
     (closed door-fg)
     (closed door-gh)
     (closed door-hi)
     (closed door-ij)
     (unlocked door-ab)
     (unlocked door-bc)
     (unlocked door-cd)
     (unlocked door-de)
     (unlocked door-ef)
     (unlocked door-fg)
     (unlocked door-gh)
     (unlocked door-hi)
     (unlocked door-ij)
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
     }
  )

(defn test-ext-3a []
  (time (ops-search extended-keyword-room-long-chain-state-a '((in R room-j)) extended-keyword-ops-a))
  )

;;;;;;;;;;;;;;;
;;; TEST 3B ;;;
;;;;;;;;;;;;;;;

(def extended-keyword-room-long-chain-state-b
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
     (closed door-ab)
     (closed door-bc)
     (closed door-cd)
     (closed door-de)
     (closed door-ef)
     (closed door-fg)
     (closed door-gh)
     (closed door-hi)
     (closed door-ij)
     (unlocked door-ab)
     (unlocked door-bc)
     (unlocked door-cd)
     (unlocked door-de)
     (unlocked door-ef)
     (unlocked door-fg)
     (unlocked door-gh)
     (unlocked door-hi)
     (unlocked door-ij)
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
     }
  )

(defn test-ext-3b []
  (time (ops-search extended-keyword-room-long-chain-state-b '((in R room-j)) extended-keyword-ops-b))
  )

;;;;;;;;;;;;;;;
;;; TEST 4A ;;;
;;;;;;;;;;;;;;;

(def base-locked-room-chain-state
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
     (unlocked door-ab false)
     (unlocked door-bc false)
     (unlocked door-cd false)
     (unlocked door-de false)
     (connects door-ab room-a)
     (connects door-ab room-b)
     (connects door-bc room-b)
     (connects door-bc room-c)
     (connects door-cd room-c)
     (connects door-cd room-d)
     (connects door-de room-d)
     (connects door-de room-e)
     (key key-ab)
     (key key-bc)
     (key key-cd)
     (key key-de)
     (holdable key-ab)
     (holdable key-bc)
     (holdable key-cd)
     (holdable key-de)
     (unlocks key-ab door-ab)
     (unlocks key-bc door-bc)
     (unlocks key-cd door-cd)
     (unlocks key-de door-de)
     (in key-ab room-a)
     (in key-bc room-b)
     (in key-cd room-c)
     (in key-de room-d)
     (in R room-a)
     (holds R nil)
     }
  )

(defn test-base-4 []
  (time (ops-search base-locked-room-chain-state '((unlocked door-)) base-ops))
  )

(def extended-keyword-locked-room-chain-state-a
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
     (closed door-ab)
     (closed door-bc)
     (closed door-cd)
     (closed door-de)
     (locked door-ab)
     (locked door-bc)
     (locked door-cd)
     (locked door-de)
     (connects door-ab room-a)
     (connects door-ab room-b)
     (connects door-bc room-b)
     (connects door-bc room-c)
     (connects door-cd room-c)
     (connects door-cd room-d)
     (connects door-de room-d)
     (connects door-de room-e)
     (key key-ab)
     (key key-bc)
     (key key-cd)
     (key key-de)
     (holdable key-ab)
     (holdable key-bc)
     (holdable key-cd)
     (holdable key-de)
     (unlocks key-ab door-ab)
     (unlocks key-bc door-bc)
     (unlocks key-cd door-cd)
     (unlocks key-de door-de)
     (in key-ab room-a)
     (in key-bc room-b)
     (in key-cd room-c)
     (in key-de room-d)
     (in R room-a)
     (holds R nil)
     }
  )

(defn test-ext-4a []
  (time (ops-search extended-keyword-locked-room-chain-state-a '((in R room-e)) extended-keyword-ops-a))
  )

;;;;;;;;;;;;;;;
;;; TEST 4B ;;;
;;;;;;;;;;;;;;;

(def extended-keyword-locked-room-chain-state-b
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
     (closed door-ab true)
     (closed door-bc true)
     (closed door-cd true)
     (closed door-de true)
     (locked door-ab true)
     (locked door-bc true)
     (locked door-cd true)
     (locked door-de true)
     (connects door-ab room-a)
     (connects door-ab room-b)
     (connects door-bc room-b)
     (connects door-bc room-c)
     (connects door-cd room-c)
     (connects door-cd room-d)
     (connects door-de room-d)
     (connects door-de room-e)
     (key key-ab)
     (key key-bc)
     (key key-cd)
     (key key-de)
     (holdable key-ab)
     (holdable key-bc)
     (holdable key-cd)
     (holdable key-de)
     (unlocks key-ab door-ab)
     (unlocks key-bc door-bc)
     (unlocks key-cd door-cd)
     (unlocks key-de door-de)
     (in key-ab room-a)
     (in key-bc room-b)
     (in key-cd room-c)
     (in key-de room-d)
     (in R room-a)
     (holds R nil)
     }
  )

(defn test-ext-4b []
  (time (ops-search extended-keyword-room-chain-state-b '((in R room-e)) extended-keyword-ops-b))
  )