
package com.phm.comp.neuralnet.event;
 
import com.phm.comp.neuralnet.NeuronGroup;
import java.util.Objects;

/**
 *
 * @author PARHAM
 */
public class NGUpdatedEvent {
    public final NeuronGroup group;
    
    public NGUpdatedEvent (NeuronGroup ng) { 
        group = Objects.requireNonNull(ng);
    }
}
