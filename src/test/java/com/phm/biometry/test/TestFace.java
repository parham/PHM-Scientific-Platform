
package com.phm.biometry.test;

import com.phm.biometry.face.CLMInstance;
import java.util.List;
import org.openimaj.image.MBFImage;
import org.openimaj.image.processing.face.detection.CLMDetectedFace;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;
import org.openimaj.image.processing.face.tracking.clm.CLMFaceTracker;
import org.openimaj.image.processing.face.tracking.clm.MultiTracker;
import org.openimaj.image.processing.face.util.CLMDetectedFaceRenderer;
import org.openimaj.image.typography.general.GeneralFont;
import org.openimaj.math.geometry.point.Point2dImpl;
import org.openimaj.math.geometry.shape.Rectangle;
import org.openimaj.video.VideoDisplay;
import org.openimaj.video.VideoDisplayListener;
import org.openimaj.video.xuggle.XuggleVideo;

/**
 *
 * @author phm
 */
public class TestFace {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        XuggleVideo xvs = new XuggleVideo("/home/phm/SEARCH_RESCUE/Face Images Experiment/Original/Faces0.5 and 1 mps Original_Bebop_Drone_2015-12-11T132925+0000_8FE390.mp4");
        VideoDisplay<MBFImage> vd = VideoDisplay.createVideoDisplay(xvs);
        vd.addVideoListener(new TestWebcam.DisplayListener());
    }
    
    
    public static class DisplayListener implements VideoDisplayListener<MBFImage> {

        CLMFaceTracker tracker = new CLMFaceTracker();
        float searchAreaSize = 1.4f;
        HaarCascadeDetector hcd = new HaarCascadeDetector();

        @Override
        public void afterUpdate(VideoDisplay<MBFImage> vd) {
            
        }

        @Override
        public void beforeUpdate(MBFImage t) {
//            tracker.track(t);
//            if (tracker.getTrackedFaces().size() > 0) {
//                tracker.getTrackedFaces().stream().forEach((f) -> {
//                    final Rectangle recface = f.lastMatchBounds;
//                    final Rectangle rbigger = f.lastMatchBounds.clone();
//                    rbigger.scaleCentroid(searchAreaSize);
//                    Float[] col2 = {0.0f, 1.0f, 0.1f};
//                    t.drawShape(recface, col2);
//                    Float[] col3 = {0.0f, 0.2f, 1.0f};
//                    t.drawShape(rbigger, col3);
//                });
//            }
            hcd.detectFaces(t.flatten()).stream().forEach((x) -> {
                Float[] col2 = {0.0f, 1.0f, 0.1f};
                t.drawShape(x.getBounds(), col2);
            });
        }

    }
}