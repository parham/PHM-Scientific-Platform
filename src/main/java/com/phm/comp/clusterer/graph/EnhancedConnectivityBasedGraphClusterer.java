
package com.phm.comp.clusterer.graph;
  
import java.util.Map;
import org.jgraph.graph.Edge;
import org.jgrapht.EdgeFactory;
import org.jgrapht.Graph;

/**
 *
 * @author phm
 * @param <V>
 * @param <E>
 */
public abstract class EnhancedConnectivityBasedGraphClusterer<V, E extends Edge>  
    extends ConnectivityBasedGraphClusterer<V, E> {

    public EnhancedConnectivityBasedGraphClusterer(EdgeFactory<V, E> ef) {
        super (ef);
    }
    
    @Override
    public Map<String, Graph<V,E>> cluster (Graph<V, E> graph) {
        if (enhance (graph)) {
            Map<String, Graph<V,E>> gs = super.cluster(graph);
            normalize(gs);
            return gs;
        }
        return null;
    }
    
    public abstract boolean enhance (Graph<V, E> g);
    public abstract boolean normalize (Map<String, Graph<V,E>> gs);
}
