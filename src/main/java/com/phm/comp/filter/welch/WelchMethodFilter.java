package com.phm.comp.filter.welch;

/**
 * Welch method is a method to computes a special kind of periodogram averaged
 * by time to reduce variance and therfore noise. Multiple window functions are
 * provided to compute the periodogram
 *
 * @author knoodrake
 *
 */
public class WelchMethodFilter extends Filter {
    
    /**
     * Computes the periodogram of given signal with welch method
     *
     * @param data	The original data, index 0 is X, time, and index 1 is
     *                  amplitude
     * @param fs	Sampling rate
     * @param lfq	Lower frequency (in Hz) to show up
     * @param hfq	Higher frequency (in Hz) to show up
     * @param segLen desired window length
     * @param useSquareWindow if true, uses square window instead of Hann
     * @param logYScale if true, amplitude is expressed in dB
     * @return	the periodogram plot data
     */
    public double[][] compute (
            double[][] data,
            double fs,
            int lfq,
            int hfq,
            int segLen,
            boolean useSquareWindow,
            boolean logYScale
    ) {
        Window w = useSquareWindow ? new SquareWindow() : new HannWindow();
        return compute (data, segLen, fs, lfq, hfq, w, logYScale);
    }

    /**
     * Computes the periodogram of given signal with welch method
     *
     * @param data	The original data, index 0 is X, time, and index 1 is
     * amplitude
     * @param segmentLength	Length of the data chuncks in samples
     * @param fs                Sample rate
     * @param freqLowerLimit	Lower frequency (in Hz) to show up
     * @param freqUpperLimit	Higher frequency (in Hz) to show up
     * @param win	the {@link Window} function used
     * @param logYScale	If true, magnitude is expressed in a dB scale
     * @return	the periodogram plot data
     * @see #compute(double[][], double, int, int)
     */
    public double[][] compute (
            double[][] data,
            int segmentLength,
            double fs,
            double freqLowerLimit,
            double freqUpperLimit,
            Window win,
            Boolean logYScale
    ) {

        // avoid aliasing
        while (data[1].length % segmentLength != 0) {
            segmentLength++;
        }

        ChunkedData chunked = new ChunkedData(data[1], (int) fs, segmentLength, win.getRecommandedOverlappingSize(segmentLength), true);
        // Limit the displayed spectrum
        int from = (int) ((chunked.getOverlapSize() / fs) * freqLowerLimit);
        int to = (int) ((chunked.getOverlapSize() / fs) * freqUpperLimit);
        double[][] powerFrequency = {
            new double[to - from],
            new double[to - from]
        };

        // inserts frequencies (x-axis)
        for (int i = from; i < to; i++) {
            powerFrequency[0][i - from] = (i * fs / chunked.getOverlapSize()) / 2.;
        }

        // computes the spectral density of each chunk
        while (chunked.hasNextChunk()) {
            double[] psd = SpectralDensityFilter.compute(chunked.getChunk(), fs);
            for (int ii = from; ii < to; ii++) {
                powerFrequency[1][ii - from] += psd[ii];
            }
            chunked.nextChunk();
        }

        // averages all the density spectrums into one
        if (logYScale) {
            for (int i = 0; i < powerFrequency[1].length; i++) {
                powerFrequency[1][i] = Math.log10(powerFrequency[1][i] / chunked.getNumberOfChunk());
            }
        } else {
            for (int i = 0; i < powerFrequency[1].length; i++) {
                powerFrequency[1][i] = powerFrequency[1][i] / chunked.getNumberOfChunk();
            }
        }

        return powerFrequency;
    }
}
