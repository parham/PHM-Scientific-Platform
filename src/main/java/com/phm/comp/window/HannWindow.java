
package com.phm.comp.window;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class HannWindow extends Window {

    public HannWindow (int order) {
        super (order);
    }

    @Override
    public RealVector getWindow () {
        double [] win = new double [orderObj];
        for (int index = 0; index < win.length; index++) {
            win [index] = 0.5 * (1.0 - Math.cos((2.0 * Math.PI * index) / (orderObj - 1)));
        }
        return new ArrayRealVector(win);
    }

    public int getRecommandedOverlappingSize (int segmentLength) {
        if (segmentLength % 2 == 0) {
            return (int) (segmentLength / 2d);
        } else {
            return (int) ((segmentLength - 1d) / 2d);
        }
    }
}
