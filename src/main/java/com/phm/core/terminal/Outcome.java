
package com.phm.core.terminal;

import com.phm.core.ds.ExtendableDataStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Parham Nooralishahi - PHM!
 */
public abstract class Outcome extends Terminal {
    protected final HashMap<String, ExtendableDataStream> streams = new HashMap<>();
    
    public Outcome (String sn) {
        super (sn);
    }
    
    public Map<String, ExtendableDataStream> getInputStreams () {
        return streams;
    }
    public ExtendableDataStream getInputStream (String strm) {
        return streams.get(strm);
    }
}