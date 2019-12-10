
package com.phm.biometry.test;

import com.phm.biometry.core.ImageInstance;
import com.phm.biometry.core.ImageListToFeaturesListConverter;
import com.phm.biometry.core.VideoSource;
import com.phm.biometry.core.ImageToStatisticalFeaturesConverter;
import com.phm.biometry.face.CLMInstance; 
import com.phm.biometry.face.CLMToFacialRegionsConverter;
import com.phm.biometry.face.ImageToCLMConverter;
import com.phm.biometry.heartrate.v2.v2HeartrateEngine;
import com.phm.biometry.heartrate.v2.v2HeartrateInfo;
import com.phm.core.data.converter.FeaturesListToFeaturesConverter;
import com.phm.core.ds.DataStream; 
import com.phm.core.data.Features;
import com.phm.core.data.InstanceList;
import com.phm.io.CSVFileVectorWriter;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.openimaj.video.capture.VideoCaptureException;
import org.openimaj.video.xuggle.XuggleVideo;

/**
 *
 * @author phm
 */
public class TestVideoHR3 {

    /**
     * @param args the command line arguments
     * @throws org.openimaj.video.capture.VideoCaptureException
     */
    public static void main(String[] args) throws VideoCaptureException, IOException {
////        XuggleVideo xvs = new XuggleVideo("E:\\deapvideos\\face_video\\s05\\s05_trial01.avi");
//        XuggleVideo xvs = new XuggleVideo("/home/phm/drone.mp4");
////        XuggleVideo xvs = new XuggleVideo("/home/phm/BACKUP/Datasets/deapvideos/face_video/s01/s01_trial05.avi");
//        VideoSource vs = new VideoSource (xvs);
//        vs.open();
//        DataStream<ImageInstance> ds = vs.getImageStream ();
//        DataStreamConverter<ImageInstance, CLMInstance> dsclm = 
//                new DataStreamConverter<> (ds, new ImageToCLMConverter(1.4f));
//        DataStreamConverter<CLMInstance, InstanceList<ImageInstance>> dsreg = 
//                new DataStreamConverter<> (dsclm, new CLMToFacialRegionsConverter());
//        DataStreamConverter<InstanceList<ImageInstance>, InstanceList<Features>> dscin =
//                new DataStreamConverter<> (dsreg, new ImageListToFeaturesListConverter(
//                new ImageToStatisticalFeaturesConverter()));
//        DataStreamConverter<InstanceList<Features>, Features> dsc = 
//                new DataStreamConverter(dscin,
//                new FeaturesListToFeaturesConverter());
//        
//        v2HeartrateEngine hr = new v2HeartrateEngine ();
//        int howMany = 20;
//        List<Features> dataset = new LinkedList<>();
//        List<Double> reshr = new LinkedList<>();
//        for (int index = 0; dsc.hasMore(); index++) {
//            System.out.println (index);
//            Features fe = dsc.next();
//            if (dataset.size() >= howMany) {
//                v2HeartrateInfo hinfo = (v2HeartrateInfo) hr.estimate(dataset);
//                if (hinfo != null) {
//                    reshr.add(hinfo.getHeartrate());
//                    System.out.println ("[ " + hinfo.getStartTime() + 
//                                        " - " + hinfo.getEndTime() + 
//                                        " ]\t" + hinfo.getHeartrate());
//                }
//                dataset = new LinkedList<>();
//            }
//            if (fe != null) {
//                dataset.add(fe);
//            }
//        }
//        
//        LinkedList<RealVector> rv = new LinkedList<>();
//        for (double h : reshr) {
//            double [] tmp = {h};
//            rv.add(new ArrayRealVector(tmp));
//        }
//        System.out.println ("Finished ....");
//        CSVFileVectorWriter vw = new CSVFileVectorWriter(
//                                 new File("/home/phm/hr" + 
//                                 System.currentTimeMillis() + ".csv"), CSVFormat.TDF);
//        vw.write(rv);
//        vw.close();
    }

}
