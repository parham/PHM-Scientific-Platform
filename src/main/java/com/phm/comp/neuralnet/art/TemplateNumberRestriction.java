
package com.phm.comp.neuralnet.art;

import com.phm.comp.neuralnet.Neuron;
import com.phm.comp.neuralnet.NeuronGroup;
import com.phm.comp.neuralnet.restric.Restriction;

/**
 *
 * @author phm
 */
public class TemplateNumberRestriction implements Restriction {

    @Override
    public boolean fulfil(Neuron net) {
        Object tmp = net.parameters.get(ARTTrainingSupervisor.ART_TEMPLATES_LIMIT);
        int nt = ((NeuronGroup)net).neurons.size();
        return tmp != null && ((int) tmp) >= nt;
    }
}
