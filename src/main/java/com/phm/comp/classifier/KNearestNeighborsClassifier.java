
package com.phm.comp.classifier;

import com.phm.comp.distance.ApacheWrapperDistanceMeasure;
import com.phm.comp.distance.DistanceMeasure;
import com.phm.core.ArraySet;
import com.phm.core.ParametersContainer;
import com.phm.core.data.Features;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects; 
import java.util.Set;

/**
 * <p>
 * <b>Publication details:<br></b>
 * <b>Authors:</b> Fix, F., Hodges, J. L.<br>
 * <b>Year:</b> 1951 <br>
 * <b>Title:</b> Discriminatory analysis, nonparametric discrimination: Consistency properties <br>
 * <b>Published In:</b> USAF School of Aviation Medicine, Randolph Field, Texas (Technical Report) <br>
 * <b>Abstract:</b> The discrimination problem (two population case) may be defined as follows: e random variable Z, of observed value z, is distributed over some space (say, p-dimensional) either according to distribution F, or according to distribution G. The problem is to decide, on the basis of z, which of the two distributions Z has. <br>
 * </p>
 * @author Parham Nooralishahi - PHM!
 * @email parham.nooralishahi@gmail.com
 */
public class KNearestNeighborsClassifier implements OfflineClassifier<Features> {

    protected LinkedList<Features> dataspace;
    protected DistanceMeasure distanceMethod;
    protected int knum = 3;
    protected LinkedList<KNNRecord> distances = new LinkedList<>();
    protected ArraySet<Object> classes = new ArraySet<>();

    public KNearestNeighborsClassifier (DistanceMeasure dm, int k) {
        distanceMethod = Objects.requireNonNull(dm);
        knum = k;
    }
    public KNearestNeighborsClassifier (DistanceMeasure dm) {
        this (dm, 3);
    }
    public KNearestNeighborsClassifier () {
        this (new ApacheWrapperDistanceMeasure(), 3);
    }
    
    public DistanceMeasure getDistanceMeasure () {
        return distanceMethod;
    }
    public int getK () {
        return knum;
    }
    public Set<Object> getClasses () {
        return classes;
    }
    
    protected void initClassDistribution () {
        classes = new ArraySet<>();
        // Extract classes
        dataspace.stream().forEach((x) -> {
            classes.add(x.getLabel());
        });
        // Measure instance's class probability
        for (int index = 0; index < dataspace.size(); index++) {
            Features d = dataspace.get(index);
            HashMap<Object, Double> cs = new HashMap<>();
            classes.stream().forEach((c) -> {
                cs.put(c, 0.0);
            });
            cs.put (d.getLabel(), 1.0);
            d.setLabel (cs);
        }
    }
    
    @Override
    public void build (Collection<? extends Features> dtst) {
        dataspace = new LinkedList<>(dtst);
        initClassDistribution();
    }

    protected Map<Features, Double> distance (Features df) {
        LinkedList<KNNRecord> list = new LinkedList<>();
        dataspace.stream().forEach((Features d) -> {
            double dis = distanceMethod.measure (d, df, new ParametersContainer()).distance;
            list.add(new KNNRecord(Math.abs(dis), d));
        });
        
        HashMap<Features, Double> map = new HashMap<>();
        for (int index = 0; index < knum; index++) {
            KNNRecord f = Collections.min (list, new KNNComparitor());
            list.remove (f);
            map.put(f.data, f.distance);
        }
        return map;
    }
    
    @Override
    public synchronized Object classify (Features df) {
        HashMap<Object, Double> res = new HashMap<>(classDistribution (df));
        Object maxl = null;
        double maxv = -1;
        LinkedList<Object> vs = new LinkedList<>(res.keySet()); 
        for (Object x : vs) {
            Double v = res.get(x);
            if (maxv < v) {
                maxl = x;
                maxv = v;
            }
        }
        return maxl;
    }

    @Override
    public synchronized ClassProbabilityDistribution classDistribution (Features df) {
        HashMap<Features, Double> knearest = new HashMap<>(distance(df));
        // Create Classification Distribution
        ClassProbabilityDistribution res = new ClassProbabilityDistribution();
        knearest.keySet().stream().forEach ((Features x) -> {
            double value = res.getOrDefault(x.getLabel(), 0.0);
            res.put(x.getLabel(), value + 1);
        });
        return res;
    }
    
    protected class KNNRecord {
        public double distance = 0;
        public Features data;
        
        public KNNRecord (double dis, Features d) {
            distance = dis;
            data = d;
        }
    }
    protected class KNNComparitor implements Comparator<KNNRecord>  {
        @Override
        public int compare(KNNRecord o1, KNNRecord o2) {
            double dis1 = o1.distance;
            double dis2 = o2.distance;
            double temp = dis1 - dis2;
            if (temp > 0) {
                return 1;
            } else if (temp < 0) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
