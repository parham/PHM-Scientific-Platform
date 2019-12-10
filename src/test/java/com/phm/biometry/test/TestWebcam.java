package com.phm.biometry.test;
 
import com.phm.biometry.face.CLMInstance;
import java.text.AttributedString;
import java.util.List;
import org.openimaj.image.MBFImage;
import org.openimaj.image.processing.face.detection.CLMDetectedFace;
import org.openimaj.image.processing.face.tracking.clm.CLMFaceTracker;
import org.openimaj.image.processing.face.tracking.clm.MultiTracker;
import org.openimaj.image.processing.face.util.CLMDetectedFaceRenderer;
import org.openimaj.image.typography.general.GeneralFont;
import org.openimaj.math.geometry.point.Point2dImpl;
import org.openimaj.math.geometry.shape.Rectangle;
import org.openimaj.video.VideoDisplay;
import org.openimaj.video.VideoDisplayListener;
import org.openimaj.video.capture.VideoCapture;
import org.openimaj.video.capture.VideoCaptureException;

/**
 *
 * @author phm
 */
public class TestWebcam {

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
                final Rectangle recface = f.lastMatchBounds;
                final Rectangle rbigger = f.lastMatchBounds.clone();
                rbigger.scaleCentroid(searchAreaSize);
                CLMInstance ffd = new CLMInstance (t, f);
//                CLMFacialFeatureDetector ffd = new CLMFacialFeatureDetector();
                
                List<Point2dImpl> points = ffd.getFacialPoints ();
                for (int index = 0; index < points.size(); index++) {
                    Point2dImpl p = points.get(index);
                    t.drawText(String.valueOf(index), (int) p.x, (int) p.y, 
                            new GeneralFont ("Times New Roman", 0), 12);
                    t.drawText (new AttributedString (String.valueOf(index)), p); //String.valueOf(index), p.x, p.y, 
                }
                
                Rectangle roi = new Rectangle(recface.x + (recface.width / 4), rbigger.y, recface.width - (2 * (recface.width / 4)), Math.abs(rbigger.y - recface.y));
                Float[] col = {1.0f, 0.0f, 0.0f};
                t.drawShape(roi, col);
                t.drawLine((int) recface.x + 10, (int) recface.y + 10, 
                           (int) recface.x + 10 + (int) recface.width, (int) recface.y + 10, col);
                Float[] col2 = {0.0f, 1.0f, 0.1f};
                t.drawShape(recface, col2);
                Float[] col3 = {0.0f, 0.2f, 1.0f};
                t.drawShape(rbigger, col3);
                CLMDetectedFace df = new CLMDetectedFace(f, t.flatten());
                new CLMDetectedFaceRenderer().drawDetectedFace(t, 2, df);
                MBFImage test = t.extractROI(roi);
                System.out.println(test.getWidth() + ":" + t.getWidth() + "\t"
                        + test.getHeight() + ":" + t.getHeight());
            }
        }

    }

}
