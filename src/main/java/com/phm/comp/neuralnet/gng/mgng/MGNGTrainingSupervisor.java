
package com.phm.comp.neuralnet.gng.mgng;

import com.phm.comp.distance.DistanceInfo;
import com.phm.comp.neuralnet.NNResult;
import com.phm.comp.neuralnet.Neuron;
import com.phm.comp.neuralnet.NeuronAnalyzer;
import com.phm.comp.neuralnet.NeuronDistanceMethod;
import com.phm.comp.neuralnet.NeuronGroup;
import com.phm.comp.neuralnet.connection.Connection;
import com.phm.comp.neuralnet.gng.GNGTrainingSupervisor;
import com.phm.core.ParametersContainer;
import com.phm.core.ProcessOnParameter;
import com.phm.core.data.Features;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author phm 
 */
public class MGNGTrainingSupervisor extends GNGTrainingSupervisor {

    public static final String NEURON_LOCAL_CONTEXT = "neuron.lcontext";
    
    ///// Merging GNG parameters
    public static final String MGNG_GLOBAL_CONTEXT = "mgng.gcontext";
    public static final String MGNG_NI = GNG_ERRORRATE_B_KEY;
    public static final String MGNG_DELTA = GNG_ERRORRATE_A_KEY;
    public static final String MGNG_ALPHA = "mgng.alpha";
    public static final String MGNG_BETA = "mgng.beta";
    public static final String MGNG_THETA = GNG_LANDA_KEY;
    
    public MGNGTrainingSupervisor (NeuronDistanceMethod dm) {
        super (dm);
    }
    public MGNGTrainingSupervisor () {
        this (new NeuronDistanceMethod(new MGNGContextDistanceMethod()));
    }
    
    @Override
    public void initialize (Neuron ngroup) {
        super.initialize(ngroup);
        double [] df = new double [ngroup.getDimension()];
        ngroup.parameters.put(MGNG_GLOBAL_CONTEXT, df);
        ngroup.parameters.put(MGNG_NI, 0.9995f);
        ngroup.parameters.put(MGNG_DELTA, 0.5f);
        ngroup.parameters.put(MGNG_ALPHA, 0.5f);
        ngroup.parameters.put(MGNG_BETA, 0.75f);
        ngroup.parameters.put(MGNG_THETA, 100);
        ngroup.analyzers.add(new GlobalContextUpdateAnalyzer());
    }
    
    protected Neuron initializeMGNGNeuron (NeuronGroup ngroup, Neuron neuron) {
        this.initializeGNGNeuron (ngroup, neuron);
        neuron.parameters.put(NEURON_LOCAL_CONTEXT, new float [neuron.getDimension()]);
        return neuron;
    }
    @Override
    protected void onDistanceCalculation (NeuronGroup ngroup, Features signal) {
        ngroup.neurons.stream().forEach((Neuron x) -> {
            ngroup.parameters.put(NEURON_LOCAL_CONTEXT, x.parameters.get(NEURON_LOCAL_CONTEXT));
            DistanceInfo dd = distanceMethod.distance(ngroup, x, signal);
            x.parameters.put(NEURON_DISTANCE, (float) dd.distance);
            x.parameters.put(NEURON_DIS_DIMENSION, dd.distancedim);
        });
    }
    @Override
    protected Neuron onInitialPhase(NeuronGroup ngroup, Features buffer) {
        Neuron nn = initializeMGNGNeuron(ngroup, new Neuron(buffer));
        ngroup.addInternalNeuron(nn);
        return nn;
    }
    
