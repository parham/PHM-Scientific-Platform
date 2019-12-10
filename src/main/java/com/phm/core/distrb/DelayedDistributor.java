
package com.phm.core.distrb;

import com.phm.core.ds.DataStream;
import java.util.Timer;
import org.apache.commons.pipeline.Pipeline;

/**
 *
 * @author phm
 */
public class DelayedDistributor extends Distributor {
    
    protected long delayObj = 10;
    protected Timer timerObj;
    
    public DelayedDistributor(DataStream ds, Pipeline pip, long delay) {
        super(ds, pip);
        delayObj = delay;
    }
    
    public long getTime () {
        return delayObj;
    }

    @Override
    public void distribute() {
        long previous = System.currentTimeMillis();
        while (hasMore()) {
            long current = System.currentTimeMillis();
            if (current - previous >= delayObj) {
                emit();
                previous = current;
            }
        }
    }
}
