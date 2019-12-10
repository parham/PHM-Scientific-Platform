
package com.phm.comp.distance;
 
import com.phm.core.ParametersContainer; 
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.ml.distance.EuclideanDistance;

/**
 *
 * @author phm
 */
public class ApacheWrapperDistanceMeasure implements DistanceMeasure {
    
    protected org.apache.commons.math3.ml.distance.DistanceMeasure dmeasure;
    
    public ApacheWrapperDistanceMeasure (org.apache.commons.math3.ml.distance.DistanceMeasure dm) {
        dmeasure = dm;
    }
    public ApacheWrapperDistanceMeasure () {
        dmeasure = new EuclideanDistance();
    }
    
    @Override
    public DistanceInfo measure (RealVector sc1, RealVector sc2, ParametersContainer pc) {
        double dis = dmeasure.compute(sc1.toArray(), sc2.toArray());
        RealVector rv = sc1.subtract (sc2);
        return new DistanceInfo(sc1, sc2, dis, rv);
    }
    
}
