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
           {:pre ((wearable ?obj)
                  (wears ?agent ?obj)
                  (holdable ?obj)
                  (agent ?agent)
                  )
            :add ((wears ?agent nil)
                  (holds ?agent ?obj))
            :del ((wears ?agent ?obj))
            :txt (?agent unequips the ?obj)
            }
           consecrate
           {:pre ((holy ?wearing)
                   (wears ?agent ?wearing)
                  (holds ?agent ?obj)
                  (agent ?agent)
                  (holdable ?obj)
                  )
            :add ((consecrated ?obj))
            :del (())
            :txt (?agent consecrates the ?obj)
            }
           purify
           {:pre ((holy ?wearing)
                  (wears ?agent ?wearing)
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
           {:pre ((altar ?altar)
                  (agent ?agent)
                  (holds ?agent ?obj)
                  (in ?agent ?room)
                  (in ?altar ?room)
                  (holdable ?obj)
                  (consecrated ?obj)
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
            :when ((wearable ?to-wear)
                    (holy ?to-wear)
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
            :when ((wearable ?to-wear)
                   (holy ?to-wear)
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




(def test-state-one
  '#{(agent R)

     (room A)
     (room B)

     (in R A)
     })



(def test-state-two
  '#{(agent R)

     (room A)
     (room B)
     (room C)
     (room D)
     (room E)

     (holdable halonic-garb)

     (in R A)
     (in halonic-garb B)
     })



(def test-state-three
  '#{(agent R)
     (wears R nil)

     (room A)
     (room B)
     (room C)
     (room D)
     (room E)

     (holdable gold-leaf)
     (holdable cake)
     (holdable book)
     (holdable wedding-dress)
     (holdable business-suit)
     (holdable rags)
     (holdable halonic-garb)

     (wearable wedding-dress)
     (wearable business-suit)
     (wearable rags)
     (wearable halonic-garb)

     (holy halonic-garb)

     (in R A)
     (in halonic-garb B)
     (in cake C)
     (in wedding-dress C)
     (in business-suit C)
     (in rags D)
     (in book D)
     (in gold-leaf D)
     })



(def test-state-four
  '#{(agent R)
     (wears R nil)

     (room A)
     (room B)
     (room C)
     (room D)
     (room E)

     (holdable gold-leaf)
     (holdable cake)
     (holdable book)
     (holdable wedding-dress)
     (holdable business-suit)
     (holdable rags)
     (holdable halonic-garb)

     (wearable wedding-dress)
     (wearable business-suit)
     (wearable rags)
     (wearable halonic-garb)

     (holy halonic-garb)

     (altar altar)
     (demonic altar)
     (structure altar)

     (in R A)
     (in halonic-garb B)
     (in cake C)
     (in wedding-dress C)
     (in business-suit C)
     (in rags D)
     (in book D)
     (in gold-leaf D)
     (in altar A)
     })


(def test-state-five
  '#{(agent R)
     (wears R nil)

     (demon behemoth)
     (demon ahriman)
     (demon nabriales)

     (wounded priest)
     (wounded saint)
     (wounded vicar)

     (room A)
     (room B)
     (room C)
     (room D)
     (room E)

     (holdable gold-leaf)
     (holdable cake)
     (holdable book)
     (holdable wedding-dress)
     (holdable business-suit)
     (holdable rags)
     (holdable halonic-garb)

     (wearable wedding-dress)
     (wearable business-suit)
     (wearable rags)
     (wearable halonic-garb)

     (holy halonic-garb)

     (altar altar)
     (demonic altar)
     (structure altar)

     (in R A)
     (in behemoth C)
     (in priest C)
     (in ahriman D)
     (in vicar D)
     (in nabriales E)
     (in saint E)
     (in halonic-garb B)
     (in cake C)
     (in wedding-dress C)
     (in business-suit C)
     (in rags D)
     (in book D)
     (in gold-leaf D)
     (in altar A)
     })



(def test-state-six
  '#{(agent R)
     (wears R nil)

     (wounded priest)

     (room A)
     (room B)
     (room C)

     (holdable gold-leaf)
     (holdable halonic-garb)

     (wearable halonic-garb)

     (holy halonic-garb)

     (altar altar)
     (demonic altar)
     (structure altar)

     (in R A)
     (in priest C)
     (in halonic-garb B)
     (in gold-leaf B)
     (in altar A)
     })

(defn t-os []
  (time (ops-search test-state-six '((agent priest)) opssearch-very-large-ops))
  )

(defn t-pl []
  (time (planner test-state-six '(agent priest) planner-very-large-ops))
  )




