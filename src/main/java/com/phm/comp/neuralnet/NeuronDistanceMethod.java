
package com.phm.comp.neuralnet;

import com.phm.comp.distance.DistanceInfo;
import com.phm.comp.distance.DistanceMeasure;
import com.phm.core.data.Features;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author PARHAM
 */
public class NeuronDistanceMethod {
    
    public DistanceMeasure distanceMethod;
    
    public NeuronDistanceMethod (DistanceMeasure dm) {
        distanceMethod = Objects.requireNonNull(dm);
    }
    
    public DistanceInfo distance (NeuronGroup parent, 
                                  Neuron neuron, 
                                  Features signal) {
        return (DistanceInfo) distanceMethod.measure (neuron, signal, parent.parameters);
    }
    public DistanceInfo distance (NeuronGroup parent, 
                                  Neuron nOne, 
                                  Neuron nTwo) {
        return (DistanceInfo) distanceMethod.measure (nOne, nTwo, parent.parameters);
    }
    public List<DistanceInfo> distance (NeuronGroup parent, 
                                        List<Neuron> arr, 
                                        Features signal) {
        LinkedList<DistanceInfo> res = new LinkedList<>();
        arr.stream().forEach((arr1) -> {
            res.add(distance(parent, arr1, signal));
        });
        return res;
    }
}