
package com.phm.comp.neuralnet;

import com.phm.comp.neuralnet.restric.RestrictionsContainer;
import com.phm.core.data.Features;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects; 

/**
 *
 * @author phm
 */
public abstract class Supervisor {
    
    public final RestrictionsContainer restrictions = new RestrictionsContainer();
    
    public abstract String getName ();
    public abstract void initialize (Neuron neuron);
    protected abstract boolean superviseOperator (Neuron neuron, Features signal, List<Neuron> result);
    protected abstract NNResult prepareResult (Neuron neuron, Features signal, List<Neuron> winners, NNResult resc);
    
    public boolean supervise (Neuron neuron, Features signal, List<NNResult> res) {
        boolean status = false;
        if (!restrictions.fulfil(neuron)) {
            LinkedList<Neuron> result = new LinkedList<>();
            status = superviseOperator(neuron, signal, result);
            // Generate result
            int numsig = (int) neuron.parameters.get(Neuron.RECIEVED_SIGNALS_NUM);
            NNResult resc = new NNResult (numsig, null, new LinkedList<>(result), signal);
            prepareResult(neuron, signal, result, resc);
            res.add(resc);
        }
        return status;
    }
    
    public boolean isFulfil (Neuron neuron) {
        return restrictions.fulfil(neuron);
    }
    
    @Override
    public String toString () {
        return getName ();
    }
    @Override
    public boolean equals (Object obj) {
        return obj != null && 
               obj instanceof Supervisor &&
               obj.hashCode() == hashCode();
    }
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(getName());
        return hash;
    }
}
