
package com.phm.comp.neuralnet;

import java.util.Comparator;
import java.util.Objects;

/**
 *
 * @author phm
 */
public class NeuronComparePolicy implements Comparator<Neuron> {

    protected String paramKey;
    
    public NeuronComparePolicy (String key) {
        paramKey = Objects.requireNonNull(key);
    }
    
    @Override
    public int compare (Neuron o1, Neuron o2) {
        float dis1 = (Float) o1.parameters.get(paramKey);
        float dis2 = (Float) o2.parameters.get(paramKey);
        float temp = dis1 - dis2;
        if (temp > 0) {
            return 1;
        } else if (temp < 0) {
            return -1;
        } else {
            return 0;
        }
    }
    
}
