package com.phm.comp.neuralnet.art.fuzzy;

import com.phm.comp.filter.VectorFilter;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;


/**
 *
 * @author phm
 */
public class FuzzyARTInputStrategy implements VectorFilter {
    @Override
    public RealVector filter (RealVector data) {
        final int ndim = data.getDimension();
        double [] tmp = new double [ndim * 2];
        for (int index = 0; index < ndim; index++) {
            tmp [index] = data.getEntry(index);
            tmp [ndim + index] = 1 - data.getEntry(index);
        }
        return new ArrayRealVector (tmp);
    }
}
