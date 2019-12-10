
package com.phm.comp.neuralnet;

import com.phm.core.ParametersContainer;
import com.phm.core.data.Features;
import java.util.List;

/**
 *
 * @author phm
 */
public interface NeuronAnalyzer {
    public void analysis (String state, Neuron n, ParametersContainer param, Features current, List<NNResult> result);
}
