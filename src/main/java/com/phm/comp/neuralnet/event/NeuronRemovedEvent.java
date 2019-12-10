
package com.phm.comp.neuralnet.event;
 
import com.phm.comp.neuralnet.Neuron;
import java.util.Objects;

/**
 *
 * @author PARHAM
 */
public class NeuronRemovedEvent {
    public final Neuron neuron;
    
    public NeuronRemovedEvent (Neuron n) { 
        neuron = Objects.requireNonNull(n);
    }
}
