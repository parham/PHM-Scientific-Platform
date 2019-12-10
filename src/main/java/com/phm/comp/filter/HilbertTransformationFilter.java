
package com.phm.comp.filter;

import com.phm.annotations.ImplementationDetails;
import com.phm.annotations.PublicationDetails;
import com.phm.annotations.PublicationType;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

@ImplementationDetails (
    className = "HilbertTransformationFilter",
    date = "10/15/2015",
    lastModification = "10/15/2015",
    version = "1.0.0",
    description = "Computes the N-point Discrete Hilbert Transform of real" +
                  " valued vector x: The algorithm consists of the following stages: - X(w)" +
                  "= FFT(x) is computed - H(w), DFT of a Hilbert transform " +
                  "filter h[n], is created: H[0]=H[N/2]=1 H[w]=2 for w=1,2,...,N/2-1" +
                  "H[w]=0 for w=N/2+1,...,N-1 - x[n] and h[n] are convolved (i.e. X(w) " +
                  "and H(w) multiplied) - y[n], the Discrete Hilbert Transform" +
                  "of x[n] is computed by y[n]=IFFT(X(w)H(w)) for n=0,...,N-1"
)
@PublicationDetails (
    author = {"Stefan L. Hahn"},
    title = "Hilbert transforms in signal processing",
    type = PublicationType.Journal,
    year = "1996",
    journal = "Artech House on Demand"
)
public class HilbertTransformationFilter implements VectorFilter {
    
    private final int numFeatures;
    
    public HilbertTransformationFilter (int num) {
        numFeatures = num;
    }
    
    public int getOutputDimension () {
        return numFeatures;
    }
    
    public Complex [] filterAsComplex (RealVector data, int n) {
        return filterAsComplex(data.toArray(), n);
    }
    public Complex [] filterAsComplex (double [] data, int n) {
        double [] dt = new double [n];
        System.arraycopy (data, 0, dt, 0, data.length);
        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
        Complex [] dfft = fft.transform(dt, TransformType.FORWARD);
        double [] H = new double[n];
        int NOver2 = (int) Math.floor(n / 2 + 0.5);
        int w = 0;
        H[0] = 1.0;
        H[NOver2] = 1.0;
        for (w = 1; w <= NOver2 - 1; w++) {
            H[w] = 2.0;
        }
        for (w = NOver2 + 1; w <= n - 1; w++) {
            H[w] = 0.0;
        }
        for (w = 0; w < n; w++) {
            dfft[w] = new Complex(dfft[w].getReal() * H[w], dfft[w].getImaginary() * H[w]);
        }
        return fft.transform(dfft, TransformType.INVERSE);
    }

    @Override
    public RealVector filter (RealVector data) {
        Complex [] d = filterAsComplex(data, numFeatures);
        double [] res = new double [d.length];
        for (int index = 0; index < res.length; index++) {
            res [index] = d [index].abs();
        }
        return new ArrayRealVector(res);
    }
}
