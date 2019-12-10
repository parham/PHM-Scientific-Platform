
package com.phm.biometry.test;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.colour.Transforms;
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
public class TestFace3 {
    static Float [] color = {0.0f, 1.0f, 0.1f};
    static File fdir = new File("/home/phm/experiment");
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        MBFImage img = ImageUtilities.readMBF(new File("/home/phm/Desktop/Screenshot from 2016-01-06 18:09:16.png"));
//        FKEFaceDetector fd = new FKEFaceDetector();
        SandeepFaceDetector fd = new SandeepFaceDetector();
        
        img.drawShape(fd.detectFaces(img).get(0).getBounds(), color);
        DisplayUtilities.display(img);
    }

}