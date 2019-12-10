
package com.phm.biometry.heartrate.v3;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import com.phm.biometry.heartrate.HeartrateEngine;
import com.phm.biometry.heartrate.HeartrateInfo;
import com.phm.biometry.heartrate.fusion.HeartrateChannelFusion;
import com.phm.biometry.heartrate.fusion.phmHeartrateChannelFusion;
import com.phm.comp.filter.DatasetFilter;
import com.phm.comp.filter.VectorFilter;
import com.phm.comp.filter.detrend.FlindersDetrendFilter;
import com.phm.comp.filter.fir.FiniteImpulseResponseFilter;
import com.phm.comp.filter.ica.RecursiveICA;
import com.phm.comp.window.RectangularWindow;
import com.phm.core.data.DatasetUtils;
import com.phm.core.data.Features;

import flanagan.math.FourierTransform;

/**
 *
 * @author Parham Nooralishahi - PHM!
 */
public class v3HeartrateEngine implements HeartrateEngine {

    protected UnivariateInterpolator interpolatorObj = new SplineInterpolator();
    protected DatasetFilter icaFilter = new RecursiveICA();
    protected VectorFilter detrendingFilter = new FlindersDetrendFilter (1);
    protected VectorFilter noiseFilter = new FiniteImpulseResponseFilter (new RectangularWindow(3, 1.0/3.0));
    protected VectorFilter hammingFilter = null;
    protected double timePeriod = 20;
    protected int freqRange = 16384;
    protected double lowBound = 50;
    protected double highBound = 150;
    protected HeartrateChannelFusion fusionObj = new phmHeartrateChannelFusion();
    
    public v3HeartrateEngine () {
        // Empty body
    }
    
