
package com.phm.comp.neuralnet.connection;
 
import com.phm.comp.neuralnet.Neuron;
import com.phm.core.ParametersContainer;
import com.phm.core.ProcessOnParameter;
import java.util.Map;
import java.util.Objects;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.Edge;

/**
 *
 * @author Parham Nooralishahi - PHM!
 */
public class Connection implements Cloneable, Edge {
    
    public static final String CONNECTION_VALUE = "connection.value";
    
    public Neuron neuronOne;
    public Neuron neuronTwo;
    public ParametersContainer parameters = new ParametersContainer ();
    protected AttributeMap attributeMap = new AttributeMap();
    //public float value = 0;
    
    public Connection (Neuron one, Neuron two) {
        this (one, two, 0.0f);
    }
    public Connection (Neuron one, Neuron two, float edge) {
        neuronOne = one;
        neuronTwo = two;
        parameters.put(CONNECTION_VALUE, edge);
    }
    public Connection (Neuron one, Neuron two, Map<String, Object> pc) {
        neuronOne = one;
        neuronTwo = two;
        parameters.putAll(pc);
    }
    public boolean include (Neuron neuron) {
        return (neuron != null &&
               (neuron.equals(neuronOne) ||
                neuron.equals(neuronTwo)));
    }
    public Neuron connectTo (Neuron neuron) {
        if (neuron == null) {
            return null;
        }
        if (neuron.equals(neuronOne)) {
            return neuronTwo;
        }
        if (neuron.equals(neuronTwo)) {
            return neuronOne;
        }
        return null;
    }
    
    @Override
    public String toString () {
//        return "[" + this.neuronOne.id + ":" + this.neuronTwo.id + "]";
        return "";
    }
    @Override
    public boolean equals (Object obj) {
        return (obj != null &&
                obj instanceof Connection &&
                obj.hashCode() == this.hashCode());
    }
    @Override
    public int hashCode() {
        int hash = 3;
        int n1 = this.neuronOne.getID();
        int n2 = this.neuronTwo.getID();
        if (n1 > n2) {
            hash = 89 * hash + Objects.hashCode(this.neuronOne);
            hash = 89 * hash + Objects.hashCode(this.neuronTwo);
        } else {
            hash = 89 * hash + Objects.hashCode(this.neuronTwo);
            hash = 89 * hash + Objects.hashCode(this.neuronOne);            
        }
        
        return hash;
    }
    @Override
    public Object clone () {
        float value = (float) getParameter(CONNECTION_VALUE);
        return new Connection(neuronOne, neuronTwo, value);
    }

    @Override
    public Object getSource() {
        return neuronOne;
    }
    @Override
    public Object getTarget() {
        return neuronTwo;
    }
    @Override
    public void setSource(Object o) {
        neuronOne = Objects.requireNonNull ((Neuron) o);
    }
    @Override
    public void setTarget(Object o) {
        neuronTwo = Objects.requireNonNull ((Neuron) o);
    }

    @Override
    public AttributeMap getAttributes() {
//        return new AttributeMap(parameters);
        return attributeMap;
    }
    @Override
    public Map changeAttributes(Map map) {
//        parameters.clear();
//        parameters.putAll(map);
//        return parameters;
        attributeMap.putAll(map);
        return attributeMap;
    }
    @Override
    public void setAttributes(AttributeMap am) {
        changeAttributes(am);
    }
    
    public Object setParameter (String param, Object v) {
        return parameters.put(param, v);
    }
    public Object getParameter (String param) {
        return parameters.get(param);
    }
    
    public static class IncrementEdgeValue implements ProcessOnParameter {
        @Override
        public Object process(Object data, ParametersContainer c) {
            float temp = (float) data;
            return temp + 1;
        }
    }
}
