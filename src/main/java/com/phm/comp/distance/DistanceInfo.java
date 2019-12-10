
package com.phm.comp.distance;
 
import java.io.Serializable;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author phm
 */
public class DistanceInfo implements Serializable {
    public RealVector entityOne;
    public RealVector entityTwo;
    public double distance = 0.0f;
    public RealVector distancedim;
    
    public DistanceInfo (RealVector e1, RealVector e2) {
        this (e1, e2, 0.0, 
              new ArrayRealVector (Math.min(e1.getDimension(),
                                            e2.getDimension())));
    }
    public DistanceInfo (RealVector e1, RealVector e2, 
                         double dis, RealVector disdim) {
        entityOne = e1;
        entityTwo = e2;
        distancedim = disdim.copy();
        distance = dis;
    }
    public DistanceInfo () {
        // Empty body
    }
}