    // Update Winners stage /////////////////////////
    @Override
    protected void onWinnersUpdate (NeuronGroup ngroup, 
                                    List<Neuron> winners, 
                                    Features signal) {
        //float epsilonB = (Float) ngroup.parameters.get(GNG_EPSILON_B_KEY);
        float epsilonN = (Float) ngroup.parameters.get(GNG_EPSILON_N_KEY);
        // float [] buffer = signal.cdata(0);
        // Create Connection between two nearest neurons
        Connection c = new Connection(winners.get(0), winners.get(1), 0);
        ngroup.updateInternalConnection(c);
        ////////////////////////////////////////////////////////
        // Add squared error of winner unit to a local error
        winners.get(0).parameters.processOnParameter(NEURON_LOCALERROR, new CalculateWinnerError());
        ////////////////////////////////////////////////////////
        // Update neuron weights
        final float [] gc = (float []) ngroup.parameters.get (MGNG_GLOBAL_CONTEXT);
        winners.get(0).connections.neighbors().stream().parallel().forEach((Neuron tx) -> {
            Neuron x = tx;
            Features dimDist = (Features) x.parameters.get (NEURON_DIS_DIMENSION);
	    float [] lc = (float []) x.parameters.get (NEURON_LOCAL_CONTEXT);
            final int ndims = signal.getDimension();
            double [] cs = new double [ndims];
            for (int dim = 0; dim < ndims; dim++) {
                cs [dim] = x.getEntry(dim) + (dimDist.getEntry(dim) * epsilonN);
                x.setEntry(dim, cs [dim]);
                lc [dim] += epsilonN * (gc [dim] - lc [dim]);
            }
	    x.parameters.put(NEURON_LOCAL_CONTEXT, lc);
        });
        // Update winner neuron's weight
        final int ndims = signal.getDimension();
        Features dimDist = (Features) winners.get(0).parameters.get(NEURON_DIS_DIMENSION);
	float [] lc = (float []) winners.get(0).parameters.get(NEURON_LOCAL_CONTEXT);
        for (int dim = 0; dim < ndims; dim++) {
            double tmp = winners.get(0).getEntry(dim) + (dimDist.getEntry(dim) * epsilonN);
            winners.get(0).setEntry(dim, tmp);
            lc [dim] += epsilonN * (gc [dim] - lc [dim]);
        }
	winners.get(0).parameters.put(NEURON_LOCAL_CONTEXT, lc);
        ////////////////////////////////////////////////////////
        // Increase winner neuron's connection's age
        winners.get(0).connections.incerementConnectionsEdgeValue();
    }
    
    @Override
    protected void onNeuronInsertion (NeuronGroup ngroup, Features signal) {
        float delta = (Float) ngroup.parameters.get(MGNG_DELTA);
        // Find the neural unit with highest accumulated error
        List<Neuron> ns = new LinkedList<>(ngroup.neurons);
        Neuron highestLE = (Neuron) ns.get(0);
        for (int index = 1; index < ns.size(); index++) {
            float hn = (Float) highestLE.parameters.get(NEURON_LOCALERROR);
            float tempn = (Float) ns.get(index).parameters.get(NEURON_LOCALERROR);
            if (hn < tempn) {
                highestLE = ns.get(index);
            }
        }
        // Find the neighbor of q which has the highest accumulated error
        List<Neuron> neighbors = highestLE.connections.neighbors();
        Neuron hMaxLENEighbor = neighbors.get(0);
        for (int index = 1; index < neighbors.size(); index++) {
            Neuron temp2 = neighbors.get(index);
            float maxle = (Float) hMaxLENEighbor.parameters.get(NEURON_LOCALERROR);
            float tle = (Float) temp2.parameters.get(NEURON_LOCALERROR);
            if (tle > maxle) {
                hMaxLENEighbor = temp2;
            }
        }
        // Put new neuron in to the system
        Neuron newNeuron = initNewNeuron (ngroup, highestLE, hMaxLENEighbor);
        ngroup.addInternalNeuron(newNeuron);
        // Remove Edges between the selected neurons
        ngroup.removeInternalConnection(new Connection(highestLE, hMaxLENEighbor));
        // Add edge between new node and f and q
        ngroup.updateInternalConnection(new Connection(newNeuron, highestLE, 0));
        ngroup.updateInternalConnection(new Connection(newNeuron, hMaxLENEighbor, 0));
            // Prepare error vector for new node
        // Decrease Error value of neuron f and q
        highestLE.parameters.processOnParameter(NEURON_LOCALERROR, new UpdateErrorNeuron(1 - delta));
        hMaxLENEighbor.parameters.processOnParameter(NEURON_LOCALERROR, new UpdateErrorNeuron(1 - delta));
    }
    
    @Override
    protected void onGNGLastStep (NeuronGroup ngroup, Features signal) {
        float ni = (Float) ngroup.parameters.get(MGNG_NI);
        // Decrease error of all units
        float bErrorRate = (Float) ngroup.parameters.get(GNG_ERRORRATE_B_KEY);
        ngroup.neurons.stream().parallel().forEach((Neuron x) -> {
            x.parameters.processOnParameter(NEURON_LOCALERROR, new UpdateErrorNeuron(ni));
        });
    }
    
