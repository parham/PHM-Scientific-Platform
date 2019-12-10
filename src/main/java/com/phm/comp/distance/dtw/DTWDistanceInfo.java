
package com.phm.comp.distance.dtw;

import com.phm.comp.distance.DistanceInfo;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects; 
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author phm
 */
public class DTWDistanceInfo extends DistanceInfo {
    
    public LinkedList<WarpingIndex> warpingPath = new LinkedList<>();
    public CostMatrix costMatrix;
    public float normalizingFactor;
    
    public DTWDistanceInfo(RealVector sc1, RealVector sc2, 
                           float dis,  
                           CostMatrix cost, 
                           List<WarpingIndex> path, 
                           float norm) {
        super (sc1, sc2);
        this.distance = dis;
        this.distancedim = new ArrayRealVector (new double[sc1.getDimension()]);
        warpingPath.addAll(path);
        costMatrix = Objects.requireNonNull(cost);
        normalizingFactor = norm;
    }
    public DTWDistanceInfo () {
        // Empty body
    }
    
    public static class WarpingIndex {
        public final int row;
        public final int col;
        public final double value;
        
        public WarpingIndex (int r, int c) {
            this (r, c, 0.0f);
        }
        public WarpingIndex (int r, int c, double v) {
            row = r;
            col = c;
            value = v;
        }
    }
}
