
package com.phm.comp.window;

import com.phm.annotations.ImplementationDetails;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

@ImplementationDetails (
        className = "RectangularWindow",
        date = "2015/10/10",
        lastModification = "2015/10/14",
        version = "1.0.0"
)
public class RectangularWindow extends Window {

    protected double initValue = 1.0;
    
    public RectangularWindow (int order, double init) {
        super (order);
        initValue = init;
    }
    
    public double getInitialValue () {
        return initValue;
    }
    
    @Override
    public RealVector getWindow () {
        double [] result = new double[orderObj];
        for (int index = 0; index < orderObj; index++) {
            result [index] = initValue;
        }
        return new ArrayRealVector(result);
    }
}
