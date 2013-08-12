(ns loom.test.titanium
  (:use [loom.graph])
  (:use [loom alg titanium]
        [clojure.test])
  (:require [clojurewerkz.titanium.edges :as edges])
  (:require [clojurewerkz.titanium.vertices :as nodes])
  (:require [clojurewerkz.titanium.graph :as tg]))

(deftest dag-ops
  (let [in-mem-graph (tg/open {"storage.backend" "inmemory"})]
    (tg/transact!
      (let [ 
            a (nodes/create! {:name "Node A"})
            b (nodes/create! {:name "Node B"})
            c (nodes/create! {:name "Node C"})
            d (nodes/create! {:name "Node D"})
            e (nodes/create! {:name "Node E"})
            f (nodes/create! {:name "Node F"})
            g (nodes/create! {:name "Node G"})
            e1  (edges/connect! a "edge A->B" b)
            e2  (edges/connect! a "edge A->C" c)
            e3  (edges/connect! b "edge B->C" c)
            e4  (edges/connect! b "edge B->D" d)
            e5  (edges/connect! c "edge C->E" e)
            e6  (edges/connect! c "edge C->F" f)
            e7  (edges/connect! e "edge E->D" d)
            e8  (edges/connect! f "edge F->E" e)
            e9  (edges/connect! g "edge G->A" a)
            e10 (edges/connect! g "edge G->F" f)
            graph (titanium->loom in-mem-graph)]
        (is (= [d e f c b a g] (post-traverse graph g)))
        (is (dag? graph))
        (is (= [g a b c f e d] (topsort graph)))
        (is (= #{a b c d e f g} (nodes graph)))
        (is (= (set (map (juxt edges/tail-vertex edges/head-vertex) #{e1 e2 e3 e4 e5 e6 e7 e8 e9 e10})) (edges graph)))
        (is (has-node? graph a))
        #_(is (= false (has-node? graph z)))
        (is (has-edge? graph a b))
        (is (= false (has-edge? graph a g)))
        #_(is (= false (has-edge? graph a z)))
        #_(is (= false (has-edge? graph z y)))
        #_(view graph)))))
