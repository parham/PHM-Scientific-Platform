
package com.phm.comp.neuralnet;

import com.google.common.eventbus.EventBus;
import com.phm.comp.neuralnet.connection.Connection;
import com.phm.comp.neuralnet.connection.ConnectionFactory;
import com.phm.comp.neuralnet.connection.ConnectionsContainer;
import com.phm.comp.neuralnet.event.ConnectionAddedEvent;
import com.phm.comp.neuralnet.event.ConnectionRemovedEvent;
import com.phm.comp.neuralnet.event.NGStopedTrainingEvent;
import com.phm.comp.neuralnet.event.NGUpdatedEvent;
import com.phm.comp.neuralnet.event.NeuronAddedEvent;
import com.phm.comp.neuralnet.event.NeuronRemovedEvent;
import com.phm.comp.neuralnet.restric.RestrictionsContainer;
import com.phm.core.ArraySet;
import com.phm.core.ParametersContainer;
import com.phm.core.data.Features;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.commons.math3.linear.RealVector;
import org.jgrapht.EdgeFactory;
import org.jgrapht.Graph;

/**
 *
 * @author PARHAM
 */
public class NeuronGroup extends Neuron implements Graph<Neuron, Connection> {
    
    public final static String NUM_NEURONS = "num.neurons";
    public final static String NET_NEURONS = "net.neurons";
    public final static String NEURON_REDIRECT_SIGNAL_FLAG = "neuron.redirect.signal.flag";
    
    protected static final ConnectionFactory conFactory = new ConnectionFactory();
    public final NeuronsContainer neurons = new NeuronsContainer ();
    public final RestrictionsContainer restrictions = new RestrictionsContainer();
    public final EventBus eventBus = new EventBus ();
    protected final ConcurrentLinkedQueue<Features> signalBuffer = new ConcurrentLinkedQueue<>();
    
    public NeuronGroup () {
        super ();
    }
    public NeuronGroup (double [] ft) {
        super (ft);
    }
    public NeuronGroup (int ndim) {
        super (ndim);
    }
    public NeuronGroup (RealVector v) {
        super (v); 
    }
    public NeuronGroup (long time, Object lbl) {
        super (time, lbl); 
    }
    public NeuronGroup (long time, Object lbl, double [] ft) {
        super (time, lbl, ft); 
    }
    public NeuronGroup (long time, Object lbl, int ndim) {
        super (time, lbl, ndim); 
    }
    public NeuronGroup (long time, Object lbl, RealVector v) {
        super (time, lbl, v); 
    }
    public NeuronGroup (Map<String, Object> param, Set<? extends Connection> cc) {
        super (param, cc);
    }
    public NeuronGroup (Map<String, Object> param, Set<? extends Connection> cc, double [] ft) {
        super (param, cc, ft);
    }
    public NeuronGroup (Map<String, Object> param, Set<? extends Connection> cc, int ndim) {
        super (param, cc, ndim);
    }
    public NeuronGroup (Map<String, Object> param, Set<? extends Connection> cc, RealVector v) {
        super (param, cc, v); 
    }
    public NeuronGroup (Map<String, Object> param, Set<? extends Connection> cc, long time, Object lbl) {
        super (param, cc, time, lbl); 
    }
    public NeuronGroup (Map<String, Object> param, Set<? extends Connection> cc, long time, Object lbl, double [] ft) {
        super (param, cc, time, lbl, ft);
    }
    public NeuronGroup (Map<String, Object> param, Set<? extends Connection> cc, long time, Object lbl, int ndim) {
        super (param, cc, time, lbl, ndim);
    }
    public NeuronGroup (Map<String, Object> param, Set<? extends Connection> cc, long time, Object lbl, RealVector v) {
        super (param, cc, time, lbl, v);
    }
    protected NeuronGroup (int tid, Map<String, Object> param, Set<? extends Connection> cc, long time, Object lbl, double [] ft) {
        super (tid, param, cc, time, lbl, ft);
    }
    protected NeuronGroup (int tid, Map<String, Object> param, Set<? extends Connection> cc, long time, Object lbl, int ndim) {
        super (tid, param, cc, time, lbl, ndim);
    }
    protected NeuronGroup (int tid, Map<String, Object> param, Set<? extends Connection> cc, long time, Object lbl, RealVector v) {
        super (tid, param, cc, time, lbl, v);
    }
    
