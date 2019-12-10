
package com.phm.comp.neuralnet.restric;

import com.phm.comp.neuralnet.Neuron;
import java.util.LinkedList;

/**
 *
 * @author PARHAM
 */
public class RestrictionsContainer extends LinkedList<Restriction> {
    public boolean fulfil (final Neuron net) {
        return this.size() > 0 && !(this.stream().map((Restriction x) -> x.fulfil(net)).allMatch((f) -> !f));
    }
}
