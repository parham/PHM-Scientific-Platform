
package com.phm.core.distrb;

import com.phm.core.ds.DataStream;
import org.apache.commons.pipeline.Pipeline;

/**
 *
 * @author phm
 */
public class DefaultDistributor extends Distributor {

    public DefaultDistributor(DataStream ds, Pipeline pip) {
        super(ds, pip);
    }

    @Override
    public void distribute () {
        while (hasMore()) {
            emit();
        }
    }
    
}
