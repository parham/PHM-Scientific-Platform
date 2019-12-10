
package com.phm.biometry.test;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.colour.Transforms;
import org.openimaj.image.model.pixel.HistogramPixelModel;
import org.openimaj.image.model.pixel.MBFPixelClassificationModel;
import org.openimaj.image.processing.face.detection.CLMDetectedFace;
import org.openimaj.image.processing.face.detection.CLMFaceDetector;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.FaceDetector;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;
import org.openimaj.image.processing.face.detection.SandeepFaceDetector;
import org.openimaj.image.processing.face.detection.keypoints.FKEFaceDetector;
import org.openimaj.image.processing.face.detection.keypoints.KEDetectedFace;
import org.openimaj.video.xuggle.XuggleVideo;

/**
 *
 * @author phm
 */
public class TestFace2 {
    static Float [] color = {0.0f, 1.0f, 0.1f};
    static File fdir = new File("/home/phm/experiment");
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        XuggleVideo xvs = new XuggleVideo("/home/phm/SEARCH_RESCUE/Face Images Experiment/Original/Faces0.5 and 1 mps Original_Bebop_Drone_2015-12-11T132925+0000_8FE390.mp4");
        xvs.seek(14.0);
//        VideoDisplay<MBFImage> vd = VideoDisplay.createVideoDisplay(xvs);
//        vd.addVideoListener(new TestWebcam.DisplayListener());
//        HaarCascadeDetector hcd = new HaarCascadeDetector();
//        FKEFaceDetector hcd = new FKEFaceDetector();
//        CLMFaceDetector hcd = new CLMFaceDetector();
        SandeepFaceDetector hcd = new SandeepFaceDetector(new HistogramPixelModel(16, 6));
        System.out.println ("System is started ...");
        while (xvs.hasNextFrame()) {
//            System.out.println(xvs.getTimeStamp());
            MBFImage frame = xvs.getNextFrame();
            FImage tmp = Transforms.calculateIntensity(frame);
//            List<DetectedFace> faces = new LinkedList<>(hcd.detectFaces (tmp));
            List<DetectedFace> faces = new LinkedList<>(hcd.detectFaces (frame));
            if (faces.size() > 0) {
                System.out.println(xvs.getTimeStamp() + " Detected face : " + faces.size());
                faces.stream().forEach((f) -> {
                    tmp.drawShape(f.getShape(), 0.4f);
                });
                File ff = new File(fdir, xvs.getTimeStamp() + "_frame.jpg");
                ImageUtilities.write(tmp, ff);
            }
        }
    }

}