
package com.phm.comp.test;

import com.phm.comp.filter.HilbertTransformationFilter;
import java.util.Random;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.ArrayRealVector;

/**
 *
 * @author phm
 */
public class TestHilbertTransform {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        HilbertTransformationFilter htf = new HilbertTransformationFilter(10);
        Random rand = new Random(System.currentTimeMillis());
        ArrayRealVector arr = new ArrayRealVector(100);
        for (int index = 0; index < arr.getDimension(); index++) {
            arr.setEntry(index, rand.nextDouble());
        }
        Complex [] res = htf.filterAsComplex(arr, 256);
        System.out.println ("FInisheD");
    }
    
}
