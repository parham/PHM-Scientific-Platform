
package com.phm.biometry.heartrate.v4;

import com.google.common.collect.EvictingQueue;
import com.phm.biometry.heartrate.HeartrateEngine;
import com.phm.biometry.heartrate.HeartrateInfo;
import com.phm.comp.regression.AutoRegression;
import com.phm.comp.regression.Lin2011AutoRegression;
import com.phm.core.data.Features;
import com.phm.io.CSVFileDatasetWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

/**
 *
 * @author PHM
 */
public class v4HeartrateEngine implements HeartrateEngine {
    
    protected HeartrateEngine heartObj;
    protected int kmax = 4;
    protected int nchannel = 3;
    protected double previousHeartrate = 80.0;
    protected double previousVariance = 30.0;
    protected double previousConfident = 1.0;
    protected double thresholdValue = 0.5;
    protected AutoRegression regressionObj = new Lin2011AutoRegression(5);
    protected EvictingQueue<Double> memoryObj;
    
    public v4HeartrateEngine (HeartrateEngine hr, int k, int nch, double thresh, int maxmem) {
        heartObj = hr;
        kmax = k;
        nchannel = nch;
        thresholdValue = thresh;
        memoryObj = EvictingQueue.create(maxmem);
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
    public void setHeartrateEngine (HeartrateEngine hre) {
        heartObj = hre;
    }
    public HeartrateEngine getHeartrateEngine () {
        return heartObj;
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
    
    public HeartrateInfo fusion (List<HeartrateInfo> hrlist) {
        // List possible heart rates
        LinkedList<Double> hrvs = new LinkedList<>();
        hrlist.stream().forEach((HeartrateInfo x) -> {
            hrvs.addAll(x.getPossibleHeartrate());
        });
        // Confidence factor calculation
        NormalDistribution confideceFunc = new NormalDistribution (previousHeartrate, previousVariance);
        StandardDeviation ustd = new StandardDeviation();
        
        LinkedList<HRItem> conflist = new LinkedList<>();
        for (double x : hrvs) {
            HRItem tmp = new HRItem (x, confideceFunc.density(x));
            conflist.add(tmp);
            ustd.increment(x);
            System.out.print (tmp.heartrate + " : " + tmp.confidence + "\t");
        }
        System.out.println ();
        // Find the 8th items with the highest confidence factor
        Collections.sort (conflist);
        int startIndex = conflist.size() > kmax ? conflist.size() - kmax : conflist.size() - 1;
        conflist = new LinkedList<>(conflist.subList(startIndex, conflist.size()));
        // Find the heart rate with maximum confidence factor
        HRItem result = conflist.getLast();
        // Memory Update
        previousConfident = result.confidence;
        previousHeartrate = result.heartrate;
        // Update the standard deviation state
        previousVariance = ustd.getResult() > 10 ? ustd.getResult() : 
                                confideceFunc.getStandardDeviation();
        ///////////////////////////////////////
        HeartrateInfo hinfo = new HeartrateInfo();
        hinfo.setHeartrate(result.heartrate);
        hinfo.setConfidence(result.confidence);
        
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
                double [] arr = {result.heartrate};
                ent = new Features (arr);
                hinfo.setHeartrate(ent.getEntry(0));
                hinfo.setConfidence(confideceFunc.density(ent.getEntry(0)));
            }
        }
        
        hinfo.setValid(hinfo.getConfidence() >= thresholdValue);
        hinfo.setStartTime(hrlist.get(0).getStartTime());
        hinfo.setEndTime(hrlist.get(0).getEndTime());
        hinfo.setPossibleHeartrate (hrvs);
        return hinfo;
    }
    
    @Override
    public HeartrateInfo estimate (Collection<? extends Features> dss) {
        try {
            LinkedList<Features> ds = new LinkedList<>(dss);
            if (ds.size() < 1) {
                return new HeartrateInfo (0, 0, 0);
            }
            String ext = this.getClass().getName() + "__" + String.valueOf (System.currentTimeMillis()) + "__";
            // Save dataset
            CSVFileDatasetWriter csvwrite0 = new CSVFileDatasetWriter (new File(ext + "data.csv"), CSVFormat.EXCEL);
            csvwrite0.write(ds);
            // Data Segmentation
            List<List<Features>> segds = segment(ds);
            LinkedList<HeartrateInfo> hrres = new LinkedList<>();
            segds.stream().forEach((List<Features> tds) -> {
                HeartrateInfo info = heartObj.estimate(tds);
                hrres.add(info);
            });
            
            HeartrateInfo resinfo = fusion (hrres);
            return resinfo;
        } catch (IOException ex) {
            Logger.getLogger(v4HeartrateEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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
