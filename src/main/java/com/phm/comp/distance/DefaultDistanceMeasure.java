
package com.phm.comp.distance;

import com.phm.core.ParametersContainer;
import java.util.Objects; 
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author phm
 */
public class DefaultDistanceMeasure implements DistanceMeasure {

    protected DistanceMeasure distanceMeasure;
    protected DimensionalDistanceMeasure dimdistanceMethod;
    
    public DefaultDistanceMeasure () {
        this (new ApacheWrapperDistanceMeasure(), new DefaultDimensionalDistanceMeasure());
    }
    public DefaultDistanceMeasure (DistanceMeasure dm) {
        this (dm, new DefaultDimensionalDistanceMeasure());
    }
    public DefaultDistanceMeasure (DistanceMeasure dm, DimensionalDistanceMeasure ddm) {
        distanceMeasure = Objects.requireNonNull(dm);
        dimdistanceMethod = Objects.requireNonNull(ddm);
    }
    
    @Override
    public DistanceInfo measure (RealVector sc1, RealVector sc2, ParametersContainer pc) {
        DistanceInfo dinfo = distanceMeasure.measure (sc1, sc2, pc);
        dinfo.distancedim = dimdistanceMethod.measure (sc1, sc2, dinfo);
        return dinfo;
    }
}
