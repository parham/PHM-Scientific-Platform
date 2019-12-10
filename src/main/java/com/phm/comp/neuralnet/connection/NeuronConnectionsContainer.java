
package com.phm.comp.neuralnet.connection;

import com.phm.comp.neuralnet.Neuron;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author PARHAM
 */
public class NeuronConnectionsContainer extends ConnectionsContainer 
                                        implements Cloneable {
    
    public Neuron host;
    
    public NeuronConnectionsContainer (Neuron neuron) {
        host = Objects.requireNonNull(neuron);
    }
    public NeuronConnectionsContainer () {
        // Empty body
    }
    public List<Neuron> neighbors () {
        return neighbors(host);
    }
    @Override
    public boolean add (Connection c) {
        if (c != null && c.include(host)) {
            return super.add(c);
        }
        return false;
    }
    public boolean add (Neuron n2, float edge) {
        return this.add (new Connection(host, n2, edge));
    }
    public boolean add (Neuron n2) {
        return this.add (host, n2, 0);
    }
    public boolean remove (Connection c) {
        return super.remove(c);
    }
    @Override
    public Object clone () {
        NeuronConnectionsContainer c = new NeuronConnectionsContainer(host);
        c.addAll(this);
        return c;
    }
}
