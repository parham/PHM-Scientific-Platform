
package com.phm.core.stage;

import com.phm.core.data.Instance;
import com.phm.core.data.converter.InstanceConverter;
import java.util.Objects;
import org.apache.commons.pipeline.StageException;
import org.apache.commons.pipeline.stage.BaseStage;

/**
 *
 * @author phm
 */
public class InstanceConverterStage extends BaseStage {
    protected InstanceConverter convObj;
    
    public InstanceConverterStage (InstanceConverter cnv) {
        convObj = Objects.requireNonNull(cnv);
    }
    
    public InstanceConverter getConverter () {
        return convObj;
    }
    
    @Override
    public void process (Object obj) throws StageException {
        Object res = convObj.apply((Instance) obj);
        if (res != null) {
            this.emit(res);
        }
    }
}
