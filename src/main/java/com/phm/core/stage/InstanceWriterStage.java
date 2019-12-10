
package com.phm.core.stage;

import com.phm.core.data.Instance;
import org.apache.commons.pipeline.stage.BaseStage;
import com.phm.io.InstanceWriter;
import java.util.Objects;
import org.apache.commons.pipeline.StageException;

/**
 *
 * @author phm
 */
public class InstanceWriterStage extends BaseStage {
    
    private final InstanceWriter writer;
    
    public InstanceWriterStage (InstanceWriter instw) {
        writer = Objects.requireNonNull(instw);
    }
    
    public InstanceWriter getWriter () {
        return writer;
    }

    @Override
    public void process(Object obj) throws StageException {
        writer.write((Instance) obj);
        this.emit(obj);
    }
}
