
package com.phm.comp.distance;

import org.apache.commons.math3.linear.RealVector;


/**
 *
 * @author phm
 */
public interface DimensionalDistanceMeasure {
    public RealVector measure (RealVector s1, RealVector s2, DistanceInfo dinfo);
}
