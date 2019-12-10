
package com.phm.comp.test;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author PHM
 */
public class TestMain1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        double [] tmp = {4, 3, 2, 64, 32};
        RealVector s1 = new ArrayRealVector(tmp);
        RealVector s2 = s1.subtract(s1);
        System.out.println (s1.toString() + "\t" + s2.toString());
    }
    
}
