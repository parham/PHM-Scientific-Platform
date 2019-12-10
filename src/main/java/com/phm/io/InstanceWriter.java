
package com.phm.io;

import com.phm.core.data.Instance;

/**
 *
 * @author phm
 * @param <DataType>
 */
public interface InstanceWriter<DataType extends Instance> {
    public boolean write (DataType data);
}
