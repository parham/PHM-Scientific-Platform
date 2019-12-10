
package com.phm.comp.math;

import java.security.InvalidParameterException;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

/**
 *
 * @author phm
 */
public class MathUtils {
    public static double periodogram (RealVector sig, int n) {
        if (sig.getDimension() < 1) {
            throw new InvalidParameterException("The length of input data is not enough.");
        }
        double [] data = sig.toArray();
        double [] dd = new double [n];
        System.arraycopy (data, 0, dd, 0, Math.min(dd.length, data.length));
        FastFourierTransformer fft = new FastFourierTransformer (DftNormalization.STANDARD);
        Complex [] lres = fft.transform (dd, TransformType.FORWARD);
        double res = 0.0;
        for (int index = 0; index < lres.length; index++) {
            res = lres [index].abs();
        }
        res = (1.0 / n) * Math.pow(res, 2.0);
        return res;
    }
}
