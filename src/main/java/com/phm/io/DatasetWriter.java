
package com.phm.io;

import com.phm.core.data.Instance;
import java.util.Collection;

/**
 *
 * @author PHM
 * @param <DataType>
 */
public interface DatasetWriter<DataType extends Instance> extends InstanceWriter<DataType> {
    public boolean write (Collection<? extends DataType> ds);
}
