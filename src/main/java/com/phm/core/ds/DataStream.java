
package com.phm.core.ds;

import com.phm.annotations.ImplementationDetails;
import com.phm.core.terminal.Source;
import com.phm.core.data.Instance; 
import java.util.Objects;

@ImplementationDetails (
    className = "DataStream",
    date = "10/20/2015",
    lastModification = "10/20/2015",
    version = "1.0.0",
    description = "data stream ..."
)
public abstract class DataStream<DataType extends Instance> {

    public final String name;
    private final Source sourceObj;
    
    public DataStream (String strm, Source src) {
        name = Objects.requireNonNull(strm);
        sourceObj = src;
    }
    
    public String getName () {
        return name;
    }
    public Source getSource () {
        return sourceObj;
    }
    
    public abstract DataType next ();
    public abstract boolean hasMore ();
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.name);
        return hash;
    }
    @Override
    public boolean equals (Object obj) {
        return obj != null &&
               obj instanceof DataStream &&
               obj.hashCode() == hashCode();
    }
}
