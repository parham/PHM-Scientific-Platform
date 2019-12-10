
package com.phm.comp.distance;

import com.phm.core.ParametersContainer;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author phm
 */
public class BlankDistanceMeasure implements DistanceMeasure {
    @Override
    public DistanceInfo measure(RealVector sc1, RealVector sc2, ParametersContainer pc) {
        return new DistanceInfo ();
    }
}
