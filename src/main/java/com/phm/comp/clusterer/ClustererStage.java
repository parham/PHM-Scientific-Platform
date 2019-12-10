
package com.phm.comp.clusterer;
 
import java.util.Objects;
import org.apache.commons.pipeline.StageException;
import org.apache.commons.pipeline.stage.BaseStage;

/**
 *
 * @author phm
 */
public class ClustererStage extends BaseStage {
    private final Clusterer clsr;
    
    public ClustererStage (Clusterer c) {
        clsr = Objects.requireNonNull(c);
    }
    
    public Clusterer getClusterer (){
        return clsr;
    }
    
    @Override
    public void process (Object obj) throws StageException {
        String clss = clsr.cluster (obj);
        super.emit(clss);
    }
}
