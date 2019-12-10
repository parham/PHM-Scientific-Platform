
package com.phm.biometry.test;

import com.phm.biometry.core.ImageInstance;
import com.phm.biometry.core.VideoSource;
import com.phm.biometry.core.ImageToStatisticalFeaturesConverter;
import com.phm.biometry.face.CLMInstance;
import com.phm.biometry.face.CLMToFaceRegionConverter;
import com.phm.biometry.face.ImageToCLMConverter;
import com.phm.biometry.heartrate.v1.v1HeartrateEngine;
import com.phm.biometry.heartrate.v1.v1HeartrateInfo;
import com.phm.biometry.heartrate.v1.v1HeartrateWriter;
import com.phm.core.ds.DataStream; 
import com.phm.core.data.Features;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.openimaj.video.capture.VideoCaptureException;
import org.openimaj.video.xuggle.XuggleVideo;

/**
 *
 * @author phm
 */
public class TestVideoHR {

    /**
     * @param args the command line arguments
     * @throws org.openimaj.video.capture.VideoCaptureException
     */
    public static void main(String[] args) throws VideoCaptureException, IOException {
////        XuggleVideo xvs = new XuggleVideo("E:\\deapvideos\\face_video\\s05\\s05_trial01.avi");
////        XuggleVideo xvs = new XuggleVideo("/home/phm/drone.mp4");
//        XuggleVideo xvs = new XuggleVideo("/home/phm/BACKUP/Datasets/deapvideos/face_video/s01/s01_trial05.avi");
//        VideoSource vs = new VideoSource (xvs);
//        vs.open();
//        DataStream<ImageInstance> ds = vs.getImageStream ();
//        DataStreamConverter<ImageInstance, CLMInstance> dsclm = 
//                new DataStreamConverter<> (ds, new ImageToCLMConverter(1.4f));
//        DataStreamConverter<CLMInstance, ImageInstance> dsreg = 
//                new DataStreamConverter<> (dsclm, new CLMToFaceRegionConverter());
//        DataStreamConverter<ImageInstance, Features> dsc =
//                new DataStreamConverter<> (dsreg, new ImageToStatisticalFeaturesConverter());
//
//        List<Features> dataset = new LinkedList<>();
//        for (int index = 0; index < 250 && dsc.hasMore(); index++) {
//            Features fe = dsc.next();
//            if (fe != null) {
//                System.out.println (fe.toString());
//                dataset.add (fe);
//            }
//        }
//
//        vs.close();
//
//        v1HeartrateEngine hr = new v1HeartrateEngine (new SplineInterpolator());
//        v1HeartrateInfo hinfo = (v1HeartrateInfo) hr.estimate(dataset);
//        v1HeartrateWriter hrw = new v1HeartrateWriter("/home/phm/experiment/v1");
//        hrw.write(hinfo);
//        System.out.println ();
//        System.out.println (hinfo.getHeartrate());
    }
}
