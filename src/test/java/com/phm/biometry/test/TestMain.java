
package com.phm.biometry.test;

import flanagan.complex.Complex;
import flanagan.math.FourierTransform;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

/**
 *
 * @author phm
 */
public class TestMain {
    static double [] data = {0.5106124773,0.5106980574,0.5108550222,0.5105081744,
0.5108552605,0.5108203907,0.5107551561,0.5105985089,0.5104234794,0.5104992669,0.5104037467,
0.51039434,0.5104089098,0.5103897899,0.5100287468,0.5101298837,0.5099393428,0.5100885916,0.5099316381,
0.5099491694,0.5098936932,0.5098507897,0.5097681032,0.5096583197,0.5099544117,0.5100931304,0.5099464801,
0.5101304284,0.5099225603,0.5097633941,0.5099419413,0.5099992443,0.5100407521,0.5102723583,0.5102660607,
0.5102520697,0.51021501,0.5104524373,0.5103249523,0.5103066948,0.5106188544,0.510600438,0.5106894562,
0.5108214801,0.5108933528,0.5104794775,0.5108686388,0.5112193786,0.5112089846,0.5112890159,0.5111922816,
0.5114416008,0.5113860907,0.511520418,0.5116070761,0.5115279865,0.5115680986,0.5116280682,0.5115948212,
0.5115914057,0.5111080632,0.5114689701,0.5118631694,0.5119498843,0.5117215459,0.5118473402,0.5118743124,
0.5121193196,0.5121536447,0.5118537059,0.5117676493,0.5119596087,0.5120873888,0.5122218523,0.5121873684,
0.5121757262,0.5114454248,0.511888292,0.5122168709,0.5120996777,0.5121387686,0.5122930669,0.512019045,
0.5118670728,0.511481713,0.5112000885,0.5114490105,0.5114056418,0.5112810957,0.5110938339,0.5111189224,
0.5105036015,0.510989429,0.5108533769,0.5107418346,0.5105868554,
0.5105718318,0.5106957652,0.5105674178,0.5104306168};
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int numdim = FourierTransform.nextPowerOfTwo(data.length * 2);
        double [] d1 = new double [numdim]; //8192
        System.arraycopy(data, 0, d1, 0, Math.min(d1.length, data.length));
        //////////////////
        FourierTransform fft1 = new FourierTransform(d1);
        fft1.transform();
//        double [][] pwd1 = fft1.powerSpectrum();
        Complex [] fft1d = fft1.getTransformedDataAsComplex();
        double [] fft1dd = new double [fft1d.length];
        for (int index  = 0; index < fft1d.length; index++) {
            fft1dd[index] = fft1d[index].over(numdim).abs();
//            fft1dd[index] = fft1d [index].abs();
        }
        double [] fft2d = fft (data);
        System.out.println (numdim);
    }
    
    public static double [] fft (double [] data) {
        // Apply FFT
        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);

        double [] dd = new double [8192];
        System.arraycopy (data, 0, dd, 0, Math.min(dd.length, data.length));
        org.apache.commons.math3.complex.Complex [] lres = fft.transform(dd, TransformType.FORWARD);
        double [] res = new double [lres.length];
        for (int index = 0; index < res.length; index++) {
            res [index] = lres [index].abs();
        }
        return res;
    }
    
}
