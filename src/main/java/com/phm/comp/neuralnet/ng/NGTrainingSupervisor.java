
package com.phm.comp.neuralnet.ng;

import com.phm.comp.distance.ApacheWrapperDistanceMeasure;
import com.phm.comp.distance.DistanceInfo;
import com.phm.comp.neuralnet.NNResult;
import com.phm.comp.neuralnet.Neuron;
import com.phm.comp.neuralnet.NeuronComparePolicy;
import com.phm.comp.neuralnet.NeuronDistanceMethod;
import com.phm.comp.neuralnet.NeuronGroup;
import com.phm.comp.neuralnet.NeuronsContainer;
import com.phm.comp.neuralnet.Supervisor; 
import com.phm.core.data.Features;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.math3.ml.distance.EuclideanDistance;

/**
 *
 * @author phm
 */
public class NGTrainingSupervisor extends Supervisor {
    
    public static final String NEURON_DISTANCE = "neuron.distance";
    public static final String NEURON_DIS_DIMENSION = "neuron.local.distance";
    public static final String NEURON_ASSIGNED_VALUE = "assigned.value";
    
    public static final String NEURON_NUMBER = "neuron.num";
    public static final String SIGNALS_NUMBER = "signals.num";
    
    public static final String NG_EI = "ei";
    public static final String NG_EF = "ef";
    public static final String NG_LI = "li";
    public static final String NG_LF = "lf";
    
    public static final NeuronComparePolicy COMPARE_POLICY = new NeuronComparePolicy (NEURON_DISTANCE);
    
    protected final NeuronDistanceMethod distanceMethod;
    protected final int netDimension;
    protected final int numNeurons;
    protected final int numMaxSignals;
    // protected boolean init = false;
    
    public NGTrainingSupervisor (NeuronDistanceMethod dm, int dim, int nn, int ns) {
        netDimension = dim;
        numNeurons = nn;
        numMaxSignals = ns;
        distanceMethod = Objects.requireNonNull(dm);
    }
    
    public NGTrainingSupervisor (int dim, int nn, int ns) {
        this (new NeuronDistanceMethod (new ApacheWrapperDistanceMeasure(new EuclideanDistance())), dim, nn, ns);
    }
    
    @Override
    public void initialize(Neuron ngroup) {
        ngroup.parameters.put(NG_EI, 0.3f);
        ngroup.parameters.put(NG_EF, 0.05f);
        ngroup.parameters.put(NG_LI, 30.0f);
        ngroup.parameters.put(NG_LF, 0.01f);
        
        ngroup.parameters.put(SIGNALS_NUMBER, numMaxSignals);
        ngroup.parameters.put(NEURON_NUMBER, numNeurons);
        ngroup.parameters.put(Neuron.NEURON_DIMENSION, netDimension);
    }
    
    private void onDistanceCalculation (NeuronGroup ngroup, Features signal) {
        ngroup.neurons.stream().parallel().forEach((Neuron x) -> {
            DistanceInfo dd = distanceMethod.distance(ngroup, x, signal);
            x.parameters.put(NEURON_DISTANCE, (float) dd.distance);
            x.parameters.put(NEURON_DIS_DIMENSION, dd.distancedim);
        });
    }
    
    private List<Neuron> sortAndUpdateNeurons (NeuronsContainer nc) {
        LinkedList<Neuron> ns = new LinkedList<>(nc);
        Collections.sort(ns, COMPARE_POLICY);
        // Update Assigned Value in each neuron
        for (int index = 0; index < ns.size(); index++) {
            ns.get(index).parameters.put(NEURON_ASSIGNED_VALUE, (float) index);
        }
        return ns;
    }
    
    public NeuronDistanceMethod getDistanceMethod () {
        return distanceMethod;
    }
    
    protected Neuron initializeNewNeuron (Neuron neuron) {
        neuron.parameters.put(NEURON_DISTANCE, 0.0f);
        neuron.parameters.put(NEURON_DIS_DIMENSION, new float [netDimension]);
        neuron.parameters.put(NEURON_DISTANCE, 0.0f);
        neuron.parameters.put(NEURON_ASSIGNED_VALUE, 0.0f);
        
        return neuron;
    }
    
    protected void beforeInitialPhase (NeuronGroup ngroup, Features signal) {
        // Empty body
    }
    
    protected Neuron onInitialPhase(NeuronGroup ngroup, Features signal) {
        Neuron n = initializeNewNeuron (new Neuron(signal));
        ngroup.addInternalNeuron(n);
        return n;
    }
    
    protected void afterInitialPhase(NeuronGroup ngroup, Features signal, Neuron nn) {
        // Empty body
    }
    
