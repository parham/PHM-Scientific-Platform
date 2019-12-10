
package com.phm.comp.clusterer;
 
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author phm
 * @param <E>
 */
public interface OfflineClusterer<E> extends Clusterer<E> {
    public Map<String, List<E>> cluster (Collection<? extends E> ds);
}
