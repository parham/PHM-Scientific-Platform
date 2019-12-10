
package com.phm.comp.neuralnet;

import com.phm.core.data.IDFeatures;
import com.phm.comp.filter.BlankFilter;
import com.phm.comp.filter.VectorFilter;
import com.phm.comp.neuralnet.connection.Connection; 
import com.phm.comp.neuralnet.connection.NeuronConnectionsContainer;
import com.phm.core.ParametersContainer;
import com.phm.core.ProcessOnParameter;
import com.phm.core.data.Features;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set; 
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author Parham Nooralishahi - PHM!
 */
public class Neuron extends IDFeatures {
    
    public final static String SYSTEM_STATUS = "system.status";
    
    public final static String NEURON_LABEL = ENTITY_LABEL;
    public final static String NEURON_ID = "neuron.id";
    public final static String NEURON_DIMENSION = "neuron.dimension";
    public final static String NEURON_NUM_CHANNEL = "neuron.channel";
    public final static String NEURON_CENTROID = "neuron.centroid";
    public final static String NEURON_CONNECTIONS = "neuron.connections";
    
    public final static String RECIEVED_SIGNALS_FLAG = "signal.store.flag";
    public final static String RECIEVED_SIGNALS = "recieved.signals";
    public final static String RECIEVED_SIGNALS_NUM = "num.signals.feed";
    
    public final static String NUM_CONNECTIONS_FLAG = "num.connections.flag";
    public final static String NUM_CONNECTIONS = "num.connections";
    
    public static final ProcessOnParameter INCREASE_QUANTITY = new ChangeQuantityByValue(1);
    public static final ProcessOnParameter DECREASE_QUANTITY = new ChangeQuantityByValue(-1);
    
    public final NeuronConnectionsContainer connections = new NeuronConnectionsContainer();
    public final SupervisorsContainer supervisors = new SupervisorsContainer ();
    public final NeuronAnalyzersContianer analyzers = new NeuronAnalyzersContianer();
    protected VectorFilter inputStrategy = new BlankFilter ();
    
    public Neuron () {
        super ();
        initialize();
    }
    public Neuron (double [] ft) {
        super (ft);
        initialize();
    }
    public Neuron (int ndim) {
        super (ndim);
        initialize();
    }
    public Neuron (RealVector v) {
        super (v); 
        initialize();
    }
    public Neuron (long time, Object lbl) {
        super (time, lbl); 
        initialize();
    }
    public Neuron (long time, Object lbl, double [] ft) {
        super (time, lbl, ft); 
        initialize(); 
    }
    public Neuron (long time, Object lbl, int ndim) {
        super (time, lbl, ndim); 
        initialize();
    }
    public Neuron (long time, Object lbl, RealVector v) {
        super (time, lbl, v); 
        initialize();
    }
    public Neuron (Map<String, Object> param, Set<? extends Connection> cc) {
        super ();
        parameters.putAll(param);
        connections.addAll(cc);
        initialize();
    }
    public Neuron (Map<String, Object> param, Set<? extends Connection> cc, double [] ft) {
        super (ft);
        parameters.putAll(param);
        connections.addAll(cc);
        initialize();
    }
    public Neuron (Map<String, Object> param, Set<? extends Connection> cc, int ndim) {
        super (ndim);
        parameters.putAll(param);
        connections.addAll(cc);
        initialize();
    }
    public Neuron (Map<String, Object> param, Set<? extends Connection> cc, RealVector v) {
        super (v); 
        parameters.putAll(param);
        connections.addAll(cc);
        initialize();
    }
    public Neuron (Map<String, Object> param, Set<? extends Connection> cc, long time, Object lbl) {
        super (time, lbl); 
        parameters.putAll(param);
        connections.addAll(cc);
        initialize();
    }
    public Neuron (Map<String, Object> param, Set<? extends Connection> cc, long time, Object lbl, double [] ft) {
        super (time, lbl, ft);
        parameters.putAll(param);
        connections.addAll(cc);
        initialize();
    }
    public Neuron (Map<String, Object> param, Set<? extends Connection> cc, long time, Object lbl, int ndim) {
        super (time, lbl, ndim);
        parameters.putAll(param);
        connections.addAll(cc);
        initialize();
    }
    public Neuron (Map<String, Object> param, Set<? extends Connection> cc, long time, Object lbl, RealVector v) {
        super (time, lbl, v);
        parameters.putAll(param);
        connections.addAll(cc);
        initialize();
    }
    public Neuron (int tid, Map<String, Object> param, Set<? extends Connection> cc, long time, Object lbl, double [] ft) {
        super (tid, time, lbl, ft);
        parameters.putAll(param);
        connections.addAll(cc);
        initialize();
    }
    public Neuron (int tid, Map<String, Object> param, Set<? extends Connection> cc, long time, Object lbl, int ndim) {
        super (tid, time, lbl, ndim);
        parameters.putAll(param);
        connections.addAll(cc);
        initialize();
    }
    public Neuron (int tid, Map<String, Object> param, Set<? extends Connection> cc, long time, Object lbl, RealVector v) {
        super (tid, time, lbl, v);
        parameters.putAll(param);
        connections.addAll(cc);
        initialize();
    }
    
