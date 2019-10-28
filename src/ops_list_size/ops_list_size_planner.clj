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
             (holds ?agent ?obj)
             )
      :add ((holds ?agent ?obj))
      :del ((in ?obj ?room1))
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
                   (holds ?agent ?obj)
                   )
            :add ((in ?obj ?room1))
            :del ((holds ?agent ?obj))
            :txt (?agent dropped ?obj in ?room1)
            }
           equip
           {:pre ((holdable ?obj)
                  (wearable ?obj)
                  (wears ?agent nil)
                  (holds ?agent ?obj)
                  )
            :add ((wears ?agent ?obj))
            :del ((wears ?agent nil)
                  (holds ?agent ?obj))
            :txt (?agent equips the ?obj)
            }
           })
  )

(def opssearch-medium-ops
  (merge opssearch-short-ops
         '{unequip
           {:pre ((wears ?agent ?obj)
                  (wearable ?obj)
                  (holdable ?obj)
                  (agent ?agent)
                  )
            :add ((wears ?agent nil)
                  (holds ?agent ?obj))
            :del ((wears ?agent ?obj))
            :txt (?agent unequips the ?obj)
            }
           consecrate
           {:pre ((wears ?agent ?wearing)
                  (holy ?wearing)
                  (holds ?agent ?obj)
                  (agent ?agent)
                  (holdable ?obj)
                  )
            :add ((consecrated ?obj))
            :del (())
            :txt (?agent consecrates the ?obj)
            }
           purify
           {:pre ((wears ?agent ?wearing)
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
                  (holds ?agent gold-leaf)
                  (pure ?struct)
                  (structure ?struct)
                  )
            :add ((gilded ?struct))
            :del ()
            :txt (?agent has gilded the pure ?struct)
            }
           tribute
           {:pre ((in ?agent ?room)
                  (in ?altar ?room)
                  (holds ?agent obj)
                  (agent ?agent)
                  (holdable ?obj)
                  (consecrated ?obj)
                  (altar ?altar)
                  (pure ?altar)
                  (gilded ?altar)
                  (structure ?altar)
                  )
            :add ((holds ?altar ?obj))
            :del ((holds ?agent ?obj))
            :txt (?agent has placed the ?obj on the ?altar in tribute)
            }
           }))

(def opssearch-very-large-ops
  (merge opssearch-large-ops
         '{deliverance
           {:pre ((in ?agent ?room)
                  (in ?altar ?room)
                  (holds ?altar ?obj)
                  (agent ?agent)
                  (altar ?altar)
                  (pure ?altar)
                  (gilded ?altar)
                  (structure ?altar)
                  )
            :add ((blessed ?agent))
            :del ((holds ?altar ?obj))
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
     (in R A)
     (holds R goblet)
     (holds R diamond)
     (holds R gold-leaf)
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

     (wearable halonic-garb)

     (holy halonic-garb)

     (structure altar)
     (altar altar)
     (pure altar)

     (in goblet A)
     (in diamond A)
     (in gold-leaf A)
     (in halonic-garb A)
     (in altar B)
     (in ahriman C)
     (in saint C)
     })

(defn os-test []
  (time (ops-search os-state '((agent saint)) opssearch-very-large-ops))
  )

















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
      :achieves (holds ?agent ?obj)
      :when ((agent ?agent)
             (room ?room1)
             (holdable ?obj)
             (in ?obj ?room1)
             )
      :post ((in ?agent ?room1))
      :pre ()
      :add ((holds ?agent ?obj))
      :del ((in ?obj ?room1))
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
            :achieves (wears ?agent ?to-wear)
            :when ((wears ?agent nil)
                   (holdable ?to-wear)
                   )
            :post ((holds ?agent ?to-wear))
            :add ((wears ?agent ?to-wear))
            :del ((wears ?agent nil)
                  (holds ?agent ?to-wear))
            :txt (?agent equips the ?to-wear)
            }
           })
  )

(def planner-medium-ops
  (merge planner-short-ops
         '{unequip
           {:name unequip
            :achieves (holds ?agent ?obj)
            :when ((wears ?agent ?obj)
                   (agent ?agent)
                   )
            :post ()
            :pre ()
            :add ((wears ?agent nil)
                  (holds ?agent ?obj))
            :del ((wears ?agent ?obj))
            :txt (?agent unequips the ?to-hold)
            }
           consecrate
           {:name consecrate
            :achieves (consecrated ?obj)
            :when ((holy ?to-wear)
                   (agent ?agent)
                   (holdable ?obj)
                   (:guard (not= (? to-wear) (? obj)))
                   )
            :post ((wears ?agent ?to-wear)
                   (holds ?agent ?obj))
            :pre ()
            :add ((consecrated ?obj))
            :del (())
            :txt (?agent consecrates the ?obj)
            }
           purify
           {:name purify
            :achieves (pure ?struct)
            :when ((holy ?to-wear)
                   (agent ?agent)
                   (in ?struct ?room)
                   (structure ?struct))
            :post ((wears ?agent ?to-wear)
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
                       (structure ?struct)
                       (agent ?agent)
                       )
            :post     ((holds ?agent gold-leaf)
                       (pure ?struct)
                       (in ?agent ?room))
            :pre      ()
            :add      ((gilded ?struct))
            :del      ()
            :txt      (?agent has gilded the pure ?struct)
            }
           tribute
           {:name     tribute
            :achieves (holds ?altar ?obj)
            :when     ((agent ?agent)
                       (wears ?agent ?wearing)
                       (altar ?altar)
                       (in ?altar ?room)
                       (holdable ?obj)
                       (:guard (not= (? wearing) (? obj)))
                       (structure ?altar)
                       )
            :post     ((holds ?agent ?obj)
                       (consecrated ?obj)
                       (gilded ?altar)
                       (in ?agent ?room))
            :pre      ()
            :add      ((holds ?altar ?obj))
            :del      ((holds ?agent ?obj))
            :txt      (?agent has placed the ?obj on the ?altar in tribute)
            }
           }
         ))

(def planner-very-large-ops
  (merge planner-large-ops
         '{deliverance
           {:name     deliverance
            :achieves (blessed ?agent)
            :when     ((wears ?agent ?wearing)
                       (holdable ?obj)
                       (in ?altar ?room)
                       (agent ?agent)
                       (:guard (not= (? wearing) (? obj)))
                       (altar ?altar)
                       (structure ?altar)
                       )
            :post     ((gilded ?altar)
                       (holds ?altar ?obj)
                       (in ?agent ?room))
            :pre      ()
            :add      ((blessed ?agent))
            :del      ((holds ?altar ?obj))
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
     (wears R nil)

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