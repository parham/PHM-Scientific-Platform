
package com.phm.io;

import com.phm.core.data.Instance;
import java.util.Collection;

/**
 *
 * @author PHM
 * @param <DataType>
 */
public interface DatasetReader<DataType extends Instance> extends InstanceReader<DataType> {
    public boolean read (Collection<DataType> ds);
}