    @Override
    public void initialize () {
        super.initialize();
        analyzers.add(new NeuronsAnalayzer(this));
        parameters.put(NEURON_REDIRECT_SIGNAL_FLAG, true);
    }
    @Override
    public boolean feed (String state, final Features s, List<NNResult> result) {
        boolean status = false;
        
        signalBuffer.add(s);
        while (signalBuffer.size() > 0) {
            Features signal = signalBuffer.poll ();
            RealVector vec = inputStrategy.filter(signal);
            Features temp = new Features (signal.getTime(), signal.getLabel(), vec);
            // Check Restrictions and Apply Supervisor algorithms
            if (supervisors.supervise (state, this, temp, result)) {
                analyzers.analysis (state, this, parameters, temp, result);
                // Release update event
                eventBus.post(new NGUpdatedEvent (this));
                boolean redirect = (boolean) parameters.get(NEURON_REDIRECT_SIGNAL_FLAG);
                if (redirect) {
                    NNResult tmp = result.get(result.size() - 1);
                    tmp.winners.stream().forEach((Neuron n) -> {
                        List<NNResult> rtmp = new LinkedList<>();
                        n.feed(state, signal, rtmp);
                    });
                }
                status = true;
            } else {
                status = false;
                break;
            }
        }
        eventBus.post(new NGStopedTrainingEvent(this));
        return status;
    }
    public void addSignalInternally (Features signal) {
        signalBuffer.add(signal);
    }
    public boolean addInternalNeuron (Neuron neuron) {
        if (neuron != null && neurons.add(neuron)) {
            parameters.processOnParameter(NeuronGroup.NUM_NEURONS, INCREASE_QUANTITY);
            eventBus.post (new NeuronAddedEvent(neuron));
            return true;
        }
        return false;
    }
    public boolean removeInternalNeuron (Neuron neuron) {
        if (neuron != null && neurons.remove(neuron)) {
            parameters.processOnParameter(NUM_NEURONS, DECREASE_QUANTITY);
            eventBus.post (new NeuronRemovedEvent(neuron));
            return true;
        }
        return false;
    }
    public void clearInternalNeurons () {
        parameters.put (NUM_NEURONS, 0);
        neurons.clear();
    }
    public int countInternalNeurons () {
        return neurons.size();
    }
    public boolean updateInternalConnection (Connection c) {
        if (c == null) return false;
        if (c.neuronOne.connections.add(c) &&
            c.neuronTwo.connections.add(c)) {
            parameters.processOnParameter(NUM_CONNECTIONS, INCREASE_QUANTITY);
            eventBus.post (new ConnectionAddedEvent(c));
        } else {
            // Remove the previous connection
            c.neuronOne.connections.remove(c);
            c.neuronTwo.connections.remove(c);
            eventBus.post (new ConnectionRemovedEvent(c));
            // Add the updated connection
            c.neuronOne.connections.add(c);
            c.neuronTwo.connections.add(c);
            eventBus.post (new ConnectionAddedEvent(c));
        }
        return true;
    }
    public boolean removeInternalConnection (Connection c) {
        if (c == null) {
            return false;
        }
        if (c.neuronOne.connections.remove(c) &&
            c.neuronTwo.connections.remove(c)) {
            parameters.processOnParameter(NUM_CONNECTIONS, DECREASE_QUANTITY);
            eventBus.post (new ConnectionRemovedEvent(c));
            return true;
        }
        return false;
    }
    public ConnectionsContainer getConnections () {
        ConnectionsContainer cc = new ConnectionsContainer();
        this.neurons.stream().parallel().forEach((Neuron x) -> {
            cc.addAll(x.connections);
        });
        return cc;
    }
    @Override
    public Set<Connection> getAllEdges(Neuron v, Neuron v1) {
        ArraySet<Connection> edges = new ArraySet<>();
        edges.addAll(v.connections.getAllConnections(v, v1));
        edges.addAll(v1.connections.getAllConnections(v, v1));
        return edges;
    }
    @Override
    public Connection getEdge(Neuron v, Neuron v1) {
        Connection c = v.connections.getConnection (v, v1);
        if (c != null) {
            return c;
        }
        c = v1.connections.getConnection(v, v1);
        if (c != null) {
            return c;
        }
        return null;
    }
    @Override
    public EdgeFactory<Neuron, Connection> getEdgeFactory() {
        return conFactory;
    }
    @Override
    public Connection addEdge(Neuron v, Neuron v1) {
        Connection prev = getEdge(v, v1);
        updateInternalConnection(conFactory.createEdge (v, v1));
        return prev;
    }
    @Override
    public boolean addEdge(Neuron v, Neuron v1, Connection e) {
        addEdge(v, v1);
        return true;
    }
    @Override
    public boolean addVertex(Neuron v) {
        return addInternalNeuron(v);
    }
    @Override
    public boolean containsEdge(Neuron v, Neuron v1) {
        Connection c = conFactory.createEdge(v, v1);
        return containsEdge(c);
    }
    @Override
    public boolean containsEdge(Connection c) {
        if (c.neuronOne.connections.contains(c) &&
            c.neuronTwo.connections.contains(c)) {
            return true;
        }
        return false;        
    }
    @Override
    public boolean containsVertex(Neuron v) {
        return this.neurons.contains(v);
    }
    @Override
    public Set<Connection> edgeSet() {
        return getConnections();
    }
    @Override
    public Set<Connection> edgesOf(Neuron v) {
        return v.connections;
    }
    @Override
    public boolean removeAllEdges(Collection<? extends Connection> cons) {
        for (Connection x : cons) {
            removeEdge(x);
        }
        return true;
    }
    @Override
    public Set<Connection> removeAllEdges (Neuron v, Neuron v1) {
        ArraySet<Connection> cons = new ArraySet<>();
        cons.addAll (v.connections.getAllConnections(v, v1));
        cons.addAll (v1.connections.getAllConnections(v, v1));
        Connection c = conFactory.createEdge(v, v1);
        for (Connection x : cons) {
            removeEdge(c);
        }
        return cons;
    }
    public Set<Connection> removeAllEdges (Connection c) {
        return removeAllEdges(c.neuronOne, c.neuronTwo);
    }
    @Override
    public boolean removeAllVertices(Collection<? extends Neuron> cs) {
        for (Neuron n : cs) {
            removeVertex(n);
        }
        return true;
    }
    @Override
    public Connection removeEdge(Neuron v, Neuron v1) {
        Connection c = conFactory.createEdge(v, v1);
        removeInternalConnection(c);
        return c;
    }
    @Override
    public boolean removeEdge(Connection c) {
        return removeInternalConnection(c);
    }
    @Override
    public boolean removeVertex(Neuron v) {
        return removeInternalNeuron(v);
    }
    @Override
    public Set<Neuron> vertexSet() {
        return neurons;
    }
    @Override
    public Neuron getEdgeSource(Connection e) {
        return e.neuronOne;
    }
    @Override
    public Neuron getEdgeTarget(Connection e) {
        return e.neuronTwo;
    }
    @Override
    public double getEdgeWeight(Connection e) {
        float v = (float) e.parameters.get(Connection.CONNECTION_VALUE);
        return v;
    }
    public boolean initialize (Graph<Neuron, Connection> graph) {
        // Add Verteces
        Set<Neuron> ns = graph.vertexSet();
        for (Neuron x : ns) {
            this.addInternalNeuron (x);
        }
        // Add Connections
        Set<Connection> cs = graph.edgeSet();
        for (Connection x : cs) {
            this.updateInternalConnection(x);
        }
        return true;
    }

    public static class NeuronsAnalayzer implements NeuronAnalyzer {
        public NeuronsAnalayzer (NeuronGroup n) {
            n.parameters.put(NUM_NEURONS, n.neurons.size());
            n.parameters.put(NET_NEURONS, n.neurons);
        }
        @Override
        public void analysis(String state, Neuron n, ParametersContainer param, Features current, List<NNResult> result) {
            NeuronGroup ng = (NeuronGroup) n;
            ng.parameters.put(NUM_NEURONS, ng.neurons.size());
            ng.parameters.put(NET_NEURONS, ng.neurons);
        }
    }
}
