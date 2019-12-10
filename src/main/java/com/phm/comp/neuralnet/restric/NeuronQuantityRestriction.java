
package com.phm.comp.neuralnet.restric;

import com.phm.comp.neuralnet.Neuron;
import com.phm.comp.neuralnet.NeuronGroup;

/**
 *
 * @author PARHAM
 */
public class NeuronQuantityRestriction implements Restriction {
    public final int limit;
    
    public NeuronQuantityRestriction (int l) {
        limit = l;
    }
    @Override
    public boolean fulfil(Neuron net) {
        Object tmp = net.parameters.get(NeuronGroup.NUM_NEURONS);
        return tmp != null && (Integer) tmp  >= limit;
    }
}
