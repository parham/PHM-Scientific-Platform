
package com.phm.biometry.heartrate.fusion;

import com.google.common.collect.EvictingQueue;
import com.phm.biometry.heartrate.HeartrateInfo;
import com.phm.comp.regression.AutoRegression;
import com.phm.comp.regression.Lin2011AutoRegression;
import com.phm.core.data.Features;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

/**
 *
 * @author phm
 */
public class phmHeartrateChannelFusion implements HeartrateChannelFusion {
    
    public static final int DEFAULT_BUFFER_SIZE = 20;
    
    protected int kmax = 4;
    protected int nchannel = 3;
    protected double previousHeartrate = 80.0;
    protected double previousVariance = 30.0;
    protected double previousConfident = 1.0;
    protected double thresholdValue = 0.005;
    protected AutoRegression regressionObj = new Lin2011AutoRegression(5);
    protected EvictingQueue<Double> memoryObj;

    public phmHeartrateChannelFusion (int maxmem) {
        memoryObj = EvictingQueue.create(maxmem);
    }
    public phmHeartrateChannelFusion () {
        this (DEFAULT_BUFFER_SIZE);
    }
    
    public void setAutoRegression (AutoRegression ar) {
        regressionObj = ar;
    }
    public AutoRegression getAutoRegression () {
        return regressionObj;
    }
    public void setConfidenceThreshold (double thresh) {
        thresholdValue = thresh;
    }
    public double getConfidenceThreshold () {
        return thresholdValue;
    }
    public void setKMax (int k) {
        kmax = k;
    }
    public int getKMAX () {
        return kmax;
    }
    public void setNumberOfChannels (int nch) {
        nchannel = nch;
    }
    public int getNumberOfChannels () {
        return nchannel;
    }
    
    @Override
    public HeartrateInfo fuse(HeartrateInfo info) {
        List<Double> hrs = info.getPossibleHeartrate();
        if (hrs.size() < 1) {
            return info;
        }
        // Confidence factor calculation
        NormalDistribution confideceFunc = new NormalDistribution (previousHeartrate, previousVariance);
        StandardDeviation ustd = new StandardDeviation();
        
        LinkedList<HRItem> conflist = new LinkedList<>();
        for (double x : hrs) {
            HRItem tmp = new HRItem (x, confideceFunc.density(x));
            conflist.add(tmp);
            ustd.increment(x);
            System.out.print (tmp.heartrate + " : " + tmp.confidence + "\t");
        }
        // Find the 8th items with the highest confidence factor
        Collections.sort (conflist);
        if (kmax < conflist.size()) {
            conflist = new LinkedList<>(conflist.subList(conflist.size() - kmax, conflist.size()));
        }
//        int startIndex = conflist.size() > kmax ? conflist.size() - kmax : conflist.size() - 1;
//        conflist = new LinkedList<>(conflist.subList(startIndex, conflist.size()));
        // Calculate the mean of m heart rate with maximum confidence factor
        double tmp = 0;
        for (int index = 0; index < conflist.size(); index++) {
            tmp += conflist.get(index).heartrate;
        }
        tmp /= conflist.size();
        HRItem result = new HRItem(tmp, confideceFunc.density(tmp));
//        HRItem result = conflist.getLast();
        // Memory Update
        previousConfident = result.confidence;
        previousHeartrate = result.heartrate;
        // Update the standard deviation state
        previousVariance = ustd.getResult() > 10 ? ustd.getResult() : 
                                confideceFunc.getStandardDeviation();
        ///////////////////////////////////////
        info.setHeartrate(result.heartrate);
        info.setConfidence(result.confidence);
        if (memoryObj.size() > 0) {
            // Data Regression
            LinkedList<Features> fus = new LinkedList<>();
            memoryObj.stream().forEach((Double x) -> {
                double [] arr = {x};
                fus.add(new Features (arr));
            });
            // Add the current heart rate 
            memoryObj.add(result.heartrate);
            ///////////
            RealVector ent = regressionObj.estimate(fus);
            if (ent == null) {
                info.setHeartrate(result.heartrate);
                info.setConfidence(confideceFunc.density(result.heartrate));
            }
        }
        info.setValid(info.getConfidence() >= thresholdValue);
        info.setPossibleHeartrate (hrs);
        return info;
    }
    
    protected class HRItem implements Comparable<HRItem> {
        public double heartrate = 0.0;
        public double confidence = 0.0;
        
        public HRItem (double hr, double cf) {
            heartrate = hr;
            confidence = cf;
        }

        @Override
        public int compareTo(HRItem o) {
            return (int) (this.confidence - o.confidence);
        }
    }
}
