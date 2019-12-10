
package com.phm.comp.test;

import com.phm.comp.filter.fir.FiniteImpulseResponseFilter;
import com.phm.comp.window.RectangularWindow;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author phm
 */
public class TestMain2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FiniteImpulseResponseFilter fir = new FiniteImpulseResponseFilter(new RectangularWindow(4, 0.25));
        double [] data = new double [20];
        for (int index = 0; index < data.length; index++) {
            data [index] = index;
            System.out.print (data [index] + "\t");
        }
        System.out.println();
        RealVector res = fir.filter(new ArrayRealVector(data));
        for (int index = 0; index < res.getDimension(); index++) {
            System.out.print (res.getEntry(index) + "\t");
        }
        System.out.println();
    }
    
}
