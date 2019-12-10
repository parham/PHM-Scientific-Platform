
package com.phm.comp.filter.detrend;

import Jama.Matrix;
import com.phm.comp.filter.VectorFilter;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author phm
 */
public class FlindersDetrendFilter implements VectorFilter {
    
    protected int pObj = 1;
    
    public FlindersDetrendFilter (int p) {
        pObj = p;
    }
    
    public void setP (int p) {
        pObj = p;
    }
    public int getP () {
        return pObj;
    }

    @Override
    public RealVector filter (RealVector data) {
        int r = data.getDimension();
        Matrix x = new Matrix(data.toArray(), r);
        if (x.getRowDimension() == 1) {
            x = x.transpose();
        }
        // check time scale
        Matrix t = new Matrix(r, 1);
        for (int index = 0; index < r; index++) {
            t.set (index, 0, index);
        }
        Matrix ones_1_p1 = new Matrix(1, pObj + 1, 1);
        Matrix ones_lent_1 = new Matrix (t.getRowDimension(), 1, 1);
        Matrix p_0p = new Matrix (1, pObj + 1);
        
        for (int index = 0; index <= pObj; index++) {
            p_0p.set (0, index, index);
        }
        Matrix b1 = t.times(ones_1_p1);
        Matrix b2 = ones_lent_1.times(p_0p);
        
        for (int tr = 0; tr < b1.getRowDimension(); tr++) {
            for (int tc = 0; tc < b1.getColumnDimension(); tc++) {
                b1.set (tr, tc, Math.pow (b1.get (tr, tc), b2.get (tr, tc)));
            }
        }
        /////////////
        Matrix b = b1;
        Matrix res = b.solve (x);
        res = b.times(res);
        res = x.minus(res);
        double [] resarr = new double [res.getRowDimension()];
        for (int index = 0; index < resarr.length; index++) {
            resarr [index] = res.get(index, 0);
        }
        RealVector resvec = new ArrayRealVector (resarr);
        return resvec;
    }
    
}
