
package com.phm.comp.neuralnet.restric;

import com.phm.comp.neuralnet.Neuron;

/**
 *
 * @author PARHAM
 */
public class ConnectionQuantityRestriction implements Restriction {
    public final int limit;
    
    public ConnectionQuantityRestriction (int l) {
        limit = l;
    }
    @Override
    public boolean fulfil(Neuron net) {
        Object tmp = net.parameters.get(Neuron.NUM_CONNECTIONS);
        return tmp != null && (Integer) tmp >= limit;
    }
}
