package com.phm.comp.neuralnet.gng;

import com.phm.comp.distance.ApacheWrapperDistanceMeasure;
import com.phm.comp.distance.DefaultDistanceMeasure;
import com.phm.comp.distance.DistanceInfo;
import com.phm.comp.neuralnet.DefaultNeuronComparePolicy;
import com.phm.comp.neuralnet.NNResult;
import com.phm.comp.neuralnet.Neuron;
import com.phm.comp.neuralnet.NeuronDistanceMethod;
import com.phm.comp.neuralnet.NeuronGroup;
import com.phm.comp.neuralnet.Supervisor;
import com.phm.comp.neuralnet.connection.Connection;
import com.phm.core.ParametersContainer;
import com.phm.core.ProcessOnParameter;
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
public class GNGTrainingSupervisor extends Supervisor {

    public static final String NEURON_DISTANCE = "neuron.distance";
    public static final String NEURON_DIS_DIMENSION = "neuron.local.distance";
    public static final String NEURON_LOCALERROR = "neuron.local.error";

    public static final String GNG_EPSILON_B_KEY = "epsilon.b";
    public static final String GNG_EPSILON_N_KEY = "epsilon.n";
    // needed age for an edge to be removed
    public static final String GNG_MAX_EDGE_AGE_KEY = "max.edge.age";
    // number signal generations needed to insert new node
    public static final String GNG_LANDA_KEY = "landa.key";
    public static final String GNG_ERRORRATE_A_KEY = "error.a";
    public static final String GNG_ERRORRATE_B_KEY = "error.b";

    public static final DefaultNeuronComparePolicy COMPARE_POLICY = new DefaultNeuronComparePolicy();

    protected final NeuronDistanceMethod distanceMethod;

    public GNGTrainingSupervisor(NeuronDistanceMethod dm) {
        distanceMethod = Objects.requireNonNull(dm);
        //netDimension = dim;
    }
    public GNGTrainingSupervisor () {
        this (new NeuronDistanceMethod (new ApacheWrapperDistanceMeasure(new EuclideanDistance())));
    }

    @Override
    public void initialize(Neuron ngroup) {
        ngroup.parameters.put(GNG_EPSILON_B_KEY, 0.05f);
        ngroup.parameters.put(GNG_EPSILON_N_KEY, 0.0006f);
        ngroup.parameters.put(GNG_MAX_EDGE_AGE_KEY, 88);
        ngroup.parameters.put(GNG_LANDA_KEY, 400);
        ngroup.parameters.put(GNG_ERRORRATE_A_KEY, 0.5f);
        ngroup.parameters.put(GNG_ERRORRATE_B_KEY, 0.0005f);
    }

    public NeuronDistanceMethod getDistanceMethod() {
        return distanceMethod;
    }

    protected Neuron initializeGNGNeuron (NeuronGroup ngroup, Neuron neuron) {
        neuron.parameters.put(NEURON_DISTANCE, 0.0f);
        neuron.parameters.put(NEURON_DIS_DIMENSION, new Features(new double [neuron.getDimension()]));
        neuron.parameters.put(NEURON_LOCALERROR, 0.0f);
        return neuron;
    }

    // Initial Phase Stages ////////////////////////////////
    protected void beforeInitialPhase(NeuronGroup ngroup, Features signal) {
        // Empty body
    }

