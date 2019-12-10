
package com.phm.comp.neuralnet.connection;
 
import com.phm.comp.neuralnet.Neuron;
import com.phm.core.ParametersContainer;
import org.jgrapht.EdgeFactory;

/**
 *
 * @author phm
 */
public class ConnectionFactory implements EdgeFactory<Neuron, Connection> {
    @Override
    public Connection createEdge(Neuron source, Neuron target) {
        return new Connection(source, target);
    }
    public Connection createEdge (Neuron source, Neuron target, ParametersContainer pc) {
        return new Connection (source, target, pc);
    }
}