    @Override
    public String getName() {
        return "mgng.train";
    }
    
    @Override
    protected Neuron initNewNeuron (NeuronGroup ngroup, 
                                    Neuron firstn, 
                                    Neuron secondn) {
        final int ndims = firstn.getDimension();
        double [] newWeight = new double [ndims];
        float [] newLContext = new float [ndims];
        float [] flcontext = (float []) firstn.parameters.get(NEURON_LOCAL_CONTEXT);
        float [] slcontext = (float []) secondn.parameters.get(NEURON_LOCAL_CONTEXT);
        float delta = (Float) ngroup.parameters.get(MGNG_DELTA);
        for (int index = 0; index < newWeight.length; index++) {
            newWeight [index] = (firstn.getEntry(index) + secondn.getEntry(index)) / 2;
            newLContext [index] = (flcontext [index] + slcontext [index]) / 2;
        }
        Neuron newNeuron = initializeMGNGNeuron (ngroup, new Neuron(newWeight));
        newNeuron.parameters.put(NEURON_LOCAL_CONTEXT, newLContext);
        float errorHighest = (Float) firstn.parameters.get(NEURON_LOCALERROR);
        float errorMax = (Float) secondn.parameters.get(NEURON_LOCALERROR);
        newNeuron.parameters.put(NEURON_LOCALERROR, (errorHighest + errorMax) * delta);
        return newNeuron;
    }
    
    @Override
    protected NNResult prepareResult(Neuron neuron, Features signal, List<Neuron> winners, NNResult resc) {
        super.prepareResult(neuron, signal, winners, resc);
        resc.parameters.put(MGNGTrainingSupervisor.MGNG_ALPHA, neuron.parameters.get(MGNGTrainingSupervisor.MGNG_ALPHA));
        resc.parameters.put(MGNGTrainingSupervisor.MGNG_BETA, neuron.parameters.get(MGNGTrainingSupervisor.MGNG_BETA));
        resc.parameters.put(MGNGTrainingSupervisor.MGNG_DELTA, neuron.parameters.get(MGNGTrainingSupervisor.MGNG_DELTA));
        resc.parameters.put(MGNGTrainingSupervisor.MGNG_GLOBAL_CONTEXT, neuron.parameters.get(MGNGTrainingSupervisor.MGNG_GLOBAL_CONTEXT));
        resc.parameters.put(MGNGTrainingSupervisor.MGNG_NI, neuron.parameters.get(MGNGTrainingSupervisor.MGNG_NI));
        resc.parameters.put(MGNGTrainingSupervisor.MGNG_THETA, neuron.parameters.get(MGNGTrainingSupervisor.MGNG_THETA));
        resc.parameters.put(MGNGTrainingSupervisor.NEURON_LOCAL_CONTEXT, neuron.parameters.get(MGNGTrainingSupervisor.NEURON_LOCAL_CONTEXT));
        return resc;
    }
    
    public class GlobalContextUpdateAnalyzer implements NeuronAnalyzer {

        @Override
        public void analysis(String state, Neuron n, ParametersContainer param, Features current, List<NNResult> result) {
            if (state.contentEquals(getName())) {
                Neuron nWinner = result.get(result.size() - 1).winners.get(0);
                param.processOnParameter(MGNG_GLOBAL_CONTEXT, new GlobalContextUpdate((NeuronGroup) n, nWinner));
            }
        }
    }
    
    protected class GlobalContextUpdate implements ProcessOnParameter {
        Neuron nWinner;
        NeuronGroup parent;
        public GlobalContextUpdate (NeuronGroup ng, Neuron winner) {
            nWinner = Objects.requireNonNull(winner);
            parent = Objects.requireNonNull(ng);
        }

        @Override
        public Object process(Object data, ParametersContainer c) {
            float [] lc = (float []) nWinner.parameters.get(NEURON_LOCAL_CONTEXT);
            float beta = (Float) parent.parameters.get(MGNG_BETA);
            
            final int ndims = nWinner.getDimension();
            //float [] wd = (float []) nWinner.centroid.cdata(ch);
            float [] gc = new float [ndims];
            for (int dim = 0; dim < ndims; dim++) {
                gc [dim] = (float) (((1 - beta) * nWinner.getEntry(dim)) + (beta * lc [dim]));
            }
            return gc;
        }
    }
}