    protected Neuron onInitialPhase(NeuronGroup ngroup, Features buffer) {
        Neuron nn = initializeGNGNeuron(ngroup, new Neuron (buffer));
        ngroup.addInternalNeuron(nn);
        return nn;
    }
    protected void afterInitialPhase(NeuronGroup ngroup, Features signal, Neuron nn) {
        // Empty body
    }
    // Distance Calculation Stages //////////////////////////
    protected void onDistanceCalculation (NeuronGroup ngroup, Features signal) {
        ngroup.neurons.stream().forEach((Neuron x) -> {
            DistanceInfo dd = distanceMethod.distance(ngroup, x, signal);
            x.parameters.put(NEURON_DISTANCE, (float) dd.distance);
            x.parameters.put(NEURON_DIS_DIMENSION, dd.distancedim);
            //x.parameters.put(NEURON_DIS_CHANNELS, dd.channeldim);
        });
    }
    protected void afterDistanceCalculation (NeuronGroup ngroup, Features signal) {
        // Empty body
    }
    // Find Winner Stages //////////////////////////////
    protected List<Neuron> onWinnersFinding (NeuronGroup ngroup, Features signal) {
        LinkedList<Neuron> ns = new LinkedList<>(ngroup.neurons);
//        Collections.sort(ns, COMPARE_POLICY);
        LinkedList<Neuron> result = new LinkedList<>();
//        result.add(ns.get(0));
//        result.add(ns.get(1));
        Neuron s0 = Collections.min(ns, COMPARE_POLICY);
        ns.remove(s0);
        Neuron s1 = Collections.min(ns, COMPARE_POLICY);
        result.add(s0);
        result.add(s1);
        return result;
    }
    protected void afterWinnersFinding (NeuronGroup ngroup, 
                                        List<Neuron> winners, 
                                        Features signal) {
        // Empty body
    }
    // Update Winners stage /////////////////////////
    protected void onWinnersUpdate (NeuronGroup ngroup, 
                                    List<Neuron> winners, 
                                    Features signal) {
        float epsilonB = (Float) ngroup.parameters.get(GNG_EPSILON_B_KEY);
        float epsilonN = (Float) ngroup.parameters.get(GNG_EPSILON_N_KEY);
        // Create Connection between two nearest neurons
        Connection c = new Connection(winners.get(0), winners.get(1), 0);
        ngroup.updateInternalConnection(c);
        ////////////////////////////////////////////////////////
        // Add squared error of winner unit to a local error
        winners.get(0).parameters.processOnParameter(NEURON_LOCALERROR, new CalculateWinnerError());
        ////////////////////////////////////////////////////////
        // Update neuron weights
        final int ndims = ngroup.getDimension();
        winners.get(0).connections.neighbors().stream().parallel().forEach((Neuron x) -> {
            Features dimDist = (Features) x.parameters.get(NEURON_DIS_DIMENSION);
            for (int dim = 0; dim < ndims; dim++) {
                x.setEntry(dim, x.getEntry(dim) + (dimDist.getEntry(dim) * epsilonN));
            }
        });
        // Update winner neuron's weight
        Neuron winner = winners.get(0);
        Features dimDist = (Features) winner.parameters.get(NEURON_DIS_DIMENSION);
        final int ndim = winner.getDimension();
        for (int dim = 0; dim < ndim; dim++) {
            winner.setEntry(dim, winner.getEntry(dim) + (dimDist.getEntry(dim) * epsilonB));
        }
        // Increase winner neuron's connection's age
        winner.connections.incerementConnectionsEdgeValue();
    }
    protected void afterWinnersUpdate (NeuronGroup ngroup, List<Neuron> winners, Features signal) {
        // Empty body
    }
    // Update GNG /////////////////////////////////////
    protected void onGNGUpdate (NeuronGroup ngroup, List<Neuron> winners, Features signal) {
        ////////////////////////////////////////////////////////
        // Remove neuron's connections which their ages are more than maximum
        removeConnectionsWithHighAge(ngroup);
        ////////////////////////////////////////////////////////
        // Remove neurons with no connections
        removeNeuronWithoutConnection(ngroup);
        ////////////////////////////////////////////////////////
    }
    protected boolean neuronInsertionConditions (NeuronGroup ngroup, Features signal) {
        int sindex = (Integer) ngroup.parameters.get(NeuronGroup.RECIEVED_SIGNALS_NUM);
        int landa = (Integer) ngroup.parameters.get(GNG_LANDA_KEY);
        return (sindex + 1) % landa == 0;
    }
    protected void onNeuronInsertion (NeuronGroup ngroup, Features signal) {
        float aErrorRate = (Float) ngroup.parameters.get(GNG_ERRORRATE_A_KEY);
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
        Neuron newNeuron = initNewNeuron(ngroup, highestLE, hMaxLENEighbor);
        ngroup.addInternalNeuron(newNeuron);
        // Remove Edges between the selected neurons
        ngroup.removeInternalConnection(new Connection(highestLE, hMaxLENEighbor));
        // Add edge between new node and f and q
        ngroup.updateInternalConnection(new Connection(newNeuron, highestLE, 0));
        ngroup.updateInternalConnection(new Connection(newNeuron, hMaxLENEighbor, 0));
        // Prepare error vector for new node
        // Decrease Error value of neuron f and q
        highestLE.parameters.processOnParameter(NEURON_LOCALERROR, new UpdateErrorNeuron(aErrorRate));
        hMaxLENEighbor.parameters.processOnParameter(NEURON_LOCALERROR, new UpdateErrorNeuron(aErrorRate));
    }
    protected void afterNeuronInsertion (NeuronGroup ngroup, Features signal)  {
        // Empty body
    }
    
