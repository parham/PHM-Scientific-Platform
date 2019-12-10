
package com.phm.comp.clusterer.graph;
 
import com.phm.comp.triangulator.Triangulator;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.jgraph.graph.Edge; 
import org.jgrapht.Graph;

/**
 *
 * @author phm
 * @param <V>
 * @param <E>
 */
public class GraphBasedClusterer<V, E extends Edge>
    implements GraphClusterer<V, E> {
    
    protected Triangulator<V,E> triangMethod;
    protected GraphClusterer<V,E> clusterer;
    protected HashMap<String, Graph<V,E>> mapCluster = new HashMap<>();
    
    public GraphBasedClusterer (Triangulator<V,E> tm,
                                GraphClusterer<V,E> ef) {
        triangMethod = Objects.requireNonNull (tm);
        clusterer = Objects.requireNonNull(ef);
    }
    
    public Triangulator<V,E> getTriangulator () {
        return triangMethod;
    }
    public GraphClusterer<V,E> getGraphClusterer () {
        return clusterer;
    }

    @Override
    public Map<String, Graph<V, E>> cluster (Graph<V, E> g) {
        return clusterer.cluster(g);
    }

    @Override
    public Map<String, List<V>> cluster (Collection<? extends V> ds) {
        Graph<V,E> graph = triangMethod.triangulate (ds);
        HashMap<String, Graph<V, E>> map = new HashMap<>(cluster(graph));
        HashMap<String, List<V>> res = new HashMap<>();
        for (String key : map.keySet()) {
            res.put(key, new LinkedList<>(map.get(key).vertexSet()));
        }
        return res;
    }

    @Override
    public String cluster (V f) {
        return String.valueOf(0);
    }

    @Override
    public Map<String, List<V>> getClusters() {
        return new HashMap<>();
    }
}