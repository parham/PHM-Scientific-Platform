
package com.phm.biometry.test;
 
import com.phm.biometry.core.ImageInstance;
import com.phm.biometry.core.VideoSource;
import com.phm.core.ds.DataStream;
import java.util.ArrayList;
import org.openimaj.image.FImage;
import org.openimaj.video.VideoDisplay;
import org.openimaj.video.capture.VideoCapture;
import org.openimaj.video.capture.VideoCaptureException;

/**
 *
 * @author phm
 */
public class TestWebcam2 {

    /**
     * @param args the command line arguments
     * @throws org.openimaj.video.capture.VideoCaptureException
     */
    public static void main(String[] args) throws VideoCaptureException {
//        VideoCapture xvs = new VideoCapture (640, 480);
//        VideoSource vs = new VideoSource (xvs);
//        vs.open();
//        DataStream<ImageInstance> ds = vs.getImageStream ();
//        CLMFaceTrackerStreamFilter csf = new CLMFaceTrackerStreamFilter(ds);
//        ArrayList<FImage> arr = new ArrayList<>();
//        for (int index = 0; index < 60; index++) {
//            ImageInstance tmp = csf.next ();
//            if (tmp != null && tmp.getImage() != null) {
//                arr.add (tmp.getImage().flatten());
//            }
//        }
//        FImage [] arrimg = arr.toArray(new FImage [0]);
//        VideoDisplay<FImage> vd = VideoDisplay.createVideoDisplay(arrimg);
////        ImageToFeatureFilter iff = new ImageToFeatureFilter(ds, new StatisticalFeaturesExtractor());
////        Dataset dataset = new Dataset();
////        int index = 0;
////        while (ds.hasMore() && index++ < 50) {
////            FeaturesEntity fe = iff.next();
////            System.out.println (fe.toString());
////            dataset.add (fe);
////        }
////        vs.close();
////        v1HeartrateEngine hr = new v1HeartrateEngine(new LinearInterpolator());
////        HeartrateInfo hinfo = hr.estimate(dataset);
////        System.out.println (hinfo.getHeartrate());
    }

}
