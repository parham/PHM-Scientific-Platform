
package com.phm.core.terminal;

import com.phm.core.ds.DataStream;
import java.util.Map;

/**
 *
 * @author phm
 */
public abstract class Source extends Terminal {
    
    public Source (String sn) {
        super (sn);
    }
    
    public abstract Map<String, DataStream> streams ();
    public abstract DataStream stream (String stname);
}

