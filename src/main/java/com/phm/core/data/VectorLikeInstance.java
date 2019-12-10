
package com.phm.core.data;

import java.util.List;

/**
 *
 * @author phm
 * @param <E>
 */
public interface VectorLikeInstance<E> extends Instance {
    public E item (int index);
    public List<E> items ();
}
