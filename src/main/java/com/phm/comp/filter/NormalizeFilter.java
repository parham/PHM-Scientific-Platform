
package com.phm.comp.filter;

import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author phm
 */
public class NormalizeFilter implements VectorFilter {

    @Override
    public RealVector filter (RealVector inst) {
        double mean = 0.0f;
        // Calculate mean 
        RealVector res = inst.copy();
        int numdim = res.getDimension();
        for (int index = 0; index < numdim; index++) {
            mean += res.getEntry(index);
        }
        mean /= numdim;
        // Calculate deviation
        double dev = 0.0f;
        for (int index = 0; index < numdim; index++) {
            dev += (inst.getEntry(index) - mean) * (inst.getEntry(index) - mean);
        }
        dev = Math.sqrt (dev / numdim);
        if (dev == 0) {
            dev = 0.000001f;
        }
        res = res.mapSubtract(mean).mapDivide(dev);
        return res;
    }
}
