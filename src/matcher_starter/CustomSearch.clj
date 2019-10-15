(def state-one
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