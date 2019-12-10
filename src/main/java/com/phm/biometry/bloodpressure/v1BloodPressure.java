
package com.phm.biometry.bloodpressure;

import com.phm.comp.filter.HilbertTransformationFilter;
import com.phm.core.data.DatasetUtils;
import com.phm.core.data.Features;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author phm
 */
public class v1BloodPressure implements BloodPressureEngine {

    protected UnivariateInterpolator interpolator = new SplineInterpolator();
    protected long timePeriod = 25;
    protected int nchannel = 3;
    protected int freqRange = 8192;
    
    public void setFrequencyRange (int freq) {
        freqRange = freq;
    }
    public int getFrequencyRange () {
        return freqRange;
    }
    public void setInterpolator (UnivariateInterpolator intr) {
        interpolator = intr;
    }
    public UnivariateInterpolator getInterpolator () {
        return interpolator;
    }
    public void setNumberChannel (int nch) {
        nchannel = nch;
    }
    public int getNumberChannel () {
        return nchannel;
    }
    
    public RealVector interpolate (List<Features> ds) {
        if (ds.size() <= 0) {
            return null;
        }
        // Create time array
        double [] ts = new double [ds.size()];
        for (int index = 0; index < ts.length; index++) {
            ts [index] = ds.get(index).getTime();
        }
        double startTime = ts [0];
        double endTime = ts [ts.length - 1];
        // Extract channels
        List<double []> channels = DatasetUtils.toChannelArrays(ds);
        double [] green = channels.get(1);
        // Apply Interpolation
        List<Double> res = new LinkedList<>();
        UnivariateFunction ufgreen = interpolator.interpolate(ts, green);
        for (double tindex = startTime; tindex <= endTime; tindex += timePeriod) {
            res.add(ufgreen.value(tindex));
        }
        RealVector results = new ArrayRealVector(res.size());
        for (int index = 0; index < res.size(); index++) {
            results.setEntry(index, res.get(index));
        }
        return results;
    }
    
    public List<List<Features>> segment (List<Features> ds) {
        int numdim = ds.get(0).getDimension() / nchannel;
        ArrayList<List<Features>> segds = new ArrayList<>(nchannel);
        for (int index = 0; index < nchannel; index++) {
            segds.add(new LinkedList<>());
        }
        for (int index = 0; index < ds.size(); index++) {
            Features ent = ds.get(index);
            for (int ch = 0; ch < nchannel; ch++) {
                RealVector vec = ent.getSubVector(ch * numdim, numdim);
                segds.get(ch).add(new Features (ent.getTime(), ent.getLabel(), vec));
            }
        }
        return segds;
    }
    
    public RealVector derivative (double [] data) {
        double [] dr = new double [data.length];
        for (int index = 0; index < dr.length - 1; index++) {
            double d0 = data [index];
            double d1 = data [index + 1];
            dr [index] = (d1 - d0) / (double) timePeriod;
        }
        return new ArrayRealVector(dr);
    }
    
    @Override
    public BloodPressureInfo estimate(Collection<? extends Features> ds) {
        // Segment data
        List<List<Features>> segdata = segment (new LinkedList<>(ds));
        List<Complex []> complexs = new LinkedList<>();
        for (List<Features> x : segdata) {
            // Interpolate data
            RealVector intr = interpolate (x);
            // Hilbert Transformation Filter
            HilbertTransformationFilter htf = new HilbertTransformationFilter (freqRange);
            Complex [] htfdata = htf.filterAsComplex(intr, freqRange);
            complexs.add(htfdata);
        }
        // PTT
        Complex [] seg0 = complexs.get(0);
        Complex [] seg1 = complexs.get(1);
        double [] ptt = new double[seg0.length];
        for (int index = 0; index < seg0.length; index++) {
            Complex tmp = seg0[index].subtract(seg1[index]);
            double tmpv = tmp.getImaginary() / tmp.getReal();
            ptt [index] = Math.atan(tmpv);
//            ptt [index] = seg0[index].subtract(seg1[index]).atan().abs();
        }
        RealVector bp = derivative(ptt);
        BloodPressureInfo info = new BloodPressureInfo();
        info.ptt = ptt;
        return info;
    }
    
}
