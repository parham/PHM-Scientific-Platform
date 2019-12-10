package com.phm.comp.filter.welch;

import java.util.Arrays;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

/**
 * Computes
 * <a href="https://en.wikipedia.org/wiki/Discrete_Fourier_transform">DFT</a>
 * through
 * <a href="https://en.wikipedia.org/wiki/Fast_Fourier_transform">FFT</a>
 * using the JTransforms library
 *
 * @author knoodrake
 *
 */
public class FFTFilter {

    public class Data {

        public double[] x;
        public Complex[] y;

        public double[] initialX;
        public double[] initialY;

        public Data(double[] x, Complex[] y) {
            this.x = x;
            this.y = y;
        }

        public Data(double[] initialData) {
            initialY = initialData;
        }

        public Data(double[][] initialData) {
            initialX = initialData[0];
            initialY = initialData[1];
        }

        private Data() {
        }
    }
    private Data data = new Data();

    public FFTFilter() {
    }

    public FFTFilter(double[] data) {
        this.data.initialY = data.clone();
    }

    public FFTFilter(double[][] data) {
        this.data.initialX = data[0].clone();
        this.data.initialY = data[1].clone();
    }

    public FFTFilter forwardFull() {
        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
//        fft.transform(f, TransformType.FORWARD)
//                DoubleFFT_1D fft = new DoubleFFT_1D(data.initialY.length);
        double[] yData = Arrays.copyOf(data.initialY, data.initialY.length * 2);
        data.y = fft.transform (yData, TransformType.FORWARD);
//        fft.realForwardFull(yData);
        if (data.initialX != null) {
            //data.x = oneOfTwo(data.initialX);
            data.x = data.initialX.clone();
        }
//        data.y = Complex.fromFFTArray(yData);
        return this;
    }

    /**
     * Compute the DFT of the signal
     *
     * @return itself
     */
    public FFTFilter forward() {
        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
//        DoubleFFT_1D fft = new DoubleFFT_1D(data.initialY.length);
        double [] yData = data.initialY;
        data.y = fft.transform(yData, TransformType.FORWARD);
//        fft.realForward(yData);
        if (data.initialX != null) {
            data.x = Filter.oneOfTwo(data.initialX);
        }
//        data.y = Complex.fromFFTArray(yData);
        return this;
    }

    public FFTFilter inverse() {
        data.initialY = new double [data.y.length];
        for (int index = 0; index < data.initialY.length; index++) {
            data.initialY [index] = data.y [index].abs();
        }
//        data.initialY = Complex.toFFTArray(data.y);
        
//        new DoubleFFT_1D(data.initialY.length).realInverse(data.initialY, false);
        return this;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public double[][] getInitialData() {
        return new double[][]{
            data.initialX,
            data.initialY
        };
    }
}
