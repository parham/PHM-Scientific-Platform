
package com.phm.comp.neuralnet.art.gaussian;

import com.phm.comp.filter.BlankFilter;
import com.phm.comp.neuralnet.NNResult;
import com.phm.comp.neuralnet.Neuron;
import com.phm.comp.neuralnet.NeuronGroup;
import com.phm.comp.neuralnet.art.ARTTrainingSupervisor;
import com.phm.comp.neuralnet.event.NeuronAddedEvent;
import com.phm.core.data.Features;
import java.util.List;

/**
 * <p>
 * <b>Publication details:<br></b>
 * <b>Authors:</b> James R. Williamson <br>
 * <b>Year:</b> 1998 <br>
 * <b>Title:</b> Gaussian ARTMAP: A Neural Network for Fast Incremental Learning of Noisy Multidimensional Maps <br>
 * <b>Published In:</b> Neural Networks 9(5) <br>
 * <b>Page:</b> 881–897 <br>
 * <b>DOI:</b> 10.1016/0893-6080(95)00115-8 <br>
 * </p>
 * @author Parham Nooralishahi - PHM!
 * @email parham.nooralishahi@gmail.com
 */
public class GaussianARTTrainingSupervisor extends ARTTrainingSupervisor {

    public static final String NEURON_PROBABILITY = "neuron.probability";
    public static final String NEURON_CODEDSIGNAL = "neuron.coded.signals";
    public static final String NEURON_DEFAULT_STD = "neuron.def.std";
    public static final String NEURON_STD = "neuron.std";
    
    @Override
    public String getName() {
        return "gaussianart.train";
    }

    @Override
    public void initialize(Neuron ngroup) {
        ngroup.parameters.put(ART_VIGILANCE_PARAMTER, 0.96f);
        ngroup.parameters.put(ART_RESONANCE_OCCURED, false);
        ngroup.parameters.put(NEURON_DEFAULT_STD, 0.9f);
        /////////////////////
        ngroup.setInputStrategy(new BlankFilter ());
    }

    protected Neuron initializeGaussianARTNeuron (NeuronGroup ngroup, 
                                                  Neuron n) {
        n.parameters.put(NEURON_ACTIVATION_VALUE, 0.0f);
        n.parameters.put(NEURON_MATCHVALUE, 0.0f);
        n.parameters.put(NEURON_PROBABILITY, 0.0f);
        n.parameters.put(NEURON_CODEDSIGNAL, 1.0f);
        n.parameters.put(NEURON_STD, ngroup.parameters.get(NEURON_DEFAULT_STD));
        
        return n;
    }
    
    @Override
    protected Neuron insert (NeuronGroup ngroup, 
                             Features signal) {
        // Initialize new neuron
        double [] dimstd = new double [signal.getDimension()];
        float defstd = (float) ngroup.parameters.get(NEURON_DEFAULT_STD);
        for (int index = 0; index < dimstd.length; index++) {
            dimstd [index] = defstd;
        }
        /////////////////////////
        Neuron newNeuron = initializeGaussianARTNeuron (ngroup, new Neuron(signal));
        newNeuron.parameters.put(NEURON_STD, dimstd);
        ngroup.addInternalNeuron(newNeuron);
        ngroup.eventBus.post(new NeuronAddedEvent(newNeuron));
        return newNeuron;
    }

    @Override
    protected NNResult prepareResult(Neuron neuron, Features signal, List<Neuron> winners, NNResult resc) {
        return new NNResult(System.currentTimeMillis(), neuron.parameters, winners);
    }

    @Override
    protected double activate(NeuronGroup ngroup, Neuron neuron, Features signal) {
        float p = (float) ngroup.parameters.get (ART_VIGILANCE_PARAMTER);
        float nj = (float) neuron.parameters.get (NEURON_CODEDSIGNAL);
        double [] stddim = (double[]) neuron.parameters.get(NEURON_STD);
        float Gj = 0.0f;
        double mulstd = 1.0;

        for (int dim = 0; dim < stddim.length; dim++) {
            double tmp = 0.0f;
            mulstd *= stddim [dim];
            tmp = (signal.getEntry(dim) - neuron.getEntry(dim)) / stddim [dim];
            Gj += tmp * tmp;
        }
        Gj = (float) Math.exp(-0.5f * Gj);
        mulstd = nj / mulstd;
        float gj = (float) (Gj > p ? mulstd * Gj : 0.0f);
        neuron.parameters.put(NEURON_ACTIVATION_VALUE, gj);
        neuron.parameters.put(NEURON_MATCHVALUE, Gj);
        return gj;
    }

    @Override
    protected double match(NeuronGroup ngroup, Neuron neuron, Features signal) {
        float gmatch = (float) neuron.parameters.get (GaussianARTTrainingSupervisor.NEURON_MATCHVALUE);
        return gmatch;
    }

    @Override
    protected void update(NeuronGroup ngroup, List<Neuron> neuron, Features signal) {
            Neuron win = neuron.get(0);
            float nj = (float) win.parameters.get(NEURON_CODEDSIGNAL);
            double [] stddim = (double []) win.parameters.get(NEURON_STD);
            nj++;
            for (int dim = 0; dim < stddim.length; dim++) {
                win.setEntry(dim, ((1 - (1 / nj)) * win.getEntry(dim)) + ((1 / nj) * signal.getEntry(dim)));
                stddim [dim] = (float) Math.sqrt (((1 - (1/nj)) * (stddim[dim] * stddim[dim])) + 
                                         ((1/nj) * (signal.getEntry(dim) - win.getEntry(dim))
                                                 * (signal.getEntry(dim) - win.getEntry(dim))));
            }
            win.parameters.put(NEURON_STD, stddim);
            win.parameters.put(NEURON_CODEDSIGNAL, nj);
    }
}
