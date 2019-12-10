
package com.phm.biometry.heartrate.v1;

import com.phm.biometry.heartrate.HeartrateEngine;
import com.phm.biometry.heartrate.HeartrateInfo;
import com.phm.core.data.DatasetUtils;
import com.phm.comp.filter.DatasetFilter;
import com.phm.comp.filter.ica.RecursiveICA;
import com.phm.core.data.Features;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

/**
 *
 * @author PHM
 */
public class v1HeartrateEngine implements HeartrateEngine {
    
    protected UnivariateInterpolator interpolator = new LinearInterpolator();
    protected DatasetFilter icaFilter = new RecursiveICA();
    protected double timePeriod = 10;
    protected int freqRange = 16384; //10000; 8192
    protected int peakRange = 5;
    protected double calibrate = 6;
    protected double minHRrange = 65; //50
    protected double maxHRrange = 130; //200

    public v1HeartrateEngine (UnivariateInterpolator ui, DatasetFilter ica) {
        interpolator = ui;
        icaFilter = ica;
    }
    public v1HeartrateEngine (UnivariateInterpolator ui) {
        this (ui, new RecursiveICA());
    }
    public v1HeartrateEngine (DatasetFilter ica) {
        this (new LinearInterpolator(), ica);
    }
    public v1HeartrateEngine () {
        this (new LinearInterpolator(), new RecursiveICA());
    }
    public void setICA (DatasetFilter df) {
        icaFilter = df;
    }
    public DatasetFilter getICA () {
        return icaFilter;
    }
    public void setInterpolation (UnivariateInterpolator ui) {
        interpolator = ui;
    }
    public UnivariateInterpolator getInterpolation () {
        return interpolator;
    }
    public void setMinimumHRRange (double min) {
        minHRrange = min;
    }
    public double getMinimumHRRange () {
        return minHRrange;
    }
    public void setMaximumHRRange (double max) {
        maxHRrange = max;
    }
    public double getMaximumHRRange () {
        return maxHRrange;
    }
    public void setCalibrate (double cl) {
        calibrate = cl;
    }
    public double getCalibrate () {
        return calibrate;
    }
    public void setPeakRange (int pk) {
        peakRange = pk;
    }
    public int getPeakRange () {
        return peakRange;
    }
    public void setFrequencyRange (int freq) {
        freqRange = freq;
    }
    public int getFrequencyRange () {
        return freqRange;
    }
    public void setTimePeriod (double tp) {
        timePeriod = tp;
    }
    public double getTimePeriod () {
        return timePeriod;
    }
    