    protected void initialize () {
        // Initialize parameters 
        parameters.put (NEURON_ID, getID ());
        parameters.put (NEURON_NUM_CHANNEL, getDimension ());
        parameters.put (SYSTEM_STATUS, "");
        // Initialize Neuron Analyzer
        analyzers.add(new Neuron.InternalAnalayzer(this));
        analyzers.add(new Neuron.RecievedSignalAnalyzer(this));
        analyzers.add(new Neuron.ConnectionsAnalyzer(this));
    }
    public String setStatus (String status) {
        return (String) parameters.get(SYSTEM_STATUS);
    }    
    public String getStatus () {
        return (String) parameters.get(SYSTEM_STATUS);
    }
    
    public void setInputStrategy (VectorFilter is) {
        inputStrategy = is;
    }
    public VectorFilter getInputStrategy () {
        return inputStrategy;
    }
    public boolean feed (String state, final Features signal, List<NNResult> result) {
        setStatus(state);
        if (result == null) {
            result = new LinkedList<>();
        }
        
        RealVector vec = inputStrategy.filter (signal);
        Features temp = new Features (signal.getTime(), signal.getLabel(), vec);
        if (supervisors.supervise(state, this, temp, result)) {
            analyzers.analysis(state, this, parameters, temp, result);
            return true;
        }
        return false;
    }
    public boolean feed (final Features signal, List<NNResult> result) {
        String state = (String) parameters.get(SYSTEM_STATUS);
        return feed (state, signal, result);
    }
    
    @Override
    public boolean equals (Object obj) {
        return (obj != null && 
                obj instanceof Neuron &&
                obj.hashCode() == hashCode());
    }
    @Override
    public int hashCode() {
        return this.getID();
    }
    @Override
    public Neuron clone () {
        return copy();
    }

    @Override
    public Neuron copy() {
        Neuron tmp = new Neuron (getID(), parameters, connections, getTime(), getLabel(), this);
        tmp.inputStrategy = inputStrategy;
        tmp.supervisors.addAll(supervisors);
        tmp.analyzers.addAll(analyzers);
        return tmp;
    }
    
    public static class ChangeQuantityByValue implements ProcessOnParameter {
        
        int extraValue = 0;
        
        public ChangeQuantityByValue (int ev) {
            extraValue = ev;
        }
        
        @Override
        public Object process(Object data, ParametersContainer c) {
            int temp = (int) data;
            return temp + extraValue;
        }
    }
    
    public static class AddValueTo implements ProcessOnParameter {
        float extraValue = 0;
        
        public AddValueTo (float ev) {
            extraValue = ev;
        }
        
        @Override
        public Object process(Object data, ParametersContainer c) {
            float temp = (float) data;
            return temp + extraValue;
        }
    }
    
    public static class InternalAnalayzer implements NeuronAnalyzer {
        public InternalAnalayzer (Neuron n) {
            n.parameters.put (NEURON_CENTROID, n);
        }
        @Override
        public void analysis(String state, Neuron n, ParametersContainer param, Features current, List<NNResult> result) {
            n.parameters.put (NEURON_CENTROID, n);
        }
    }
    
    public static class ConnectionsAnalyzer implements NeuronAnalyzer {
        public ConnectionsAnalyzer (Neuron n) {
            n.parameters.put(NEURON_CONNECTIONS, n.connections);
            n.parameters.put(NUM_CONNECTIONS, 0);
        }
        
        @Override
        public void analysis(String state, Neuron n, ParametersContainer param, Features current, List<NNResult> result) {
            n.parameters.put(NUM_CONNECTIONS, n.connections.size());
        }
    }
    
    public static class RecievedSignalAnalyzer implements NeuronAnalyzer {
        public RecievedSignalAnalyzer (Neuron n) {
            n.parameters.put (RECIEVED_SIGNALS, new LinkedList<Features>());
            n.parameters.put (RECIEVED_SIGNALS_FLAG, false);
            n.parameters.put (RECIEVED_SIGNALS_NUM, 0);
        }
        @Override
        public void analysis(String state, Neuron n, ParametersContainer param, Features current, List<NNResult> result) {
            param.processOnParameter(Neuron.RECIEVED_SIGNALS_NUM, Neuron.INCREASE_QUANTITY);
            if ((boolean) param.get(Neuron.RECIEVED_SIGNALS_FLAG)) {
                ((List<Features>) param.get(Neuron.RECIEVED_SIGNALS)).add(current);
            }
        }
    }
}