    public void setChannelFusion (HeartrateChannelFusion hcf) {
        fusionObj = Objects.requireNonNull(hcf);
    }
    public HeartrateChannelFusion getChannelFusion () {
        return fusionObj;
    }
    public void setMinimumHeartrate (double hr) {
        lowBound = hr;
    }
    public double getMinimumHeartrate () {
        return lowBound;
    }
    public void setMaximumHeartrate (double hr) {
        highBound = hr;
    }
    public double getMaximumHeartrate () {
        return highBound;
    }
    public void setFrequencyRange (int fr) {
        freqRange = fr;
    }
    public int getFrequencyRange () {
        return freqRange;
    }
    public void setNoiseFilter (VectorFilter sf) {
        noiseFilter = sf;
    }
    public VectorFilter getNoiseFilter () {
        return noiseFilter;
    }
    public void setDetrendingFilter (VectorFilter sf) {
        detrendingFilter = sf;
    }
    public VectorFilter getDetrendingFilter () {
        return detrendingFilter;
    }
    public void setICA (DatasetFilter dsf) {
        icaFilter = dsf;
    }
    public DatasetFilter getICA () {
        return icaFilter;
    }
    public void setInterporator (UnivariateInterpolator inter) {
        interpolatorObj = inter;
    }
    public UnivariateInterpolator getInterpolator () {
        return interpolatorObj;
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
        // Apply Interpolation
        LinkedList<Features> results = new LinkedList<>();
        LinkedList<UnivariateFunction> uflist = new LinkedList<>();
        for (int ch = 0; ch < channels.size(); ch++) {
            uflist.add(interpolatorObj.interpolate(ts, channels.get(ch)));
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
//    protected RealVector reduceNoise (RealVector vec) {
//        return noiseFilter.filter(vec);
//    }
    
    protected RealVector fft (RealVector ds) {
        // Extract channels
        double [] data = ds.toArray();
        // Apply FFT
        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
        
        double [] dd = new double [freqRange];
        System.arraycopy (data, 0, dd, 0, Math.min(dd.length, data.length));
        Complex [] lres = fft.transform(dd, TransformType.FORWARD);
        double [] res = new double [lres.length];
        for (int index = 0; index < res.length; index++) {
            res [index] = lres [index].abs();
        }
        return new ArrayRealVector (res);
    }

    protected double [] welch (RealVector data, double [][] welch) {
        int arrlen = FourierTransform.calcDataLength(true, freqRange, 3); // data.getDimension()
        double [] dd = new double [arrlen];
        System.arraycopy (data.toArray(), 0, dd, 0, Math.min(dd.length, data.getDimension()));
        FourierTransform fft = new FourierTransform (dd);
        fft.setDeltaT(timePeriod);
//        fft.setSegmentNumber(3);
        fft.setOverlapOption(true);
        fft.setSegmentLength(data.getDimension());
        fft.setSegmentNumber(3);
        fft.setWelch();
        double [][] res = fft.powerSpectrum();
//        fft.plotPowerSpectrum();
        int len = res[0].length;
        double mindex = res [0][0];
        double mvalue = res [1][0];
        double [] resvalue = new double [2];
        if (len > 1) {
            LinkedList<FreqItem> freqcandid = new LinkedList<>();
            for (int index = 0; index < len; index++) {
//                double newfreq = res[0][index] * 60;
                double newfreq = res[0][index] * (60 * timePeriod) / arrlen;
//                double newfreq = res[0][index] != 0 ? 1 / res[0][index] : 0.0;
                if (newfreq >= getMinimumHeartrate()&& newfreq <= getMaximumHeartrate()) {
                    freqcandid.add(new FreqItem(res[0][index], newfreq, res [1][index]));
                }
            }
            if (freqcandid.size() > 0) {
                FreqItem item = Collections.max(freqcandid);
                mindex = item.value;
                mvalue = item.power;
            }
        }
        resvalue [0] = mindex;
        resvalue [1] = mvalue;
        
        welch = res;
        return resvalue;
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
        List<RealVector> icarows = DatasetUtils.toChannelRows(icads);
        /////
        LinkedList<Double> hres = new LinkedList<>();
        LinkedList<RealVector> detrendingds = new LinkedList<>();
        LinkedList<RealVector> noiseds = new LinkedList<>();
        LinkedList<RealVector> smoothds = new LinkedList<>();
        
        double avrg = 0;
        double [][] reswelch = null;
        for (RealVector ica : icarows) {
            // Detrending the signal
            RealVector vectrend = detrendingFilter.filter (ica);
            detrendingds.add(vectrend);
            // Noise Reduction
            RealVector vecsmooth = noiseFilter.filter (vectrend);
            noiseds.add(vecsmooth);
            // Hamming boundpass filter
            RealVector vechamming = vecsmooth.copy();
            smoothds.add(vechamming);
            // Welch filter
            
            double [] hrtmp = welch (vechamming, reswelch);
            double hr = hrtmp [0];
            hres.add (hr);
            avrg += hr;
        }
        avrg /= hres.size();
        
        // Heartrate information
        v3HeartrateInfo hinfo = new v3HeartrateInfo();
        hinfo.setPossibleHeartrate(hres);
        hinfo.setHeartrate(avrg);
        hinfo.setStartTime(ds.getFirst().getTime());
        hinfo.setEndTime(ds.getLast().getTime());
        hinfo.setValid(true);
        hinfo.setConfidence(1.0);
        hinfo.detrending = detrendingds;
        hinfo.ica = icads;
//        hinfo.interpolate = interds;
        hinfo.noiseFilter = noiseds;
//        hinfo.normalize = normds;
        hinfo.smooth = smoothds;
        
        LinkedList<RealVector> lwelch = new LinkedList<>();
        for (int row = 0; row < reswelch.length; row++) {
            RealVector tmp = new ArrayRealVector(reswelch[row]);
            lwelch.add(tmp);
        }
        hinfo.welch = lwelch;
        // Channel Fusion
        fusionObj.fuse(hinfo);
        return hinfo;
    }
    protected class FreqItem implements Comparable<FreqItem> {
        public double freq = 0.0;
        public double value = 0.0;
        public double power = 0.0;
        
        public FreqItem (double f, double v, double p) {
            freq = f;
            power = p;
            value = v;
        }

        @Override
        public int compareTo(FreqItem o) {
            return (int) (this.power - o.power);
        }
    }
}
