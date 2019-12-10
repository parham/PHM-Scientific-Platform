
package com.phm.io;

import com.phm.core.data.Instance;

/**
 *
 * @author phm
 * @param <DataType>
 */
public interface InstanceReader<DataType extends Instance> {
    public DataType read ();
}
