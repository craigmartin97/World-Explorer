(ns ops-list-size.ops-list-size-planner
  (:require [org.clojars.cognesence.breadth-search.core :refer :all]
            [org.clojars.cognesence.matcher.core :refer :all]
            [org.clojars.cognesence.ops-search.core :refer :all]
            [planner.planner :refer :all]))

(def opssearch-very-short-ops
  '{move
    {:pre ((agent ?agent)
            (room ?room1)
            (room ?room2)
            (in ?agent ?room1)
            )
     :add ((in ?agent ?room2))
     :del ((in ?agent ?room1))
     :txt (agent ?agent has moved from ?room1 to ?room2)
     }
     pickup
     {:pre ((agent ?agent)
             (room ?room1)
             (holdable ?obj)
             (in ?obj ?room1)
             (in ?agent ?room1)
             (holds ?agent ??x)
             )
      :add ((holds ?agent ??x ?obj))
      :del ((holds ?agent ??x)
            (in ?obj ?room1))
      :txt (?agent picked up ?obj from ?room1)
      }
     }
  )

(def opssearch-short-ops
  (merge opssearch-very-short-ops
         '{drop-obj
           {:pre ((holdable ?obj)
                   (room ?room1)
                   (agent ?agent)
                   (in ?agent ?room1)
                   (holds ?agent ??objs1 ?obj ??objs2)
                   )
            :add ((in ?obj ?room1)
                  (holds ?agent ??objs1 ??objs2))
            :del ((holds ?agent ??objs1 ?obj ??objs2))
            :txt (?agent dropped ?obj in ?room1)
            }
           equip
           {:pre ((holds ?agent ??objs1 ?obj ??objs2)
                  (wearable ?obj)
                  (holdable ?obj)
                  (wears ?agent ??wearables)
                  )
            :add ((wears ?agent ??wearables ?obj)
                  (holds ?agent ??objs1 ??objs2))
            :del ((wears ?agent ??wearables)
                  (holds ?agent ??objs1 ?obj ??objs2))
            :txt (?agent equips the ?obj)
            }
           })
  )

(def opssearch-medium-ops
  (merge opssearch-short-ops
         '{unequip
           {:pre ((wears ?agent ??wears1 ?obj ??wears2)
                  (wearable ?obj)
                  (holdable ?obj)
                  (holds ?agent ??objs)
                  (?agent agent)
                  )
            :add ((wears ?agent ??wears1 ??wears2)
                  (holds ?agent ??objs ?obj))
            :del ((wears ?agent ??wears1 ?obj ??wears2)
                  (holds ?agent ??objs))
            :txt (?agent unequips the ?obj)
            }
           consecrate
           {:pre ((wears ?agent ??wears1 ?wearing ??wears2)
                  (holy ?wearing)
                  (holds ?agent ??objs1 ?obj ??objs2)
                  (agent ?agent)
                  (holdable ?obj)
                  )
            :add ((consecrated ?obj))
            :del (())
            :txt (?agent consecrates the ?obj)
            }
           purify
           {:pre ((wears ?agent ??wears1 ?wearing ??wears2)
                  (holy ?wearing)
                  (in ?agent ?room)
                  (in ?struct ?room)
                  (agent ?agent)
                  (structure ?struct)
                  )
            :add ((pure ?struct))
            :del ((demonic ?struct))
            :txt (?agent has purified the ?struct)
            }
           })
  )

(def opssearch-large-ops
  (merge opssearch-medium-ops
         '{gild
           {:pre ((in ?agent ?room)
                  (in ?struct ?room)
                  (holds ?agent ??objs1 gold-leaf ??objs2)
                  (pure ?struct)
                  (structure ?struct)
                  )
            :add ((gilded ?struct)
                  (holds ?agent ??objs1 ??objs2))
            :del ((holds ?agent ??objs1 gold-leaf ??objs2))
            :txt (?agent has gilded the pure ?struct)
            }
           tribute
           {:pre ((in ?agent ?room)
                  (in ?altar ?room)
                  (holds ?agent ??ag-holds1 ?obj ??ag-holds2)
                  (holds ?altar ??alt-holds)
                  (agent ?agent)
                  (holdable ?obj)
                  (consecrated ?obj)
                  (altar ?altar)
                  (pure ?altar)
                  (gilded ?altar)
                  (structure ?altar)
                  )
            :add ((holds ?altar ??alt-holds ?obj)
                  (holds ?agent ??ag-holds1 ??ag-holds2))
            :del ((holds ?altar ??alt-holds)
                  (holds ?agent ??ag-holds1 ?obj ??ag-holds2))
            :txt (?agent has placed the ?obj on the ?altar in tribute)
            }
           }))

