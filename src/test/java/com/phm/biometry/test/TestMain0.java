
package com.phm.biometry.test;

import java.util.Random;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

/**
 *
 * @author phm
 */
public class TestMain0 {

    /**
     * @param args the command line arguments
     */
    public static void main(String [] args) {
        double high = Math.ceil(Math.log(120) / Math.log(2));
        double low = Math.floor(Math.log(120) / Math.log(2));
        System.out.println ("120\t" + low + "\t" + high);
        int len = 120;
        int numdim = (int) Math.ceil (Math.log(len) / Math.log(2));
        double [] data = new double [(int) Math.pow (2, numdim)];
        Random rand = new Random (System.currentTimeMillis());
        for (int index = 0; index < len; index++) {
            data [index] = rand.nextDouble();
        }
        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
        Complex [] res = fft.transform(data, TransformType.FORWARD);
        double [] dfft = new double[res.length];
        for (int index = 0; index < dfft.length; index++) {
            dfft [index] = res [index].abs();
        }
        System.out.println ();
    }
    
}
