
package com.phm.biometry.test;

import com.phm.biometry.core.ImageInstance;
import com.phm.biometry.core.ImageListToFeaturesListConverter;
import com.phm.biometry.core.VideoSource;
import com.phm.biometry.core.ImageToStatisticalFeaturesConverter;
import com.phm.biometry.face.CLMInstance; 
import com.phm.biometry.face.CLMToFacialRegionsConverter;
import com.phm.biometry.face.ImageToCLMConverter;
import com.phm.biometry.heartrate.fusion.phmHeartrateChannelFusion;
import com.phm.biometry.heartrate.v2.v2HeartrateInfo;
import com.phm.biometry.heartrate.v2.v2HeartrateWriter;
import com.phm.biometry.heartrate.v2.v2HeartrateEngine;
import com.phm.core.data.converter.FeaturesListToFeaturesConverter;
import com.phm.comp.filter.ica.RecursiveICA;
import com.phm.core.ds.DataStream; 
import com.phm.core.data.Features;
import com.phm.core.data.InstanceList;
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
public class TestVideoHR1 {

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
//        DataStreamConverter<CLMInstance, InstanceList<ImageInstance>> dsreg = 
//                new DataStreamConverter<> (dsclm, new CLMToFacialRegionsConverter());
//        DataStreamConverter<InstanceList<ImageInstance>, InstanceList<Features>> dscin =
//                new DataStreamConverter<> (dsreg, new ImageListToFeaturesListConverter(
//                new ImageToStatisticalFeaturesConverter()));
//        DataStreamConverter<InstanceList<Features>, Features> dsc = 
//                new DataStreamConverter(dscin,
//                new FeaturesListToFeaturesConverter());
//        List<Features> dataset = new LinkedList<>();
//        for (int index = 0; index < 150 && dsc.hasMore(); index++) {
//            Features fe = dsc.next();
//            if (fe != null) {
//                System.out.println (fe.toString());
//                dataset.add (fe);
//            }
//        }
//
//        vs.close();
//
//        v2HeartrateEngine hr = new v2HeartrateEngine (new SplineInterpolator(), 
//                                                      new RecursiveICA(),
//                                                      new phmHeartrateChannelFusion());
//        v2HeartrateInfo hinfo = (v2HeartrateInfo) hr.estimate(dataset);
//        v2HeartrateWriter hrw = new v2HeartrateWriter("/home/phm/experiment/v2");
//        hrw.write(hinfo);
//        System.out.println ();
//        System.out.println (hinfo.getHeartrate());
    }

}
