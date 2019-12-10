
package com.phm.comp.distance;

import com.phm.core.ParametersContainer; 
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author PARHAM
 */
public interface DistanceMeasure {
    public DistanceInfo measure (RealVector sc1, RealVector sc2, ParametersContainer pc);
}