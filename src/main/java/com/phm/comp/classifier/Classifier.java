
package com.phm.comp.classifier;
 
import com.phm.core.data.Instance;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author phm
 * @param <DataType>
 */
public interface Classifier<DataType extends Instance> {
    public Object classify (DataType entity);
    public ClassProbabilityDistribution classDistribution (DataType entity);
    
    public static class ClassProbabilityDistribution extends HashMap<Object, Double> {

        public ClassProbabilityDistribution (Map<? extends Object, ? extends Double> m) {
            super(m);
        }
        public ClassProbabilityDistribution () {
            super();
        }
        
        public double getProbability (Object clss) {
            return getOrDefault(clss, 0.0);
        }
        public Object getClassWithHighestProbability () {
            LinkedList<Object> keys = new LinkedList<>(this.keySet());
            Object maxl = keys.get(0);
            double maxv = getProbability(maxl);
            for (Object k : keys) {
                double tmp = getProbability(k);
                if (tmp > maxv) {
                    maxl = k;
                    maxv = tmp;
                }
            }
            return maxl;
        }
        public double getHighestProbability () {
            Object lbl = getClassWithHighestProbability();
            return getProbability (lbl);
        }
        public double getLowestProbability () {
            Object lbl = getClassWithLowestProbability();
            return getProbability (lbl);
        }
        public Object getClassWithLowestProbability () {
            LinkedList<Object> keys = new LinkedList<>(this.keySet());
            Object minl = keys.get(0);
            double minv = getProbability(minl);
            for (Object k : keys) {
                double tmp = getProbability(k);
                if (tmp < minv) {
                    minl = k;
                    minv = tmp;
                }
            }
            return minl;
        }
    }
}
