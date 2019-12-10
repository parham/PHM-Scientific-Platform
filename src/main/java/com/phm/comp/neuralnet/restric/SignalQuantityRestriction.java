
package com.phm.comp.neuralnet.restric;

import com.phm.comp.neuralnet.Neuron;

/**
 *
 * @author PARHAM
 */
public class SignalQuantityRestriction implements Restriction {
    public final int limit;
    
    public SignalQuantityRestriction (int l) {
        limit = l;
    }
    
    @Override
    public boolean fulfil(Neuron net) {
        Object tmp = net.parameters.get(Neuron.RECIEVED_SIGNALS_NUM);
        return tmp != null && (Integer) tmp >= limit;
    }
}
