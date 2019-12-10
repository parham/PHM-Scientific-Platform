
package com.phm.core.distrb;

import com.phm.core.data.Instance;
import com.phm.core.ds.DataStream;
import java.util.Objects;
import org.apache.commons.pipeline.Pipeline;

/**
 *
 * @author phm
 */
public abstract class Distributor {
    protected DataStream inputObj;
    protected Pipeline pipeObj;
    
    public Distributor (DataStream ds, Pipeline pip) {
        inputObj = Objects.requireNonNull(ds);
        pipeObj = Objects.requireNonNull(pip);
    }
    
    public DataStream getInputStream() {
        return inputObj;
    }
    public Pipeline getPipeline () {
        return pipeObj;
    }
    public void emit () {
        Instance inst = inputObj.next();
        pipeObj.getSourceFeeder().feed(inst);
    }
    public boolean hasMore () {
        return inputObj.hasMore();
    }
    
    public abstract void distribute ();
}
