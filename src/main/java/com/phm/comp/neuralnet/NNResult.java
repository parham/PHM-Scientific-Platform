
package com.phm.comp.neuralnet;
 
import com.phm.core.ParametersContainer;
import com.phm.core.data.Features;
import java.util.LinkedList;
import java.util.List; 
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author phm
 */
public class NNResult extends Features {
//    public FeaturesEntity signal;
    public final ParametersContainer parameters = new ParametersContainer();
    public final LinkedList<Neuron> winners = new LinkedList<>();
    
    public NNResult (long ts, ParametersContainer p, List<Neuron> wins, RealVector signal) {
        super (ts, null, signal);
        parameters.putAll(p);
        winners.addAll(wins);
    }
    public NNResult (long ts, ParametersContainer p, List<Neuron> wins) {
        super (ts, null);
        parameters.putAll(p);
        winners.addAll(wins);
    }
    public NNResult () {
        // Empty body
    }

    @Override
    public NNResult copy() {
        return new NNResult(this.getTime(), parameters, winners);
    }
}
