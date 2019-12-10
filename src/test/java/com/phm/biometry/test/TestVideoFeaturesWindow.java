
package com.phm.biometry.test;

import com.phm.biometry.heartrate.v2.v2HeartrateEngine;
import com.phm.biometry.heartrate.v2.v2HeartrateInfo;
import com.phm.comp.filter.ica.RecursiveICA;
import com.phm.core.ds.DataStream; 
import com.phm.core.data.Features;
import com.phm.core.data.InstanceList;
import com.phm.core.data.converter.WindowInstanceToListConverter;
import com.phm.core.terminal.CSVFeaturesSource;
import com.phm.io.CSVFileVectorWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.openimaj.video.capture.VideoCaptureException;

/**
 *
 * @author phm
 */
public class TestVideoFeaturesWindow {

    /**
     * @param args the command line arguments
     * @throws org.openimaj.video.capture.VideoCaptureException
     */
    public static void main(String[] args) throws VideoCaptureException, IOException {
//        File path = new File("/home/phm/s02_trial01.avi.csv");
//        String fname = "s02_trial01";
//        CSVFeaturesSource fsource = new CSVFeaturesSource(path, CSVFormat.TDF, Charset.defaultCharset());
//        fsource.start();
//        DataStream<Features> ds = (DataStream<Features>) fsource.stream(CSVFeaturesSource.FEATURES_STREAM);
//        
//        DataStreamConverter<Features, InstanceList<Features>> dscwin = 
//            new DataStreamConverter (ds, new WindowInstanceToListConverter (150, 0.1f));
//
//        int index = 0;
//        v2HeartrateEngine hr = new v2HeartrateEngine(new SplineInterpolator(), new RecursiveICA());
//        LinkedList<RealVector> res = new LinkedList<>();
//        while (dscwin.hasMore()) {
//            InstanceList<Features> tmp = dscwin.next();
//            if (tmp != null && tmp.size() > 0) {
//                v2HeartrateInfo hinfo = (v2HeartrateInfo) hr.estimate(tmp);
//                if (hinfo != null) {
//                    double [] dd = {hinfo.getStartTime(),
//                                    hinfo.getHeartrate(),
//                                    hinfo.getConfidence()};
//                    res.add(new ArrayRealVector(dd));
//                    System.out.println (hinfo.getStartTime() + " " +
//                                        hinfo.getHeartrate() + " " +
//                                        hinfo.getConfidence());
//                }
//            }
//        }
//        
//        CSVFileVectorWriter csvw = new CSVFileVectorWriter (
//                                   new File("/home/phm/experiment/", "hr_" + fname + ".csv"), CSVFormat.TDF);
//        csvw.write(res);
    }

}
