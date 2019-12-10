
package com.phm.biometry.test;

import Jama.Matrix;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.processing.face.alignment.CLMAligner;
import org.openimaj.image.processing.face.detection.CLMDetectedFace;
import org.openimaj.image.processing.face.detection.CLMFaceDetector;
import org.openimaj.image.processing.face.util.CLMDetectedFaceRenderer;
import org.openimaj.math.geometry.point.Point2dImpl;
import org.openimaj.math.geometry.shape.Rectangle;

/**
 *
 * @author phm
 */
public class FacePointMain {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
//        MBFImage image = ImageUtilities.readMBF(new File ("/home/phm/Documents/images.jpg"));
//        FacialKeypointExtractor fke = new FacialKeypointExtractor();
//        FacialKeypoint [] fks = fke.extractFacialKeypoints (image.flatten());
//        for (FacialKeypoint fk : fks) {
//            System.out.println(fk.toString());
//        }
        //////////////////////////////////////////////////////
//        MBFImage image = ImageUtilities.readMBF(new File ("/home/phm/Documents/images.jpg"));
//        FKEFaceDetector fke = new FKEFaceDetector();
//        KEDetectedFace fd = fke.detectFaces (image.flatten()).get(0);
////        FacialKeypoint [] fks = fd.getKeypoints();
////        for (FacialKeypoint fk : fks) {
////            System.out.println(fk.toString());
////        }
//        new KEDetectedFaceRenderer().drawDetectedFace(image, 10, fd);
//        DisplayUtilities.display(image);
        ///////////////////////////////////////////////////////
        MBFImage image = ImageUtilities.readMBF(new File ("C:\\Users\\PHM\\Pictures\\face.jpg"));
        CLMFaceDetector cfe = new CLMFaceDetector();
        CLMDetectedFace fd = cfe.detectFaces (image.flatten()).get(0);
        new CLMDetectedFaceRenderer ().drawDetectedFace (image, 10, fd);
        CLMAligner ca = new CLMAligner();
        FImage imgt = ca.align (fd);
        DisplayUtilities.display (imgt);
        List<Point2dImpl> tmp = getFacialPoints(fd);
        for (Point2dImpl x : tmp) {
            System.out.println (x.x + " " + x.y);
        }
        DisplayUtilities.display (image);
        System.out.println (fd.toString());
    }
    
    public static List<Point2dImpl> getFacialPoints (CLMDetectedFace fd) {
        LinkedList<Point2dImpl> ps = new LinkedList<>();
        final int n = fd.getShapeMatrix().getRowDimension() / 2;
        Rectangle bounds = fd.getBounds();
        Matrix shape = fd.getShapeMatrix();
        Matrix visi = fd.getVisibility();
        for (int i = 0; i < n; i++) {
            if (visi.get(i, 0) == 0) continue;
            Point2dImpl pt = new Point2dImpl(((float) shape.get(i, 0) + bounds.x), 
                                   ((float) shape.get(i + n, 0) + bounds.y));
            ps.add(pt);
        }
        return ps;
    }
}
