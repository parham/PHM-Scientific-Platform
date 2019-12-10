
package com.phm.core.ds;

import com.phm.core.data.Instance;
import com.phm.core.terminal.Source;
import java.util.Collection;

/**
 *
 * @author phm
 * @param <DataType>
 */
public abstract class ExtendableDataStream<DataType extends Instance> extends DataStream<DataType> {

    public ExtendableDataStream(String strm, Source src) {
        super(strm, src);
    }
    
    public abstract boolean add (DataType d);
    public boolean addAll(Collection<? extends DataType> ds) {
        ds.stream().forEach((x) -> {
            add(x);
        });
        return true;
    }
}
