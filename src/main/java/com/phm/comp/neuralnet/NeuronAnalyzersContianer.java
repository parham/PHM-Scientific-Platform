
package com.phm.comp.neuralnet;

import com.phm.core.ParametersContainer;
import com.phm.core.data.Features;
import java.util.LinkedList; 
import java.util.List;

/**
 *
 * @author phm
 */
public class NeuronAnalyzersContianer extends LinkedList<NeuronAnalyzer> {    
    public void analysis (String state, Neuron n, ParametersContainer param, Features current, List<NNResult> result) {
        this.stream().forEach((NeuronAnalyzer x) -> x.analysis(state, n, param, current, result));
    }
}
