
package com.phm.biometry.test;

import org.openimaj.image.MBFImage;
import org.openimaj.image.processing.face.detection.CLMDetectedFace;
import org.openimaj.image.processing.face.tracking.clm.CLMFaceTracker;
import org.openimaj.image.processing.face.tracking.clm.MultiTracker;
import org.openimaj.image.processing.face.util.CLMDetectedFaceRenderer;
import org.openimaj.math.geometry.shape.Rectangle;
import org.openimaj.video.VideoDisplay;
import org.openimaj.video.VideoDisplayListener;
import org.openimaj.video.xuggle.XuggleVideo;

/**
 *
 * @author PHM
 */
public class TestMain2 {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        XuggleVideo v = new XuggleVideo ("/home/phm/BACKUP/Datasets/deapvideos/face_video/s01/s01_trial01.avi");
        VideoDisplay<MBFImage> vd = VideoDisplay.createVideoDisplay( v );
        vd.addVideoListener(new DisplayListener());
    }
    
    public static class DisplayListener implements VideoDisplayListener<MBFImage> {
        CLMFaceTracker tracker = new CLMFaceTracker();
        float searchAreaSize = 1.4f;
        
        @Override
        public void afterUpdate (VideoDisplay<MBFImage> vd) {
            
        }

        @Override
        public void beforeUpdate(MBFImage t) {
            tracker.track(t);
            if (tracker.getTrackedFaces().size() > 0) {
                MultiTracker.TrackedFace f = tracker.getTrackedFaces().get(0);
                final Rectangle recface = f.lastMatchBounds;
                final Rectangle rbigger = f.lastMatchBounds.clone();
                rbigger.scaleCentroid(searchAreaSize);
                Rectangle roi = new Rectangle (recface.x + (recface.width / 4), rbigger.y, recface.width - (2 * (recface.width / 4)), Math.abs(rbigger.y - recface.y));
                Float [] col = {1.0f, 0.0f, 0.0f};
                t.drawShape(roi, col);
                Float [] col2 = {0.0f, 1.0f, 0.1f};
                t.drawShape(recface, col2);
                Float [] col3 = {0.0f, 0.2f, 1.0f};
                t.drawShape(rbigger, col3);
                CLMDetectedFace df = new CLMDetectedFace (f, t.flatten());
                new CLMDetectedFaceRenderer ().drawDetectedFace (t, 10, df);
                MBFImage test = t.extractROI(roi);
                System.out.println (test.getWidth() + ":" + t.getWidth() + "\t" + 
                                    test.getHeight() + ":" + t.getHeight());
            }
        }
        
    }
}
