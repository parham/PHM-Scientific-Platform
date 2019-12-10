
package com.phm.comp.classifier;

import com.phm.core.data.Instance;
import java.util.Objects;
import org.apache.commons.pipeline.StageException;
import org.apache.commons.pipeline.stage.BaseStage;

/**
 *
 * @author phm
 */
public class AbsoluteClassifierStage extends BaseStage {
    
    private final Classifier clfy;
    
    public AbsoluteClassifierStage (Classifier clf) {
        clfy = Objects.requireNonNull(clf);
    }
    
    public Classifier getClassifier () {
        return clfy;
    }
    
    @Override
    public void process (Object obj) throws StageException {
        Object clss = clfy.classify((Instance) obj);
        super.emit(clss);
    }
}
