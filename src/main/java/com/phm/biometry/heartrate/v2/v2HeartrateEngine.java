
package com.phm.biometry.heartrate.v2;

import com.phm.biometry.heartrate.HeartrateEngine;
import com.phm.biometry.heartrate.HeartrateInfo;
import com.phm.biometry.heartrate.fusion.AverageHeartrateChannelFusion;
import com.phm.biometry.heartrate.fusion.HeartrateChannelFusion;
import com.phm.core.data.DatasetUtils;
import com.phm.comp.filter.DatasetFilter;
import com.phm.comp.filter.ica.RecursiveICA;
import com.phm.core.data.Features; 
import com.phm.core.data.Instance;
import com.phm.core.data.converter.InstanceConverter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedDeque;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
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
public class v2HeartrateEngine implements HeartrateEngine {
    
    protected UnivariateInterpolator interpolator = new LinearInterpolator();
    protected DatasetFilter icaFilter = new RecursiveICA();
    protected double timePeriod = 10;
    protected int freqRange = 16384; //10000; 8192
    protected int peakRange = 5;
    protected double calibrate = 6;
    protected double minHRrange = 65; //50
    protected double maxHRrange = 130; //200
    protected HeartrateChannelFusion fusionObj;

    public v2HeartrateEngine (UnivariateInterpolator ui, DatasetFilter ica, HeartrateChannelFusion hcf) {
        interpolator = ui;
        icaFilter = ica;
        fusionObj = hcf;
    }
    public v2HeartrateEngine (UnivariateInterpolator ui, DatasetFilter ica) {
        this (ui, ica, new AverageHeartrateChannelFusion());
    }
    public v2HeartrateEngine (UnivariateInterpolator ui) {
        this (ui, new RecursiveICA());
    }
    public v2HeartrateEngine (DatasetFilter ica) {
        this (new SplineInterpolator(), ica);
    }
    public v2HeartrateEngine () {
        this (new SplineInterpolator(), new RecursiveICA());
    }
    public void setChannelFusion (HeartrateChannelFusion hcf) {
        fusionObj = Objects.requireNonNull(hcf);
    }
    public HeartrateChannelFusion getChannelFusion () {
        return fusionObj;
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
        for (int index = 0; index < ds.size(); index++) {
            ts [index] = ds.get(index).getTime();
        }
        double startTime = ts [0];
        double endTime = ts [ts.length - 1];
        // Extract channels
        List<double []> channels = DatasetUtils.toChannelArrays(ds);
        // Apply Interpolation
        LinkedList<Features> results = new LinkedList<>();
        LinkedList<UnivariateFunction> uflist = new LinkedList<>();
        for (int ch = 0; ch < channels.size(); ch++) {
            uflist.add(interpolator.interpolate(ts, channels.get(ch)));
        }
        
        for (double tindex = startTime; tindex <= endTime; tindex += timePeriod) {
            double [] chs = new double [channels.size()];
            for (int ch = 0; ch < channels.size(); ch++) {
                chs [ch] = uflist.get(ch).value(tindex);
            }
            results.add(new Features ((long) tindex, null, chs));
        }
        
        return results;
    }
    protected List<Features> normalize (List<Features> ds) {
        LinkedList<Features> result = new LinkedList<>();
        ds.stream().forEach((x) -> {
            DescriptiveStatistics dsfunc = new DescriptiveStatistics (x.toArray());
            double mean = dsfunc.getMean();
            double std = dsfunc.getStandardDeviation();
            RealVector res = x.mapSubtract(mean).mapDivide(std);
            result.addLast(new Features(x.getTime(), x.getLabel(), res));
        });
        return result;
    }
    protected RealVector fft (RealVector ds) {
        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
        double [] ddata = ds.toArray();
        double [] dd = new double[freqRange];
        System.arraycopy (ddata, 0, dd, 0, Math.min(dd.length, ddata.length));
        Complex [] cd = fft.transform(dd, TransformType.FORWARD);
        double [] chs = new double[cd.length];
        for (int index = 0; index < chs.length; index++) {
            chs [index] = cd [index].abs();
        }
        
        return new ArrayRealVector(chs);
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
//        List<Features> normds = new LinkedList<>(interds);
        // Independent Components Analysis
        final List<RealVector> icads = icaFilter.filter (normds);
        final List<RealVector> icarows = DatasetUtils.toChannelRows(icads);

        final ConcurrentLinkedDeque<RealVector> fftds = new ConcurrentLinkedDeque ();
        final ConcurrentLinkedDeque<RealVector> peakpointsds = new ConcurrentLinkedDeque<>();
        final ConcurrentLinkedDeque<RealVector> convertds = new ConcurrentLinkedDeque<>();
        final ConcurrentLinkedDeque<RealVector> calculateds = new ConcurrentLinkedDeque<>();
        final ConcurrentLinkedDeque<RealVector> finalprocessds = new ConcurrentLinkedDeque<>();
        final ConcurrentLinkedDeque<Double> possibleHR = new ConcurrentLinkedDeque<> ();
        
        icarows.stream().parallel().forEach((RealVector ica) -> {
            // Fast Fourier Transformation
            RealVector resfft = fft (ica);
            // Extract peak points
            RealVector respk = peakpoints(resfft);
            // Convert to spatial domain
            RealVector resconv = convert(respk);
            // Calculate heart rate
            RealVector rescal = calculate (resconv);
            // Final step
            RealVector reshr = finalprocess(rescal);
            ///////////
            fftds.add(resfft);
            peakpointsds.add(respk);
            convertds.add(resconv);
            calculateds.add(rescal);
            finalprocessds.add(reshr);
            if (reshr.getDimension() > 0) {
                possibleHR.add(reshr.getEntry(0));
            }
        });
        
        v2HeartrateInfo info = new v2HeartrateInfo();
        info.setStartTime (ds.getFirst().getTime());
        info.setEndTime(ds.getLast().getTime());
        info.setPossibleHeartrate(new LinkedList<>(possibleHR));
        info.setConfidence(1.0);
        info.setValid(true);
        info.data = new LinkedList<>(dss);
        info.interpolate = interds;
        info.normalize = normds;
        info.ica = icads;
        info.fft = new LinkedList<>(fftds);
        info.convert = new LinkedList<>(convertds);
        info.calculate = new LinkedList<>(calculateds);
        info.peakpoints = new LinkedList<>(peakpointsds);
        info.finalprocess = new LinkedList<>(finalprocessds);
        
        // Channel Fusion
        fusionObj.fuse(info);
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
    
    public static class HRInfo {
        public RealVector ica = null;
        public RealVector fft = null;
        public RealVector peakpoints = null;
        public RealVector convert = null;
        public RealVector calculate = null;
        public RealVector finalprocess = null;
        public double start = 0;
        public double end = 0;
        public double heartrate = 0.0f;
        public double confidence = 1.0;
        public ArrayList<Double> possibleHeartrates;
        public boolean validObj = false;
    }
}
