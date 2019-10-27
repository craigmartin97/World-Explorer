(ns tuple-order-planner.state)

;--------------------------------------------------
;--------------------------------------------------
;---------------------State-----------------------------
;--------------------------------------------------
;--------------------------------------------------

(def state-many-objs
  '#{
     ;define agent
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

     (in R A)
     (holds R)

     (holdable key)
     (in key D)

     (holdable dog)
     (in dog A)

     (holdable cat)
     (in cat A)

     (holdable mouse)
     (in mouse A)

     (holdable phone)
     (in phone B)

     (holdable cup)
     (in cup F)

     (holdable glass)
     (in glass K)

     (holdable remote)
     (in remote I)

     (holdable water)
     (in water I)

     (holdable watch)
     (in watch G)

     }
  )