
package com.phm.comp.neuralnet;

import com.phm.core.data.Features;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.pipeline.StageException;
import org.apache.commons.pipeline.stage.BaseStage;

/**
 *
 * @author phm
 */
public class NeuralNetworkStage extends BaseStage {
    private final NeuronGroup neurong;
    private final Supervisor supervisor;
    
    public NeuralNetworkStage (NeuronGroup ng, Supervisor sp) {
        neurong = Objects.requireNonNull(ng);
        supervisor = Objects.requireNonNull(sp);
    }
    public NeuronGroup getNetwork () {
        return neurong;
    }
    public Supervisor getSupervisor () {
        return supervisor;
    }
    
    @Override
    public void process(Object obj) throws StageException {
        List<NNResult> res = new LinkedList<>();
        if (neurong.feed((Features) obj, res)) {
            super.emit(res);
        }
    }
}