(def opssearch-very-large-ops
  (merge opssearch-large-ops
         '{deliverance
           {:pre ((in ?agent ?room)
                  (in ?altar ?room)
                  (holds ?altar ??objs1 ?obj ??objs2)
                  (agent ?agent)
                  (altar ?altar)
                  (pure ?altar)
                  (gilded ?altar)
                  (structure ?altar)
                  )
            :add ((blessed ?agent)
                  (holds ?altar ??objs1 ??objs2))
            :del ((holds ?altar ??objs1 ?obj ??objs2))
            :txt (?agent has delivered the ?obj to a higher plane of existance)
            }
           transcend
           {:pre ((in ?agent ?room)
                  (in ?altar ?room)
                  (agent ?agent)
                  (blessed ?agent)
                  (altar ?altar)
                  (pure ?altar)
                  (structure ?altar)
                  )
            :add ((transcended ?agent))
            :del (())
            :txt (The blessed ?agent prays in front of the ?altar and achieves transcendance)
            }
           smite
           {:pre ((in ?agent ?room)
                  (in ?demon ?room)
                  (agent ?agent)
                  (transcended ?agent)
                  (demon ?demon)
                  )
            :add ((smited ?demon))
            :del ((demon ?demon))
            :txt (The transcendant ?agent smites the ?demon into ashes)
            }
           benediction
           {:pre ((in ?agent1 ?room)
                  (in ?agent2 ?room)
                  (agent ?agent1)
                  (transcended ?agent1)
                  (wounded ?agent2)
                  )
            :add ((agent ?agent2))
            :del ((wounded ?agent2))
            :txt (The transcednant ?agent1 focuses their holy spirit into a benediction which fully revitalise ?agent2)
            }
           }))

(def os-state
  '#{(agent R)
     (in R B)
     (holds R goblet diamond gold-leaf)
     (wears R halonic-garb)

     (room A)
     (room B)
     (room C)

     (demon ahriman)
     (wounded saint)

     (holdable goblet)
     (holdable diamond)
     (holdable gold-leaf)
     (holdable halonic-garb)
     (holdable halonic-biretta)

     (wearable halonic-garb)
     (wearable halonic-biretta)

     (holy halonic-garb)
     (holy halonic-biretta)

     (structure altar)
     (altar altar)
     (pure altar)
     (holds altar)

     (consecrated goblet)

     (in goblet A)
     (in diamond A)
     (in gold-leaf A)
     (in halonic-garb A)
     (in halonic-biretta A)
     (in altar B)
     (in ahriman C)
     (in saint C)
     })



















(def planner-very-short-ops
  '{move
    {
     :name move-agent
     :achieves (in ?agent ?room2)
     :when ((agent ?agent)
            (room ?room1)
            (room ?room2)
            (in ?agent ?room1)
            )
     :post ()
     :pre ()
     :add ((in ?agent ?room2))
     :del ((in ?agent ?room1))
     :txt (agent ?agent has moved from ?room1 to ?room2)
     }
     pickup
     {:name pickup-obj
      :achieves (holds ?agent ??x ?obj)
      :when ((agent ?agent)
             (room ?room1)
             (holdable ?obj)
             (in ?obj ?room1)
             )
      :post ((holds ?agent ??x)
             (in ?agent ?room1))
      :pre ()
      :add ((holds ?agent ??x ?obj))
      :del ((holds ?agent ??x)
            (in ?obj ?room1))
      :txt (?agent picked up ?obj from ?room1)
      }
    }
  )

(def planner-short-ops
  (merge planner-very-short-ops
         '{drop-obj
           {:name drop-obj
            :achieves (in ?obj ?room1)
            :when ((holdable ?obj)
                   (room ?room1)
                   (agent ?agent)
                   )
            :post ((holds ?agent ?obj)
                   (in ?agent ?room1))
            :pre ()
            :add ((in ?obj ?room1))
            :del ((holds ?agent ?obj))
            :txt (?agent dropped ?obj in ?room1)
            }
           equip
           {:name equip
            :achieves (wears ?agent ??to-wear)
            :when ((wears ?agent ??wearing)
                   (holds ?agent ??holding)
                   )
            :post ((holds ?agent ??holding ??to-wear))
            :add ((wears ?agent ??wearing ??to-wear)
                  (holds ?agent ??holding))
            :del ((wears ?agent ??wearing)
                  (holds ?agent ??holding ??to-wear))
            :txt (?agent equips the ?to-wear)
            }
           })
  )

(def planner-medium-ops
  (merge planner-short-ops
         '{
           consecrate
           {:name consecrate
            :achieves (consecrated ?obj)
            :when ((wears ?agent ??wearing)
                   (holy ?to-wear)
                   (agent ?agent)
                   (holdable ?obj)
                   (holds R ??objs))
            :post ((wears ?agent ??wearing ?to-wear)
                   (holds ?agent ??objs ?obj))
            :pre ()
            :add ((consecrated ?obj))
            :del (())
            :txt (?agent consecrates the ?obj)
            }
           purify
           {:name purify
            :achieves (pure ?struct)
            :when ((wears ?agent ??wearing)
                   (holy ?to-wear)
                   (agent ?agent)
                   (in ?struct ?room)
                   (structure ?struct))
            :post ((wears ?agent ??wearing ?to-wear)
                   (in ?agent ?room))
            :pre ()
            :add ((pure ?struct))
            :del ((demonic ?struct))
            :txt (?agent has purified the ?struct)
            }
           })
  )

