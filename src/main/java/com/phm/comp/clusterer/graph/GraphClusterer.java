
package com.phm.comp.clusterer.graph;
 
import com.phm.comp.clusterer.OfflineClusterer;
import java.util.Map;
import org.jgraph.graph.Edge;
import org.jgrapht.Graph;

/**
 *
 * @author phm
 * @param <V>
 * @param <E>
 */
public interface GraphClusterer<V, E extends Edge>  extends OfflineClusterer<V> {
    public Map<String, Graph<V,E>> cluster (Graph<V,E> g);
}
