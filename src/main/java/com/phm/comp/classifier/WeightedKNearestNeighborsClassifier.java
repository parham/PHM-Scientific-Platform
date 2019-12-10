
package com.phm.comp.classifier;
 
import com.phm.comp.distance.DistanceMeasure;
import com.phm.core.data.Features;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Parham Nooralishahi - PHM!
 * @email parham.nooralishahi@gmail.com
 */
public class WeightedKNearestNeighborsClassifier extends KNearestNeighborsClassifier {
    
    public WeightedKNearestNeighborsClassifier (DistanceMeasure dm, int k) {
        super (dm, k);
    }
    public WeightedKNearestNeighborsClassifier (DistanceMeasure dm) {
        super (dm);
    }
    public WeightedKNearestNeighborsClassifier () {
        super ();
    }

    @Override
    public synchronized ClassProbabilityDistribution classDistribution (Features df) {
        HashMap<Features, Double> knearest = new HashMap<>(distance(df));
        HashMap<Features, Double> wvf = new HashMap<>(weightedVotingFunction(df, knearest));
        // Create Classification Distribution
        ClassProbabilityDistribution res = new ClassProbabilityDistribution();
        knearest.keySet().stream().forEach ((Features x) -> {
            double value = res.getOrDefault(x.getLabel(), 0.0);
            double vote = wvf.get(x);
            res.put(x.getLabel(), value + vote);
        });
        return res;
    }
    
    protected Map<Features, Double> weightedVotingFunction (Features f, Map<Features, Double> dis) {
        double sum = 0.0;
        for (double x : dis.values()) {
            sum += x;
        }
        HashMap<Features, Double> res = new HashMap<>(dis);
        for (Features x : dis.keySet()) {
            res.put(x, 1 - (dis.get(x) / sum));
        }
        return res;
    }
}
