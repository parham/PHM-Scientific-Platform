
package com.phm.comp.neuralnet.gng.mgng;

import com.phm.comp.distance.DistanceInfo;
import com.phm.comp.distance.DistanceMeasure;
import com.phm.core.ParametersContainer;
import com.phm.core.data.Features;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author phm
 */
public class MGNGContextDistanceMethod implements DistanceMeasure {

    @Override
    public DistanceInfo measure (RealVector sc1, RealVector sc2, ParametersContainer pc) {
        float dw = 0;
        float dc = 0;
        final int ndims = sc1.getDimension();
        double [] chs = new double [ndims];
        float [] lc = (float []) pc.get(MGNGTrainingSupervisor.NEURON_LOCAL_CONTEXT);
        float [] gc = (float []) pc.get(MGNGTrainingSupervisor.MGNG_GLOBAL_CONTEXT);
        float alpha = (Float) pc.get(MGNGTrainingSupervisor.MGNG_ALPHA);
        
        for (int dim = 0; dim < ndims; dim++) {
            dw += (float) Math.pow((sc2.getEntry(dim) - sc1.getEntry(dim)), 2.0);
            dc += (float) Math.pow((gc[dim] - lc[dim]), 2.0);
            chs [dim] = sc2.getEntry(dim) - sc1.getEntry(dim);
        }
        dw = (float) Math.abs(Math.sqrt(dw));
        dc = (float) Math.abs(Math.sqrt(dc));
        
        float dist = ((1 - alpha) * dw) - (alpha * dc);
        return new DistanceInfo (sc1, sc2, dist, new Features(chs));
    }
}
