
package com.phm.comp.neuralnet.art.fuzzy;
 
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
 * <b>Authors:</b> Gail A. Carpenter, Stephen Grossberg, Natalya Markuzon, John H. Reynolds, and David B. Rosen <br>
 * <b>Year:</b> 1992 <br>
 * <b>Title:</b> Fuzzy ARTMAP: A neural network architecture for incremental supervised learning of analog multidimensional maps <br>
 * <b>Published In:</b> IEEE Transactions on Neural Networks 3(5) <br>
 * <b>Page:</b> 698 - 713 <br>
 * <b>DOI:</b> 10.1109/72.159059 <br>
 * </p>
 * @author Parham Nooralishahi - PHM!
 * @email parham.nooralishahi@gmail.com
 */
public class FuzzyARTTrainingSupervisor extends ARTTrainingSupervisor {

    public static final String FUZZYART_CHOICE_PARAMTER = "fuzzyart.choices.param";
    public static final String FUZZYART_LEARNING_RATE = "fuzzyart.learning.rate";
    
    protected final int netDimension;
    
    public FuzzyARTTrainingSupervisor (int dim) {
        netDimension = dim;
    }
    
    @Override
    public String getName() {
        return "fuzzyart.train";
    }

    @Override
    public void initialize(Neuron ngroup) {
        ngroup.parameters.put(ART_VIGILANCE_PARAMTER, 0.92f);
        ngroup.parameters.put(FUZZYART_CHOICE_PARAMTER, 0.00001f);
        ngroup.parameters.put(FUZZYART_LEARNING_RATE, 0.5f);
        ngroup.parameters.put(ART_RESONANCE_OCCURED, false);
        ////////////
        ngroup.setInputStrategy(new FuzzyARTInputStrategy());
    }
    
    protected Neuron initializeFuzzyARTNeuron (NeuronGroup ng, Neuron n) {
        n.parameters.put(NEURON_ACTIVATION_VALUE, 0.0f);
        return n;
    }

    @Override
    protected NNResult prepareResult(Neuron neuron, Features signal, List<Neuron> winners, NNResult resc) {
        resc.parameters.put(Neuron.SYSTEM_STATUS, neuron.parameters.get(Neuron.SYSTEM_STATUS));
        resc.parameters.put(Neuron.NEURON_ID, neuron.parameters.get(Neuron.NEURON_ID));
        resc.parameters.put(Neuron.NEURON_CENTROID, neuron.parameters.get(Neuron.NEURON_CENTROID));
        resc.parameters.put(Neuron.NEURON_DIMENSION, neuron.parameters.get(Neuron.NEURON_DIMENSION));
        resc.parameters.put(Neuron.NEURON_NUM_CHANNEL, neuron.parameters.get(Neuron.NEURON_NUM_CHANNEL));
        resc.parameters.put(Neuron.NUM_CONNECTIONS, neuron.parameters.get(Neuron.NUM_CONNECTIONS));
        resc.parameters.put(Neuron.RECIEVED_SIGNALS_NUM, neuron.parameters.get(Neuron.RECIEVED_SIGNALS_NUM));
        resc.parameters.put(NeuronGroup.NUM_NEURONS, neuron.parameters.get(NeuronGroup.NUM_NEURONS));
        resc.parameters.put(FuzzyARTTrainingSupervisor.FUZZYART_CHOICE_PARAMTER, neuron.parameters.get(FuzzyARTTrainingSupervisor.FUZZYART_CHOICE_PARAMTER));
        resc.parameters.put(FuzzyARTTrainingSupervisor.FUZZYART_LEARNING_RATE, neuron.parameters.get(FuzzyARTTrainingSupervisor.FUZZYART_LEARNING_RATE));
        resc.parameters.put(FuzzyARTTrainingSupervisor.ART_RESONANCE_OCCURED, neuron.parameters.get(FuzzyARTTrainingSupervisor.ART_RESONANCE_OCCURED));
        resc.parameters.put(FuzzyARTTrainingSupervisor.ART_TEMPLATES_LIMIT, neuron.parameters.get(FuzzyARTTrainingSupervisor.ART_TEMPLATES_LIMIT));
        resc.parameters.put(FuzzyARTTrainingSupervisor.ART_VIGILANCE_PARAMTER, neuron.parameters.get(FuzzyARTTrainingSupervisor.ART_VIGILANCE_PARAMTER));
        resc.parameters.put(FuzzyARTTrainingSupervisor.NEURON_ACTIVATION_VALUE, neuron.parameters.get(FuzzyARTTrainingSupervisor.NEURON_ACTIVATION_VALUE));
        return resc;
    }

    @Override
    protected double activate(NeuronGroup ngroup, Neuron neuron, Features signal) {
        double choiceValue = (float) ngroup.parameters.get(FuzzyARTTrainingSupervisor.FUZZYART_CHOICE_PARAMTER);
        double vsubs = 0;
        double vn = 0;
        final int ndims = neuron.getDimension();
        for (int index = 0; index < ndims; index++) {
            vsubs += Math.min(neuron.getEntry(index), signal.getEntry(index));
            vn += neuron.getEntry(index);
        }
        double res = vsubs / (choiceValue + vn);
        
        return res;
    }

    @Override
    protected double match(NeuronGroup ngroup, Neuron neuron, Features signal) {
        double vsubs = 0;
        double vs = 0;
        int ndims = neuron.getDimension();
        for (int index = 0; index < ndims; index++) {
            vsubs += Math.min(signal.getEntry(index), neuron.getEntry(index));
            vs += signal.getEntry(index);
        }
        return vsubs / vs;
    }

    @Override
    protected void update(NeuronGroup ngroup, List<Neuron> neuron, Features signal) {
        Neuron win = neuron.get(0);
        double learningRate = (float) ngroup.parameters.get(FuzzyARTTrainingSupervisor.FUZZYART_LEARNING_RATE);
        final int ndims = win.getDimension();
        
        for (int index = 0; index < ndims; index++) {
            win.setEntry(index, ((1 - learningRate) * win.getEntry(index)) + 
                                     (learningRate * Math.min(win.getEntry(index), signal.getEntry(index))));
        }
    }

    @Override
    protected Neuron insert(NeuronGroup ngroup, Features signal) {
        double [] sd = new double[signal.getDimension()];
        for (int index = 0; index < sd.length; index++) {
            sd [index] = signal.getEntry(index);
        }
        Neuron newNeuron = initializeFuzzyARTNeuron(ngroup, new Neuron(sd));
        ngroup.addInternalNeuron(newNeuron);
        ngroup.eventBus.post(new NeuronAddedEvent(newNeuron));
        return newNeuron;
    }
}
