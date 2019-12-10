
package com.phm.comp.filter.fir;

import com.phm.comp.filter.VectorFilter;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author phm
 */
public class ExponentialMovingAverageFilter implements VectorFilter {
    private double smoothingFactor;
    
    public boolean setSmoothingFactor (double sf) {
        if (smoothingFactor <= 0 || smoothingFactor >= 1) {
            smoothingFactor = sf;
            return true;
        }
        return false;
    }
    public double getSmoothingFactor () {
        return smoothingFactor;
    }

    @Override
    public RealVector filter (RealVector values) {
            int length = values.getDimension();
            double [] result = new double [length];
            result [0] = values.getEntry(0) * smoothingFactor;
            double k = 1 - smoothingFactor;
            for (int i = 1; i < length; i++) {
                result [i] = values.getEntry(i) * smoothingFactor + result [i - 1] * k;
            }
            return new ArrayRealVector (result);
    }
}
