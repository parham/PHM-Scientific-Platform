
package com.phm.comp.clusterer;

import java.util.List;
import java.util.Map;

/**
 *
 * @author phm
 * @param <E>
 */
public interface Clusterer<E> {
    public String cluster (E f);
    public Map<String, List<E>> getClusters ();
}