(def planner-large-ops
  (merge planner-medium-ops
         '{gild
           {:name     gild
            :achieves (gilded ?struct)
            :when     ((in ?struct ?room)
                       (holds ?agent ??objs)
                       (structure ?struct))
            :post     ((holds ?agent ??objs gold-leaf)
                       (in ?agent ?room)
                       (pure ?struct))
            :pre      ()
            :add      ((gilded ?struct)
                       (holds ?agent ??objs))
            :del      ((holds ?agent ??objs gold-leaf))
            :txt      (?agent has gilded the pure ?struct)
            }
           tribute
           {:name     tribute
            :achieves (holds ?altar ??alt-holds ?obj)
            :when     ((agent ?agent)
                       (altar ?altar)
                       (in ?altar ?room)
                       (holds ?altar ??alt-holds)
                       (holds ?agent ??ag-holds)
                       (holdable ?obj)
                       (structure ?altar))
            :post     ((holds ?agent ??ag-holds ?obj)
                       (consecrated ?obj)
                       (gilded ?altar)
                       (in ?agent ?room))
            :pre      ()
            :add      ((holds ?altar ??alt-holds ?obj)
                       (holds ?agent ??ag-holds))
            :del      ((holds ?altar ??alt-holds)
                       (holds ?agent ??ag-holds ?obj))
            :txt      (?agent has placed the ?obj on the ?altar in tribute)
            }
           }
         ))

(def planner-very-large-ops
  (merge planner-large-ops
         '{deliverance
           {:name     deliverance
            :achieves (blessed ?agent)
            :when     ((holds ?altar ??objs)
                       (holdable ?obj)
                       (in ?altar ?room)
                       (agent ?agent)
                       (altar ?altar)
                       (structure ?altar)
                       )
            :post     ((gilded ?altar)
                       (holds ?altar ??objs ?obj)
                       (in ?agent ?room))
            :pre      ()
            :add      ((blessed ?agent)
                       (holds ?altar ??objs1 ??objs2))
            :del      ((holds ?altar ??objs1 ?obj ??objs2))
            :txt      (?agent has delivered the ?obj to a higher plane of existance)
            }
           transcend
           {:name     transcend
            :achieves (transcended ?agent)
            :when     ((in ?altar ?room)
                       (agent ?agent)
                       (altar ?altar)
                       (structure ?altar))
            :post     ((gilded ?altar)
                       (blessed ?agent)
                       (in ?agent ?room))
            :add ((transcended ?agent))
            :del ()
            :txt (The blessed ?agent prays in front of the ?altar and achieves transcendance)
            }
           smite
           {:name     smite
            :achieves (smited ?demon)
            :when     ((in ?demon ?room)
                       (agent ?agent)
                       (demon ?demon))
            :post     ((transcended ?agent)
                       (in ?agent ?room))
            :add ((smited ?demon))
            :del ((demon ?demon))
            :txt (The transcendant ?agent smites the ?demon into ashes)
            }
           benediction
           {:name     benediction
            :achieves (agent ?agent2)
            :when     ((in ?agent2 ?room)
                       (agent ?agent1)
                       (wounded ?agent2))
            :post     ((transcended ?agent1)
                       (in ?agent1 ?room))
            :pre ()
            :add ((agent ?agent2))
            :del ((wounded ?agent2))
            :txt (The transcednant ?agent1 focuses their holy spirit into a benediction which fully revitalise ?agent2)
            }
           }))

(def pl-state
  '#{(agent R)
     (in R A)
     (holds R)
     (wears R)

     (room A)
     (room B)
     (room C)

     (demon ahriman)
     (wounded saint)

     (holdable goblet)
     (holdable diamond)
     (holdable gold-leaf)
     (holdable halonic-garb)
     (holdable halonic-biretta)

     (wearable halonic-garb)
     (wearable halonic-biretta)

     (holy halonic-garb)
     (holy halonic-biretta)

     (structure altar)
     (altar altar)
     (demonic altar)
     (holds altar)

     (in goblet A)
     (in diamond A)
     (in halonic-garb A)
     (in halonic-biretta A)
     (in gold-leaf B)
     (in altar B)
     (in ahriman C)
     (in saint C)
     })

(defn pl-test []
  (time (planner pl-state '(agent saint) planner-very-large-ops))
  )


(def test-state-one
  '#{(agent R)
     (in R A)

     (room A)
     (room B)
     })