
package com.phm.comp.neuralnet.art;

import com.phm.comp.neuralnet.Neuron;
import com.phm.comp.neuralnet.NeuronComparePolicy;
import com.phm.comp.neuralnet.NeuronGroup;
import com.phm.comp.neuralnet.Supervisor;
import com.phm.core.data.Features;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author phm
 */
public abstract class ARTTrainingSupervisor extends Supervisor {

    public static final String ART_VIGILANCE_PARAMTER = "art.vigilance.param";
    public static final String ART_RESONANCE_OCCURED = "art.resonance.occured";
    public static final String ART_TEMPLATES_LIMIT = "art.resonance.occured";
    
    public static final String NEURON_ACTIVATION_VALUE = "neuron.activation";
    public static final String NEURON_MATCHVALUE = "neuron.matchvalue";
    
    //public static final NeuronActivationComparePolicy COMPARE_POLICY = new NeuronActivationComparePolicy ();
    public static final NeuronComparePolicy COMPARE_POLICY = new NeuronComparePolicy (NEURON_ACTIVATION_VALUE);
    
    protected abstract double activate (NeuronGroup ngroup, Neuron neuron, Features signal);
    protected abstract double match (NeuronGroup ngroup, Neuron neuron, Features signal);
    protected abstract void update (NeuronGroup ngroup, List<Neuron> neuron, Features signal);
    protected abstract Neuron insert (NeuronGroup ngroup, Features signal);
    
    protected void beforeCalculateActivationValues (NeuronGroup ngroup, Features signal) {
        
    }
    protected void onCalculateActivationValues (NeuronGroup ngroup, Features signal) {
        ngroup.neurons.stream().forEach((Neuron x) -> {
            double value = activate(ngroup, x, signal);
            x.parameters.put(NEURON_ACTIVATION_VALUE, (float) value);
        });
    }
    protected void afterCalculateActivationValues (NeuronGroup ngroup, Features signal) {
        
    }
    
    protected List<Neuron> findHighestActivatedNeurons (NeuronGroup ngroup) {
        List<Neuron> ns = new LinkedList<>(ngroup.neurons);
        Neuron high = Collections.max(ns, COMPARE_POLICY);
        LinkedList<Neuron> list = new LinkedList<>();
        list.add(high);
        return list;
    }

    protected boolean doesResonanceOccured (NeuronGroup ngroup, 
                                            List<Neuron> neuron, 
                                            Features signal) {
        double reson = (float) ngroup.parameters.get(ART_VIGILANCE_PARAMTER);
        double nv = match(ngroup, neuron.get(0), signal);
        boolean res = nv > reson;
        ngroup.parameters.put(ART_RESONANCE_OCCURED, res);
        return res; 
    }
    
    protected void beforeResonanceOccured (NeuronGroup ngroup, 
                                           List<Neuron> neuron, 
                                           Features signal) {
        // Empty body
    }
    
    protected void onResonanceOccured (NeuronGroup ngroup, 
                                       List<Neuron> neuron, 
                                       Features signal) {
        update(ngroup, neuron, signal);
    }
    
    protected void afterResonanceOccured (NeuronGroup ngroup, 
                                          List<Neuron> neuron, 
                                          Features signal) {
        // Empty body
    }
    protected void beforeResonanceDoesntOccured (NeuronGroup ngroup, 
                                                 List<Neuron> neuron, 
                                                 Features signal) {
        // Empty body
    }
    protected void onResonanceDoesntOccured (NeuronGroup ngroup, 
                                             List<Neuron> neuron, 
                                             Features signal) {
        neuron.get(0).parameters.put(NEURON_ACTIVATION_VALUE, 0.0f);
    }
    protected void afterResonanceDoesntOccured (NeuronGroup ngroup,
                                                List<Neuron> neuron, 
                                                Features signal) {
        // Empty body
    }
    protected void onLastStep (NeuronGroup ngroup, Features signal) {
        // Empty body        
    }
    protected void onFinalization (NeuronGroup ngroup, Features signal) {
        // Empty body
    }
    
    @Override
    protected boolean superviseOperator(Neuron neuron, Features signal, List<Neuron> result) {
        NeuronGroup ngroup = (NeuronGroup) neuron;
        List<Neuron> neurons = new LinkedList<> (ngroup.neurons);
        boolean insertFlag = true;
        
        // Calculation Activation value for all templates
        beforeCalculateActivationValues (ngroup, signal);
        onCalculateActivationValues (ngroup, signal);
        afterCalculateActivationValues (ngroup, signal);

        for (Neuron neuron1 : neurons) {
            // Find the template with highest activation value
            List<Neuron> highActives = findHighestActivatedNeurons (ngroup);
            if (doesResonanceOccured (ngroup, highActives, signal)) {
                beforeResonanceOccured (ngroup, highActives, signal);
                onResonanceOccured (ngroup, highActives, signal);
                afterResonanceOccured (ngroup, highActives, signal);
                result.addAll (highActives);
                insertFlag = false;
                break;
            } else {
                onResonanceDoesntOccured(ngroup, highActives, signal);
            }
        }
        // Insertation procedure
        if (insertFlag) {
            Neuron nn = insert (ngroup, signal);
            result.add(nn);
        }
        
        onLastStep(ngroup, signal);
        onFinalization(ngroup, signal);
        return true;
    }
    
}
