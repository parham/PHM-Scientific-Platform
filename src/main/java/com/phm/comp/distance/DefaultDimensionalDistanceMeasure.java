
package com.phm.comp.distance;

import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author phm
 */
public class DefaultDimensionalDistanceMeasure implements DimensionalDistanceMeasure {
    @Override
    public RealVector measure (RealVector s1, RealVector s2, DistanceInfo dinfo) {
        return s1.subtract(s2);
    }
}
