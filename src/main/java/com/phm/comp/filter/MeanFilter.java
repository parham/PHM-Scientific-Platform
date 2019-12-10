
package com.phm.comp.filter; 

import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author phm
 */
public class MeanFilter implements VectorFilter {
    
    protected int filtersize = 3;
    
    public MeanFilter (int fsize) {
        filtersize = fsize;
    }
    public int getFilterSize () {
        return filtersize;
    }
    public void setFilterSize (int a) {
        filtersize = a;
    }

    @Override
    public RealVector filter (RealVector data) {
        final int fs = filtersize / 2;
        RealVector res = data.copy();
        for (int index = fs; index < res.getDimension()- fs; index++) {
            double tmp = 0;
            for (int dim = index - fs; dim < index + fs; dim++) {
                tmp += res.getEntry(dim);
            }
            tmp /= filtersize;
            res.setEntry(index, tmp);
        }
        return res;
    }

}
