
package com.phm.comp.neuralnet.connection;
 
import com.phm.comp.neuralnet.Neuron;
import com.phm.core.ArraySet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set; 

/**
 *
 * @author Parham Nooralishahi - PHM!
 */
public class ConnectionsContainer extends HashSet<Connection> {
    
    protected ArraySet<Neuron> neurons = new ArraySet<>();
    
    @Override
    public boolean add (Connection c) {
        if (c != null && !this.contains(c)) {
            super.add(c);
            return true;
        }
        return false;
    }
    public boolean add (Neuron n1, Neuron n2, float edge) {
        return add (new Connection(n1, n2, edge));
    }
    public boolean add (Neuron n1, Neuron n2) {
        return add (n1, n2, 0);
    }
    public boolean remove (Neuron n1, Neuron n2) {
        return remove (new Connection(n1, n2));
    }
    public boolean contains (Neuron n1, Neuron n2) {
        return contains (new Connection(n1, n2));
    }
    public List<Neuron> neighbors (Neuron n) {
        LinkedList<Neuron> neighbors = new LinkedList<>();
        this.stream().map((x) -> x.connectTo(n)).filter((temp) -> (temp != null)).forEach((temp) -> {
            neighbors.add(temp);
        });
        
        return neighbors;
    }
    public float setConnectionEdgeValue (Connection c, float edge) {
        float preValue;
        for (Connection x : this) {
            if (c.equals(x)) {
                preValue = (float) c.getParameter(Connection.CONNECTION_VALUE);
                c.setParameter(Connection.CONNECTION_VALUE, edge);
                return preValue;
            }
        }
        return -1;
    }
    public float incrementConnectionEdgeValue (Connection c) {
        for (Connection x : this) {
            if (c.equals(x)) {
                c.parameters.processOnParameter(Connection.CONNECTION_VALUE, new Connection.IncrementEdgeValue());
                return (float) c.parameters.get(Connection.CONNECTION_VALUE);
            }
        }
        return -1;
    }
    public void incerementConnectionsEdgeValue () {
        this.stream().parallel().forEach((Connection x) -> { 
            incrementConnectionEdgeValue(x);
        });
    }
    public void removeConnectionWithValue (float value) {
        LinkedList<Connection> del = new LinkedList<>();
        this.stream().forEach((Connection x) -> {
            float v = (float) x.getParameter(Connection.CONNECTION_VALUE);
            if (v == value) {
                del.add(x);
            }
        });
        del.stream().forEach((x) -> {
            remove(x);
        });
    }
    public void removeConnectionWithHigherValue (float value) {
        LinkedList<Connection> del = new LinkedList<>();
        this.stream().forEach((Connection x) -> {
            float v = (float) x.getParameter(Connection.CONNECTION_VALUE);
            if (v >= value) {
                del.add(x);
            }
        });
        del.stream().forEach((x) -> {
            remove(x);
        });
    }
    public Set<Connection> getAllConnections (Neuron source, Neuron target) {
        Set<Connection> s = new ArraySet<>();
        Connection c = new Connection(source, target);
        for (Connection x : this) {
            if (c.equals(x)) {
                s.add(x);
            }
        }
        return s;
    }
    public Connection getConnection (Neuron source, Neuron target) {
        Connection c = new Connection(source, target);
        for (Connection x : this) {
            if (c.equals(x)) {
                return x;
            }
        }
        return null;
    }
}
