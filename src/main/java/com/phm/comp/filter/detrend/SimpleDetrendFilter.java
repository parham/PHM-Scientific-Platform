
package com.phm.comp.filter.detrend;

import com.phm.comp.filter.VectorFilter;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author phm
 */
public class SimpleDetrendFilter implements VectorFilter {

    protected int nObj = 1;
    
    public SimpleDetrendFilter (int n) {
        nObj = n;
    }
    
    public void setN (int n) {
        nObj = n;
    }
    public int getN () {
        return nObj;
    }

    @Override
    public RealVector filter (RealVector data) {
        double [] array = new double [data.getDimension()];
        System.arraycopy(data.toArray(), 0, array, 0, array.length);
        double x, y, a, b;
        double sy = 0.0,
               sxy = 0.0,
               sxx = 0.0;
        int i;

        for (i = 0, x = (-nObj / 2.0 + 0.5); i < nObj; i++, x+=1.0) {
             y = array[i];
             sy += y;
             sxy += x * y;
             sxx += x * x;
        }
        b = sxy / sxx;
        a = sy / nObj;

        for (i=0, x=(-nObj / 2.0 + 0.5); i < nObj; i++, x += 1.0) {
             array[i] -= (a+b*x);
        }
        return new ArrayRealVector(array);
    }
    
}