    protected List<Features> interpolate (List<Features> ds) {
        // Create time array
        double [] ts = new double [ds.size()];
        for (int index = 0; index < ts.length; index++) {
            ts [index] = ds.get(index).getTime();
        }
        double startTime = ts [0];
        double endTime = ts [ts.length - 1];
        // Extract channels
        List<double []> channels = DatasetUtils.toChannelArrays(ds);
        double [] red = channels.get(0);
        double [] green = channels.get(1);
        double [] blue = channels.get(2);
        // Apply Interpolation
        LinkedList<Features> results = new LinkedList<>();
        UnivariateFunction ufred = interpolator.interpolate(ts, red);
        UnivariateFunction ufgreen = interpolator.interpolate(ts, green);
        UnivariateFunction ufblue = interpolator.interpolate(ts, blue);
        for (double tindex = startTime; tindex <= endTime; tindex += timePeriod) {
            double [] chs = new double [3];
            chs [0] = ufred.value(tindex);
            chs [1] = ufgreen.value(tindex);
            chs [2] = ufblue.value(tindex);
            results.add(new Features ((long) tindex, null, chs));
        }
        
        return results;
    }
    protected List<Features> normalize (List<Features> ds) {
        List<double []> channels = DatasetUtils.toChannelArrays(ds);
        double [] red = channels.get(0);
        double [] green = channels.get(1);
        double [] blue = channels.get(2);
        
        LinkedList<Features> results = new LinkedList<>();
        DescriptiveStatistics dsred = new DescriptiveStatistics(red);
        double meanRed = dsred.getMean();
        double stdRed = dsred.getStandardDeviation();
        DescriptiveStatistics dsgreen = new DescriptiveStatistics(green);
        double meanGreen = dsgreen.getMean();
        double stdGreen = dsgreen.getStandardDeviation();
        DescriptiveStatistics dsblue = new DescriptiveStatistics(blue);
        double meanBlue = dsblue.getMean();
        double stdBlue = dsblue.getStandardDeviation();
        for (int index = 0; index < red.length; index++) {
            double [] chs = new double [3];
            chs [0] = (red [index] - meanRed) / stdRed;
            chs [1] = (green [index] - meanGreen) / stdGreen;
            chs [2] = (blue [index] - meanBlue) / stdBlue;
            results.add(new Features (ds.get(index).getTime(), null, chs));
        }
        return results;
    }
//    protected double [] fft (List<RealVector> ds) {
//        // Extract channels
//        List<double []> data = DatasetUtils.toChannelArrays(ds);
//        // Apply FFT
//        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
//        LinkedList<Complex []> lres = new LinkedList<>();
//        for (int nch = 0; nch < data.size(); nch++) {
//            double [] ddata = data.get(nch);
//            double [] dd = new double [freqRange];
//            System.arraycopy (ddata, 0, dd, 0, Math.min(dd.length, ddata.length));
//            Complex [] cd = fft.transform(dd, TransformType.FORWARD);
//            lres.add(cd);
//        }
//        double [] res = new double [lres.get(0).length];
//        for (int index = 0; index < res.length; index++) {
//            for (int ch = 0; ch < lres.size(); ch++) {
//                res [index] += lres.get(ch) [index].abs();
//            }
//        }
//        return res;
//    }
    protected double [] fft (List<RealVector> ds) {
        // Extract channels
        List<double []> data = DatasetUtils.toChannelArrays(ds);
        // Apply FFT
        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
        LinkedList<Complex []> lres = new LinkedList<>();
        for (int nch = 0; nch < data.size(); nch++) {
            double [] ddata = data.get(nch);
            double [] dd = new double [freqRange];
            System.arraycopy (ddata, 0, dd, 0, Math.min(dd.length, ddata.length));
            Complex [] cd = fft.transform(dd, TransformType.FORWARD);
            lres.add(cd);
        }
        double [] res = new double [lres.get(0).length];
        for (int index = 0; index < res.length; index++) {
            for (int ch = 0; ch < lres.size(); ch++) {
                res [index] += lres.get(ch) [index].abs();
            }
        }
        return res;
    }
    protected RealVector peakpoints (RealVector data) {
        // Extract peak points
        LinkedList<Integer> localMax = new LinkedList<>();
        for (int index = 1; index < data.getDimension() - 1; index++) {
            double p1 = data.getEntry(index - 1);
            double p2 = data.getEntry(index);
            double p3 = data.getEntry(index + 1);
            double m12 = p2 - p1;
            double m23 = p3 - p2;
            if ((m12 * m23) <= 0 && p1 < p2) {
                localMax.add(index);
            }
        }
        
        double [] res = new double [data.getDimension()];
        int sindex = 0;
        for (int index : localMax) {
            if ((index - sindex) >= peakRange) {
                res [index] = data.getEntry(index);
                sindex = index;
            }
        }   
        return new ArrayRealVector (res);
    }
    protected RealVector convert (RealVector freq) {
        LinkedList<HElement> els = new LinkedList<>();
        for (int index = 0; index < freq.getDimension(); index++) {
            if (freq.getEntry(index) != 0) {
                HElement he = new HElement();
                he.freq = freq.getEntry(index);
                he.index = index;
                els.add(he);
            }
        }
        
        Collections.sort (els);
        double [] res = new double [els.size ()];
        for (int index = 0; index < res.length; index++) {
            res [index] = els.get (res.length - index - 1).index; // index
        }
        return new ArrayRealVector (res);
    }
    protected RealVector calculate (RealVector freq) {
        double [] res = new double [freq.getDimension()];
        double factor = (60.0 * timePeriod) / freqRange;
        for (int index = 0; index < freq.getDimension(); index++) {
            res [index] = (factor * (freq.getEntry(index))) + calibrate;
        }
        return new ArrayRealVector (res);
    }
    protected RealVector finalprocess (RealVector data) {
        LinkedList<Double> restmp = new LinkedList<>();
        for (int index = 0; index < data.getDimension(); index++) {
            if (data.getEntry(index) >= minHRrange && data.getEntry(index) <= maxHRrange) {
                restmp.add (data.getEntry(index));
            }
        }
        double [] res = new double [restmp.size()];
        for (int index = 0; index < res.length; index++) {
            res [index] = restmp.get (index);
        }
        return new ArrayRealVector(res);
    }
    
    @Override
    public HeartrateInfo estimate (Collection<? extends Features> dss) {
        LinkedList<Features> ds = new LinkedList<>(dss);
        if (ds.size() <= 0) {
            return null;
        }
        // Data Interpolation
        List<Features> interds = interpolate (ds);
        // Data Normalization
        List<Features> normds = normalize (interds);
        // Independent Components Analysis
        List<RealVector> icads = icaFilter.filter (normds);
        // Fast Fourier Transformation
        RealVector resfft = new ArrayRealVector(fft (icads));
        // Extract peak points
        RealVector respk = peakpoints(resfft);
        // Convert to spatial domain
        RealVector resconv = convert(respk);
        // Calculate heart rate
        RealVector rescal = calculate (resconv);
        // Final step
        RealVector reshr = finalprocess(rescal);
        ///////////
        v1HeartrateInfo info = new v1HeartrateInfo();
        info.setStartTime (ds.getFirst().getTime());
        info.setEndTime(ds.getLast().getTime());
        info.setHeartrate(reshr.getDimension() > 0 ? reshr.getEntry(0) : 0.0);
        ArrayList<Double> phr = new ArrayList<>();
        phr.add(reshr.getDimension() > 0 ? reshr.getEntry(0) : 0.0);
        info.setPossibleHeartrate(phr);
        info.setConfidence(1.0);
        info.setValid(reshr.getDimension() > 0);
        info.data = new LinkedList<>(dss);
        info.interpolate = interds;
        info.normalize = normds;
        info.ica = icads;
        info.fft = resfft;
        info.convert = resconv;
        info.calculate = rescal;
        info.peakpoints = respk;
        info.finalprocess = reshr;
        return info;
    }
    
    protected class HElement implements Comparable<HElement> {
        public double freq = 0;
        public double index = 0;

        @Override
        public int compareTo (HElement o) {
            return Double.compare(this.freq, o.freq);
        }
    }
}
