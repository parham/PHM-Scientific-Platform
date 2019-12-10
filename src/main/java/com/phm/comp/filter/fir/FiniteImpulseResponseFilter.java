
package com.phm.comp.filter.fir;

import com.phm.comp.filter.VectorFilter;
import com.phm.comp.window.Window;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author phm
 */
public class FiniteImpulseResponseFilter implements VectorFilter {

    protected Window weightsWindow;
    
    public FiniteImpulseResponseFilter (Window win) {
        weightsWindow = win;
    }
    
    public void setWindow (Window win) {
        weightsWindow = win;
    }
    public Window getWindow () {
        return weightsWindow;
    }
    
    @Override
    public RealVector filter (RealVector data) {
        double [] win = weightsWindow.getWindow().toArray();
        double [] signal = data.toArray();
        double [] res = new double [signal.length];
        System.arraycopy (signal, 0, res, 0, res.length);
        final int windiv = win.length / 2;
        for (int index = windiv; index < signal.length - (win.length - windiv); index++) {
            double tmp = 0;
            for (int dim = 0; dim < win.length; dim++) {
                tmp += signal [index + dim - windiv] * win [dim];
            }
            res [index] = tmp;
        }
        return new ArrayRealVector(res);
    }
    
}
