
package com.phm.comp.neuralnet.event;
 
import com.phm.comp.neuralnet.Neuron;
import java.util.Objects;

/**
 *
 * @author PARHAM
 */
public class NeuronAddedEvent {
    public final Neuron neuron;
    
    public NeuronAddedEvent (Neuron n) { 
        neuron = Objects.requireNonNull(n);
    }
}
