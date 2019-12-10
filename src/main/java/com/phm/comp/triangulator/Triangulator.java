
package com.phm.comp.triangulator;
 
import java.util.Collection;
import org.jgraph.graph.Edge;
import org.jgrapht.Graph;

/**
 *
 * @author phm
 * @param <V>
 * @param <E>
 */
public interface Triangulator<V, E extends Edge> {
    public Graph<V,E> triangulate (Collection<? extends V> ds);
}
