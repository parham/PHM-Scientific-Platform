
package com.phm.comp.clusterer.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import org.jgraph.graph.Edge;
import org.jgrapht.EdgeFactory;
import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleGraph;

/**
 *
 * @author phm
 * @param <V>
 * @param <E>
 */
public class ConnectivityBasedGraphClusterer<V, E extends Edge> 
    implements GraphClusterer<V, E> {

    protected EdgeFactory<V,E> edgeFactory;
    protected Random rand = new Random(System.currentTimeMillis());
    
    public ConnectivityBasedGraphClusterer (EdgeFactory<V,E> ef) {
        edgeFactory = ef;
    }
    
    @Override
    public Map<String, Graph<V,E>> cluster (Graph<V, E> graph) { 
        LinkedList<Graph<V,E>> clst = new LinkedList<>();
        LinkedList<V> ns = new LinkedList<>(graph.vertexSet());
        while (ns.size() > 0) {
            Stack<V> stack = new Stack<>();
            V tmp = stack.push(ns.remove (0));
            SimpleGraph<V,E> c = new SimpleGraph(edgeFactory);
            while (stack.size() > 0) {
                tmp = stack.pop();
                if (!c.containsVertex(tmp)) {
                    c.addVertex (tmp);
                    Set<E> tmpc = graph.edgesOf (tmp);
                    for (E e : tmpc) {
                        V target = e.getSource().equals(tmp) ? (V) e.getTarget() : (V) e.getSource();
                        ns.remove(target);
                        stack.push (target);
                    }
                }
            }
            // Add Graphs
            Set<V> vs = c.vertexSet();
            for (V t : vs) {
                Set<E> te = graph.edgesOf(t);
                for (E e : te) {
                    c.addEdge((V) e.getSource(), (V) e.getTarget());
                }
            }
            clst.add(c);
        }
        HashMap<String, Graph<V,E>> res = new HashMap<>();
        for (int index = 0; index < clst.size(); index++) {
            res.put(String.valueOf(index), clst.get(index));
        }
        return res;
    }

    @Override
    public Map<String, List<V>> cluster(Collection<? extends V> ds) {
        HashMap<String, List<V>> map =  new HashMap<>();
        map.put(String.valueOf(rand.nextInt()), new LinkedList<>(ds));
        return map;
    }

    @Override
    public String cluster (V f) {
        return String.valueOf (rand.nextInt());
    }

    @Override
    public Map<String, List<V>> getClusters() {
        return new HashMap<>();
    }

}
 