
package com.phm.comp.distance.dtw;

import com.phm.comp.distance.DimensionalDistanceMeasure;
import com.phm.comp.distance.DistanceInfo;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author Parham Nooralishahi - PHM!
 */
public class AverageDimDistancePolicy implements DimensionalDistanceMeasure {
    
    @Override
    public RealVector measure (RealVector s1, RealVector s2, DistanceInfo dinfo) {
        double [] dims = new double [s1.getDimension()];
        double [] ndims = new double [dims.length];
        DTWDistanceInfo dinf = (DTWDistanceInfo) dinfo;
        for (DTWDistanceInfo.WarpingIndex p : dinf.warpingPath) {
            dims [p.row] += (s2.getEntry(p.col) - s1.getEntry(p.row));
            ndims [p.row]++;
        }
        for (int index = 0; index < dims.length; index++) {
            if (ndims [index] != 0) {
                dims [index] /= ndims [index];
            }
        }
        dinf.distancedim = new ArrayRealVector (dims);
        return dinf.distancedim;
    }
}