    protected void afterDistanceCalculation (NeuronGroup ngroup, Features signal) {
        // Empty body
    }
    
    protected List<Neuron> onNGUpdate (NeuronGroup ngroup, Features signal) {
        final int numMaxSignal = (int) ngroup.parameters.get(SIGNALS_NUMBER);
        final int numSignal = (int) ngroup.parameters.get(Neuron.RECIEVED_SIGNALS_NUM);
        final float ei = (float) ngroup.parameters.get(NG_EI);
        final float ef = (float) ngroup.parameters.get(NG_EF);
        final float li = (float) ngroup.parameters.get(NG_LI);
        final float lf = (float) ngroup.parameters.get(NG_LF);
        
        List<Neuron> sortedNeuron = sortAndUpdateNeurons(ngroup.neurons);
        // Update Neuron weights
        sortedNeuron.stream().parallel().forEach((Neuron n) -> {
            float k = (Float) n.parameters.get(NEURON_ASSIGNED_VALUE);
            float e = (ei * ((float) Math.pow((ef / ei), (numSignal / numMaxSignal))));
            float l = li * ((float) Math.pow((lf / li), (numSignal / numMaxSignal)));
            float h = (float) Math.exp(-k / l);
            final int ndims = n.getDimension();
            for (int index = 0; index < ndims; index++) {
                double value = n.getEntry(index) + ((e * h) * (signal.getEntry(index) - n.getEntry(index)));
                n.setEntry(index, value);
            }
        });
        return sortedNeuron;
    }
    
    protected void onNGLastStep (NeuronGroup ngroup, Features signal, List<Neuron> result) {
        // 
    }
    
    protected void onNGFinalization (NeuronGroup ngroup, Features signal, List<Neuron> result) {
        // Empty body
    }
    
    @Override
    protected boolean superviseOperator (Neuron neuron, Features s, List<Neuron> result) {
        // Initialize needed neurons in case there isn't enough
        // neuron available inside the network
        NeuronGroup ngroup = (NeuronGroup) neuron;
        //final int numNeurons = (int) ngroup.parameters.get(NEURON_NUMBER);
        if (ngroup.neurons.size() <= numNeurons) {
            // Before Initial Phase
            beforeInitialPhase(ngroup, s);
            // On Initial Phase
            Neuron n = onInitialPhase(ngroup, s);
            result.add(n);
            // After Initial Phase
            afterInitialPhase(ngroup, s, n);
        } else {
            // Order all elements of A, according to their distance to e
            // i.e. find the sequence of indices (i_0,i_1,…,i_(N-1) ) such
            // that w_i0 is the reference vector closest to ?, w_i1 is the reference
            // Finding Distance
            onDistanceCalculation(ngroup, s);
            // After Distance Calculation
            afterDistanceCalculation(ngroup, s);
            // NG Update
            List<Neuron> sn = onNGUpdate(ngroup, s);
            result.addAll(sn);
            // After NG Update
            onNGLastStep (ngroup, s, sn);
        }
        onNGFinalization (ngroup, s, result);
        return true;
    }

    @Override
    public String getName() {
        return "ng.train";
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
        resc.parameters.put(NGTrainingSupervisor.NEURON_DISTANCE, neuron.parameters.get(NGTrainingSupervisor.NEURON_DISTANCE));
        resc.parameters.put(NGTrainingSupervisor.NEURON_ASSIGNED_VALUE, neuron.parameters.get(NGTrainingSupervisor.NEURON_ASSIGNED_VALUE));
        resc.parameters.put(NGTrainingSupervisor.NEURON_DIS_DIMENSION, neuron.parameters.get(NGTrainingSupervisor.NEURON_DIS_DIMENSION));
        resc.parameters.put(NGTrainingSupervisor.NEURON_NUMBER, neuron.parameters.get(NGTrainingSupervisor.NEURON_NUMBER));
        resc.parameters.put(NGTrainingSupervisor.NG_EF, neuron.parameters.get(NGTrainingSupervisor.NG_EF));
        resc.parameters.put(NGTrainingSupervisor.NG_EI, neuron.parameters.get(NGTrainingSupervisor.NG_EI));
        resc.parameters.put(NGTrainingSupervisor.NG_LF, neuron.parameters.get(NGTrainingSupervisor.NG_LF));
        resc.parameters.put(NGTrainingSupervisor.NG_LI, neuron.parameters.get(NGTrainingSupervisor.NG_LI));
        resc.parameters.put(NGTrainingSupervisor.SIGNALS_NUMBER, neuron.parameters.get(NGTrainingSupervisor.SIGNALS_NUMBER));
        
        return resc;
    }
}
