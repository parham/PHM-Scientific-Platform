package com.phm.biometry.test;
 
import com.phm.biometry.face.CLMInstance;
import org.openimaj.image.MBFImage;
import org.openimaj.image.processing.face.tracking.clm.CLMFaceTracker;
import org.openimaj.image.processing.face.tracking.clm.MultiTracker;
import org.openimaj.video.VideoDisplay;
import org.openimaj.video.VideoDisplayListener;
import org.openimaj.video.capture.VideoCapture;
import org.openimaj.video.capture.VideoCaptureException;

/**
 *
 * @author phm
 */
public class TestWebcam3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws VideoCaptureException {
        VideoCapture v = new VideoCapture(640, 480);
        VideoDisplay<MBFImage> vd = VideoDisplay.createVideoDisplay(v);
        vd.addVideoListener(new DisplayListener());
    }

    public static class DisplayListener implements VideoDisplayListener<MBFImage> {

        CLMFaceTracker tracker = new CLMFaceTracker();
        float searchAreaSize = 1.4f;

        @Override
        public void afterUpdate(VideoDisplay<MBFImage> vd) {
            
        }

        @Override
        public void beforeUpdate(MBFImage t) {
            tracker.track(t);
            if (tracker.getTrackedFaces().size() > 0) {
                MultiTracker.TrackedFace f = tracker.getTrackedFaces().get(0);
                CLMInstance en = new CLMInstance (System.currentTimeMillis(),
                                                  null, t,f, 1.4f);
                Float[] col = {1.0f, 0.0f, 0.0f};
//                t.drawImage(en.extractSkin(), 10, 10);
                t.drawImage(en.extractRightside(), 10, 10);
//                t.drawPolygon(new Polygon(en.getFaceBoundaryPoints()), col);
//                t.drawPolygon(new Polygon(en.getLeftEyeBoundaryPoints()), col);
//                t.drawPolygon(new Polygon(en.getRightEyeBoundaryPoints()), col);
//                t.drawPolygon(new Polygon(en.getLeftEyebrowBoundaryPoints()), col);
//                t.drawPolygon(new Polygon(en.getRightEyebrowBoundaryPoints()), col);
//                t.drawPolygon(new Polygon(en.getNoseBoundaryPoints()), col);
//                t.drawPolygon(new Polygon(en.getMouthOutterBoundaryPoints()), col);
//                t.drawPolygon(new Polygon(en.getMouthInnerBoundaryPoints()), col);
                
            }
        }

    }

}
