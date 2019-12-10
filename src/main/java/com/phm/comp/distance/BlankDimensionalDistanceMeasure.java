
package com.phm.comp.distance;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author phm
 */
public class BlankDimensionalDistanceMeasure implements DimensionalDistanceMeasure {

    @Override
    public RealVector measure(RealVector s1, RealVector s2, DistanceInfo dinfo) {
        return new ArrayRealVector (Math.min(s1.getDimension(),
                                             s2.getDimension()));
    }
    
}
