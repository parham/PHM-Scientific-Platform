
package com.phm.comp.neuralnet.event;

import com.phm.comp.neuralnet.NeuronGroup;
import java.util.Objects;

/**
 *
 * @author PARHAM
 */
public class NGStopedTrainingEvent {
    public final NeuronGroup group;

    public NGStopedTrainingEvent(NeuronGroup ng) { 
        group = Objects.requireNonNull(ng);
    }
}
