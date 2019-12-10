
package com.phm.comp.neuralnet;

import com.phm.core.data.Features;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author Parham Nooralishahi - PHM!
 */
public class NeuronsContainer extends HashSet<Neuron> implements Iterable<Neuron>  {
    
    public NeuronsContainer () {
        super ();
    }
    public NeuronsContainer (Collection<? extends Neuron> ns) {
        super (ns);
    }
    public NeuronsContainer (int cs) {
        super (cs);
    }
   
    public void toDataset (List<Neuron> ds) {
        ds.addAll(this);
    }
    public Neuron getByID (int id) {
        Neuron neuron = null;
        for (Neuron n : this) {
            if (n.getID() == id) {
                neuron = n;
            }
        }
        return neuron;
    }
    
    public Neuron getIndex (int index) {
        int d = 0;
        for (Neuron n : this) {
            if (index >= d) {
                d++;
                return n;
            }
        }
        return null;
    }
    public void feed (String state, final Features signal, List<NNResult> result) {
        for (Neuron n : this) {
            n.feed(state, signal, result);
        }
    }
    
}
