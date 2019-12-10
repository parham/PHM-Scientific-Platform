
package com.phm.core.ds;

import com.phm.core.terminal.Source;
import com.phm.core.data.Instance;
import java.util.LinkedList;

/**
 *
 * @author phm
 * @param <DataType>
 */
public class DefaultOfflineDataStream<DataType extends Instance> 
    extends BufferedDataStream<DataType> {
    
    public DefaultOfflineDataStream (String sn, Source src) {
        super (sn, src, new LinkedList<>());
    }
}
