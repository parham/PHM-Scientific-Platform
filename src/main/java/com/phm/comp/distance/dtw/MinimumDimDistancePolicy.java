
package com.phm.comp.distance.dtw;

import com.phm.comp.distance.DimensionalDistanceMeasure;
import com.phm.comp.distance.DistanceInfo;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author phm
 */
public class MinimumDimDistancePolicy implements DimensionalDistanceMeasure {

    @Override
    public RealVector measure (RealVector s1,
                               RealVector s2,
                               DistanceInfo dinfo) {
        
        double [] disDim = new double [s1.getDimension()];
        for (int index = 0; index < disDim.length; index++) {
            if (index < s2.getDimension()) {
                disDim [index] = s2.getEntry(index) - s1.getEntry(index);
            }
        }

        DTWDistanceInfo tdinfo = (DTWDistanceInfo) dinfo;
        for (DTWDistanceInfo.WarpingIndex p : tdinfo.warpingPath) {
            double td = (s2.getEntry(p.col) - s1.getEntry(p.row));
            if (Math.abs(disDim [p.row]) > Math.abs(td)) {
                disDim [p.row] = td;
            }
        }
        dinfo.distancedim = new ArrayRealVector (disDim);
        return dinfo.distancedim;
    }
}