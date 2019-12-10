
package com.phm.comp.neuralnet.art;

import com.phm.comp.neuralnet.Neuron;
import com.phm.comp.neuralnet.NeuronComparePolicy;
import com.phm.comp.neuralnet.NeuronGroup;
import com.phm.comp.neuralnet.Supervisor;
import com.phm.core.ArraySet;
import com.phm.core.data.Features;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author phm
 */
public abstract class ARTMAPTrainingSupervisor extends Supervisor {
    public static final String DEFAULT_CLASS = "nolabel";
    public static final String ARTMAP_DCLASS_LIST = "artmap.dclass.list";
    public static final String ARTMAP_VIGILANCE_PARAMTER = "artmap.vigilance.param";
    public static final String ARTMAP_RESONANCE_OCCURED = "artmap.resonance.occured";
    public static final String ARTMAP_TEMPLATES_LIMIT = "artmap.templates.limits";
    public static final String ARTMAP_EPSILON = "artmap.epsilon";
    
    public static final String NEURON_ACTIVATION_VALUE = "neuron.activation";
    public static final String NEURON_ACTIVATION_BACKUP = "neuron.activation.backup";
    public static final String NEURON_MATCHVALUE = "neuron.matchvalue";
    
    //public static final NeuronActivationComparePolicy COMPARE_POLICY = new NeuronActivationComparePolicy ();
    public static final NeuronComparePolicy COMPARE_POLICY = new NeuronComparePolicy (NEURON_ACTIVATION_VALUE);
    
    protected abstract double activate (NeuronGroup ngroup, Neuron neuron, Features signal);
    protected abstract double match (NeuronGroup ngroup, Neuron neuron, Features signal);
    protected abstract void update (NeuronGroup ngroup, List<Neuron> neuron, Features signal);
    protected abstract Neuron insert (NeuronGroup ngroup, Features signal);
    
    protected float vigilTemp = 0;
    protected boolean isChanged = false;
    
    protected void beforeCalculateActivationValues (NeuronGroup ngroup, Features signal) {
        // Empty body
    }
    protected void onCalculateActivationValues (NeuronGroup ngroup, Features signal) {
        ngroup.neurons.stream().forEach((Neuron x) -> {
            double value = activate(ngroup, x, signal);
            x.parameters.put(NEURON_ACTIVATION_VALUE, (float) value);
            x.parameters.put(NEURON_ACTIVATION_BACKUP, (float) value);
        });
    }
    protected void afterCalculateActivationValues (NeuronGroup ngroup, Features signal) {
        
    }
    
    protected List<Neuron> findHighestActivatedNeurons (NeuronGroup ngroup) {
        List<Neuron> ns = new LinkedList<>(ngroup.neurons);
        Neuron high = Collections.max (ns, COMPARE_POLICY);
        LinkedList<Neuron> list = new LinkedList<>();
        list.add(high);
        return list;
    }
    protected void afterFindHighestActivatedNeurons (NeuronGroup ngroup, List<Neuron> winners) {
        
    }

    protected boolean doesResonanceOccured (NeuronGroup ngroup, 
                                            List<Neuron> neuron, 
                                            Features signal) {
        double reson = (float) ngroup.parameters.get (ARTMAP_VIGILANCE_PARAMTER);
        double nv = match(ngroup, neuron.get(0), signal);
        boolean res = nv > reson;
        ngroup.parameters.put (ARTMAP_RESONANCE_OCCURED, res);
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
        
    }
    protected void beforeResonanceDoesntOccured (NeuronGroup ngroup, 
                                                 List<Neuron> neuron, 
                                                 Features signal) {
        // Empty body
    }
    protected void onResonanceDoesntOccured (NeuronGroup ngroup, 
                                             List<Neuron> neuron, 
                                             Features signal) {
        neuron.get(0).parameters.put (NEURON_ACTIVATION_VALUE, 0.0f);
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
        if (isChanged) {
            isChanged = false;
            ngroup.parameters.put (ARTMAP_VIGILANCE_PARAMTER, vigilTemp);
        }
    }
    protected boolean labelExists (NeuronGroup ngroup, Features signal) {
        ArraySet<String> clss = (ArraySet<String>) ngroup.parameters.get(ARTMAP_DCLASS_LIST);
        return clss.contains((String) signal.getLabel());
    }
    protected void addClass (NeuronGroup ngroup, Features signal) {
        ArraySet<String> clss = (ArraySet<String>) ngroup.parameters.get(ARTMAP_DCLASS_LIST);
        clss.add((String) signal.getLabel());
        ngroup.parameters.put(ARTMAP_DCLASS_LIST, clss);
    }
    protected boolean labelMatched (NeuronGroup ngroup, List<Neuron> winner, Features signal) {
        String nlbl = (String) winner.get(0).getLabel();
        String slbl = (String) signal.getLabel();
        return nlbl.contentEquals(slbl);
    }
    protected void decreaseResonanceThresh (NeuronGroup ngroup, List<Neuron> winners, Features signal) {
        onResonanceDoesntOccured(ngroup, winners, signal);
        float vigilthresh = (float) ngroup.parameters.get(ARTMAP_VIGILANCE_PARAMTER);
        float actv = (float) winners.get(0).parameters.get(NEURON_MATCHVALUE);
        float epsil = (float) ngroup.parameters.get(ARTMAP_EPSILON);
        float temp = actv + epsil;
        ////////////////
        isChanged = true;
        vigilTemp = vigilthresh;
        ngroup.parameters.put(ARTMAP_VIGILANCE_PARAMTER, temp);
    }
    protected boolean passSignal (Neuron neuron, Features signal) {
        return true;
    }
    @Override
    protected boolean superviseOperator(Neuron neuron, Features signal, List<Neuron> result) {
        NeuronGroup ngroup = (NeuronGroup) neuron;
        List<Neuron> neurons = new LinkedList<>(ngroup.neurons);
        boolean insertFlag = true;
        if (passSignal(neuron, signal)) {
            if (labelExists((NeuronGroup) neuron, signal)) {
                // Calculation Activation value for all templates
                beforeCalculateActivationValues(ngroup, signal);
                onCalculateActivationValues(ngroup, signal);
                afterCalculateActivationValues(ngroup, signal);

                for (int index = 0; index < neurons.size(); index++) {
                    // Find the template with highest activation value
                    List<Neuron> highActives = findHighestActivatedNeurons (ngroup);
                    afterFindHighestActivatedNeurons(ngroup, highActives);
                    if (doesResonanceOccured (ngroup, highActives, signal)) {
                        if (labelMatched(ngroup, highActives, signal)) {
                            beforeResonanceOccured (ngroup, highActives, signal);
                            onResonanceOccured (ngroup, highActives, signal);
                            afterResonanceOccured (ngroup, highActives, signal);
                            result.addAll (highActives);
                            insertFlag = false;
                            break;
                        } else {
                            decreaseResonanceThresh(ngroup, highActives, signal);
                        }
                    } else {
                        onResonanceDoesntOccured(ngroup, highActives, signal);
                    }
                }
            } else {
                addClass ((NeuronGroup) neuron, signal);
            }
        
            // Insertation procedure
            if (insertFlag) {
                Neuron nn = insert (ngroup, signal);
                result.add(nn);
            }

            onLastStep(ngroup, signal);
            onFinalization(ngroup, signal);
        }
        return true;
    }
}