    protected void onGNGLastStep (NeuronGroup ngroup, Features signal) {
        // Decrease error of all units
        float bErrorRate = (Float) ngroup.parameters.get(GNG_ERRORRATE_B_KEY);
        ngroup.neurons.stream().parallel().forEach((Neuron x) -> {
            x.parameters.processOnParameter(NEURON_LOCALERROR, new UpdateErrorNeuron(bErrorRate));
        });
    }
    protected void onGNGFinalization (NeuronGroup ngroup, Features signal, List<Neuron> result) {
        // Empty body
    }

    @Override
    protected boolean superviseOperator (Neuron neuron, Features signal, List<Neuron> result) {
        NeuronGroup ngroup = (NeuronGroup) neuron;
        if (ngroup.neurons.size() < 2) {
            // Before Initial Phase
            beforeInitialPhase(ngroup, signal);
            // On Initial Phase
            Neuron nn = onInitialPhase(ngroup, signal);
            result.add(nn);
            // After Initial Phase
            afterInitialPhase(ngroup, signal, nn);
        } else {
            // On Distance Calculation phase
            onDistanceCalculation (ngroup, signal);
            // After Distance Calculation phase
            afterDistanceCalculation(ngroup, signal);
            // Find out two best neurons
            List<Neuron> winners = onWinnersFinding(ngroup, signal);
            // After Winners finding stage
            afterWinnersFinding(ngroup, winners, signal);
            ////////////////////////////////////////////////////////
            onWinnersUpdate(ngroup, winners, signal);
            // After Winners update stage
            afterWinnersUpdate(ngroup, winners, signal);
            // GNG update
            onGNGUpdate(ngroup, winners, signal);
            // Insert new neuron
            if (neuronInsertionConditions(ngroup, signal)) {
                onNeuronInsertion(ngroup, signal);
                afterNeuronInsertion(ngroup, signal);
            }
            onGNGLastStep(ngroup, signal);
            result.addAll(winners);
        }
        
        onGNGFinalization(ngroup, signal, result);
        return true;
    }

    @Override
    public String getName() {
        return "gng.train";
    }

    protected Neuron initNewNeuron(NeuronGroup ngroup, 
                                   Neuron firstn, 
                                   Neuron secondn) {
        double [] newWeight = new double [
                Math.min(firstn.getDimension(), secondn.getDimension())];
        for (int index = 0; index < newWeight.length; index++) {
            newWeight[index] = (firstn.getEntry(index) + 
                                secondn.getEntry(index)) / 2;
        }
        Neuron newNeuron = initializeGNGNeuron(ngroup, new Neuron(newWeight));
        float errorHighest = (Float) firstn.parameters.get(NEURON_LOCALERROR);
        float errorMax = (Float) secondn.parameters.get(NEURON_LOCALERROR);
        newNeuron.parameters.put(NEURON_LOCALERROR, (errorHighest + errorMax) / 2);
        return newNeuron;
    }

