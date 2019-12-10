
package com.phm.core.stage;

import com.phm.core.data.Instance;
import com.phm.core.data.filter.InstanceFilter;
import java.util.Objects;
import org.apache.commons.pipeline.StageException;
import org.apache.commons.pipeline.stage.BaseStage;

/**
 *
 * @author phm
 */
public class InstanceFilterStage extends BaseStage {
    protected InstanceFilter filterObj;
    
    public InstanceFilterStage (InstanceFilter cnv) {
        filterObj = Objects.requireNonNull(cnv);
    }
    
    public InstanceFilter getConverter () {
        return filterObj;
    }
    
    @Override
    public void process (Object obj) throws StageException {
        Object res = filterObj.apply((Instance) obj);
        if (res != null) {
            this.emit(res);
        }
    }
}