    protected void removeConnectionsWithHighAge(NeuronGroup ngroup) {
        int maxAge = (Integer) ngroup.parameters.get(GNG_MAX_EDGE_AGE_KEY);
        ngroup.neurons.stream().parallel().forEach((Neuron x) -> {
            x.connections.removeConnectionWithHigherValue(maxAge);
        });
    }
    protected void removeNeuronWithoutConnection(NeuronGroup ngroup) {
        LinkedList<Neuron> ns = new LinkedList<>(ngroup.neurons);
        ns.stream().filter((Neuron x) -> x.connections.size() <= 0).forEach((Neuron n) -> {
            ngroup.neurons.remove(n);
        });
    }

    @Override
    protected NNResult prepareResult(Neuron neuron, Features signal, List<Neuron> winners, NNResult resc) {
        resc.parameters.put (Neuron.SYSTEM_STATUS, neuron.parameters.get(Neuron.SYSTEM_STATUS));
        resc.parameters.put (Neuron.NEURON_ID, neuron.parameters.get(Neuron.NEURON_ID));
        resc.parameters.put (Neuron.NEURON_CENTROID, neuron.parameters.get(Neuron.NEURON_CENTROID));
        resc.parameters.put (Neuron.NEURON_DIMENSION, neuron.parameters.get(Neuron.NEURON_DIMENSION));
        resc.parameters.put (Neuron.NEURON_NUM_CHANNEL, neuron.parameters.get(Neuron.NEURON_NUM_CHANNEL));
        resc.parameters.put (Neuron.NUM_CONNECTIONS, neuron.parameters.get(Neuron.NUM_CONNECTIONS));
        resc.parameters.put (Neuron.RECIEVED_SIGNALS_NUM, neuron.parameters.get(Neuron.RECIEVED_SIGNALS_NUM));
        resc.parameters.put (NeuronGroup.NUM_NEURONS, neuron.parameters.get(NeuronGroup.NUM_NEURONS));
        resc.parameters.put (GNGTrainingSupervisor.GNG_EPSILON_B_KEY, neuron.parameters.get(GNGTrainingSupervisor.GNG_EPSILON_B_KEY));
        resc.parameters.put (GNGTrainingSupervisor.GNG_EPSILON_N_KEY, neuron.parameters.get(GNGTrainingSupervisor.GNG_EPSILON_N_KEY));
        resc.parameters.put (GNGTrainingSupervisor.GNG_ERRORRATE_A_KEY, neuron.parameters.get(GNGTrainingSupervisor.GNG_ERRORRATE_A_KEY));
        resc.parameters.put (GNGTrainingSupervisor.GNG_ERRORRATE_B_KEY, neuron.parameters.get(GNGTrainingSupervisor.GNG_ERRORRATE_B_KEY));
        resc.parameters.put (GNGTrainingSupervisor.GNG_LANDA_KEY, neuron.parameters.get(GNGTrainingSupervisor.GNG_LANDA_KEY));
        resc.parameters.put (GNGTrainingSupervisor.GNG_MAX_EDGE_AGE_KEY, neuron.parameters.get(GNGTrainingSupervisor.GNG_MAX_EDGE_AGE_KEY));
        resc.parameters.put (GNGTrainingSupervisor.NEURON_DISTANCE, neuron.parameters.get(GNGTrainingSupervisor.NEURON_DISTANCE));
        resc.parameters.put (GNGTrainingSupervisor.NEURON_DIS_DIMENSION, neuron.parameters.get(GNGTrainingSupervisor.NEURON_DIS_DIMENSION));
        resc.parameters.put (GNGTrainingSupervisor.NEURON_LOCALERROR, neuron.parameters.get(GNGTrainingSupervisor.NEURON_LOCALERROR));
        return resc;
    }

    public class CalculateWinnerError implements ProcessOnParameter {

        @Override
        public Object process(Object data, ParametersContainer c) {
            float dis = (Float) c.get(NEURON_DISTANCE);
            float lerror = (Float) data;
            return lerror + (dis * dis);
        }
    }

    public class UpdateErrorNeuron implements ProcessOnParameter {

        float factor = 0;

        public UpdateErrorNeuron(float f) {
            factor = f;
        }

        @Override
        public Object process(Object data, ParametersContainer c) {
            float error = (Float) data;
            return error - (error * factor);
        }
    }
}
