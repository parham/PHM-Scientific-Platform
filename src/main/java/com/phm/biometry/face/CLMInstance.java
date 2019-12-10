
package com.phm.biometry.face;

import Jama.Matrix;
import com.phm.biometry.core.ImageInstance;
import java.util.LinkedList;
import java.util.List;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.Edge;
import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleGraph;
import org.openimaj.image.MBFImage; 
import org.openimaj.image.pixel.ConnectedComponent;
import org.openimaj.image.pixel.PixelSet;
import org.openimaj.image.processing.face.detection.CLMDetectedFace;
import org.openimaj.image.processing.face.tracking.clm.MultiTracker;
import org.openimaj.math.geometry.point.Point2dImpl;
import org.openimaj.math.geometry.shape.Polygon;
import org.openimaj.math.geometry.shape.Rectangle;

/**
 *
 * @author Parham Nooralishahi - PHM!
 */
public class CLMInstance extends ImageInstance {
    
    protected static final int [][] facialConnections = {
        {0, 1},{1, 2},{2, 3},{3, 4},{4, 5},{5, 6},{6, 7},{7, 8},
        {8, 9},{9, 10},{10, 11},{11, 12},{12, 13},{13, 14},
        {14, 15},{15, 16},{17, 18},{18, 19},{19, 20},{20, 21},
        {22, 23},{23, 24},{24, 25},{25, 26},{27, 28},{28, 29},
        {29, 30},{31, 32},{32, 33},{33, 34},{34, 35},{36, 37},
        {37, 38},{38, 39},{39, 40},{40, 41},{41, 36},{42, 43},
        {43, 44},{44, 45},{45, 46},{46, 47},{47, 42},{48, 49},
        {49, 50},{50, 51},{51, 52},{52, 53},{53, 54},{54, 55},
        {55, 56},{56, 57},{57, 58},{58, 59},{59, 48},{60, 65},
        {60, 61},{61, 62},{62, 63},{63, 64},{64, 65}};
    
    protected Rectangle innerBoundary;
    protected Rectangle outterBoundary;
    protected float searchAreaSize = 1.4f;
    protected MBFImage croppedRegion;
    protected boolean valid = false;
    protected MultiTracker.TrackedFace detectedFace;
    protected LinkedList<Point2dImpl> facialPoints = null;
    
    public CLMInstance (long time, Object lbl, MBFImage orig, MultiTracker.TrackedFace tface, float searchsize) {
        super (time, lbl, orig);
        initialize(orig, tface, searchsize);
    }
    public CLMInstance (MBFImage orig, MultiTracker.TrackedFace tface, float searchsize) {
        this (System.currentTimeMillis(), null, orig, tface, searchsize);
    }
    public CLMInstance (long time, Object lbl, MBFImage orig, MultiTracker.TrackedFace tface) {
        this (time, lbl, orig, tface, 1.4f);
    }
    public CLMInstance (MBFImage orig, MultiTracker.TrackedFace tface) {
        this (orig, tface, 1.4f);
    }
    
    protected void initialize (MBFImage data, MultiTracker.TrackedFace f, float searchsize) {
        detectedFace = f;
        PixelSet pixelSet = new PixelSet((int) f.lastMatchBounds.x,
                                         (int) f.lastMatchBounds.y,
                                         (int) f.lastMatchBounds.width,
                                         (int) f.lastMatchBounds.height);
        
        MBFImage face = pixelSet.crop (data.getImage(), true);
        setTracker(f);
        setSearchAreaSize(searchsize);
        setCroppedRegion(face);
        setInnerBoundary(f.lastMatchBounds.clone());
        setOutterBoundary(f.lastMatchBounds.clone());
        getOutterBoundary().scaleCentroid(searchsize);
        setValid(true);
    }
    
    public MBFImage extractSkin () {
        MBFImage res = getImage().clone();
        ConnectedComponent cc = new ConnectedComponent(new Polygon(getFaceBoundaryPoints()));
        Float[] col = {0.0f, 0.0f, 0.0f};
        res.drawPolygonFilled(new Polygon(getLeftEyeBoundaryPoints()), col);
        res.drawPolygonFilled(new Polygon(getRightEyeBoundaryPoints()), col);
        res.drawPolygonFilled(new Polygon(getLeftEyebrowBoundaryPoints()), col);
        res.drawPolygonFilled(new Polygon(getRightEyebrowBoundaryPoints()), col);
        res.drawPolygonFilled(new Polygon(getNoseBoundaryPoints()), col);
        res.drawPolygonFilled(new Polygon(getMouthOutterBoundaryPoints()), col);
        res = cc.crop(res, true);
        return res;
    }
    public MBFImage extractForehead () {
        MultiTracker.TrackedFace f = getTracker();
        final Rectangle recface = getInnerBoundary();
        final Rectangle rbigger = getOutterBoundary();
        Rectangle roi = new Rectangle (recface.x + (recface.width / 4), 
                                       rbigger.y, recface.width - (2 * (recface.width / 4)), 
                                       Math.abs(rbigger.y - recface.y));
        MBFImage fore = getImage().extractROI(roi);
        return fore != null ? fore : getImage().clone();
    }
    public MBFImage extractLeftside () {
        MBFImage left = extractSkin();
        Rectangle rec = new Rectangle (0, left.getHeight() / 4, 
                                       left.getWidth() / 2, 
                                       left.getHeight() - (left.getHeight() / 4));
        left = left.extractROI(rec);
        return left != null ? left : getImage().clone();
    }
    public MBFImage extractRightside () {
        MBFImage left = extractSkin();
        Rectangle rec = new Rectangle (left.getWidth() / 2, left.getHeight() / 4, 
                                       left.getWidth() / 2, 
                                       left.getHeight() - (left.getHeight() / 4));
        left = left.extractROI(rec);
        return left != null ? left : getImage().clone();
    }
    
    public static int [][] getFacialConnections () {
        return facialConnections;
    }
    public List<Point2dImpl> getFacialPoints () {
        if (facialPoints == null || facialPoints.size() < 1) {
            CLMDetectedFace fd = new CLMDetectedFace (detectedFace, getImage().flatten());
            LinkedList<Point2dImpl> ps = new LinkedList<>();
            final int n = fd.getShapeMatrix().getRowDimension() / 2;
            Rectangle bounds = fd.getBounds();
            Matrix shape = fd.getShapeMatrix();
            Matrix visi = fd.getVisibility ();
            for (int i = 0; i < n; i++) {
                if (visi.get(i, 0) == 0) continue;
                ps.add(new Point2dImpl(((float) shape.get(i, 0) + bounds.x), 
                                       ((float) shape.get(i + n, 0) + bounds.y)));
            }
            facialPoints = new LinkedList<>(ps);
        }
        return facialPoints;
    }
    public Graph<Point2dImpl, Edge> getFacialGraph () {
        SimpleGraph<Point2dImpl, Edge> graph = new SimpleGraph (DefaultEdge.class);
        List<Point2dImpl> points = getFacialPoints ();
        for (Point2dImpl p : points) {
            graph.addVertex(p);
        }
        for (int index = 0; index < facialConnections.length; index++) {
            int fc = facialConnections [index][0];
            int sc = facialConnections [index][1];
            if (fc < points.size() && sc < points.size()) {
                graph.addEdge (points.get(fc), points.get(sc));
            }
        }
        return graph;
    }
    public List<Point2dImpl> getFaceBoundaryPoints () {
        LinkedList<Point2dImpl> face = new LinkedList<>();
        LinkedList<Point2dImpl> fps = new LinkedList<>(getFacialPoints ());
        if (fps.size() >= 16) {
            face.add(new Point2dImpl(getInnerBoundary().x, getOutterBoundary().y));
            for (int index = 0; index <= 16; index++) {
                face.add(fps.get(index));
            }
            face.add(new Point2dImpl(getInnerBoundary().x + getInnerBoundary().width, 
                                     getOutterBoundary().y));
        }
        return face;
    }
    public List<Point2dImpl> getLeftEyeBoundaryPoints () {
        LinkedList<Point2dImpl> eye = new LinkedList<>();
        LinkedList<Point2dImpl> fps = new LinkedList<>(getFacialPoints ());
        if (eye.size() >= 41) {
            for (int index = 36; index <= 41; index++) {
                eye.add(fps.get(index));
            }
        }
        return eye;
    }
    public List<Point2dImpl> getRightEyeBoundaryPoints () {
        LinkedList<Point2dImpl> eye = new LinkedList<>();
        LinkedList<Point2dImpl> fps = new LinkedList<>(getFacialPoints ());
        if (fps.size() >= 47) {
            for (int index = 42; index <= 47; index++) {
                eye.add(fps.get(index));
            }
        }
        return eye;    
    }
    public List<Point2dImpl> getLeftEyebrowBoundaryPoints () {
        LinkedList<Point2dImpl> eye = new LinkedList<>();
        LinkedList<Point2dImpl> fps = new LinkedList<>(getFacialPoints ());
        if (fps.size() >= 21) {
            for (int index = 17; index <= 21; index++) {
                eye.add(fps.get(index));
            }
        }
        return eye;
    }
    public List<Point2dImpl> getRightEyebrowBoundaryPoints () {
        LinkedList<Point2dImpl> eye = new LinkedList<>();
        LinkedList<Point2dImpl> fps = new LinkedList<>(getFacialPoints ());
        if (fps.size() >= 26) {
            for (int index = 22; index <= 26; index++) {
                eye.add(fps.get(index));
            }
        }
        return eye;
    }
    public List<Point2dImpl> getNoseBoundaryPoints () {
        LinkedList<Point2dImpl> nose = new LinkedList<>();
        LinkedList<Point2dImpl> fps = new LinkedList<>(getFacialPoints ());
        if (fps.size() >= 35) {
            nose.add(fps.get(27));
            for (int index = 31; index <= 35; index++) {
                nose.add(fps.get(index));
            }
        }
        return nose; 
    }
    public List<Point2dImpl> getMouthOutterBoundaryPoints () {
        LinkedList<Point2dImpl> m = new LinkedList<>();
        LinkedList<Point2dImpl> fps = new LinkedList<>(getFacialPoints ());
        if (fps.size() >= 59) {
            for (int index = 48; index <= 59; index++) {
                m.add(fps.get(index));
            }
        }
        return m; 
    }
    public List<Point2dImpl> getMouthInnerBoundaryPoints () {
        LinkedList<Point2dImpl> m = new LinkedList<>();
        LinkedList<Point2dImpl> fps = new LinkedList<>(getFacialPoints ());
        if (fps.size() >= 65) {
            for (int index = 60; index <= 65; index++) {
                m.add(fps.get(index));
            }
        }
        return m; 
    }
    
    protected void setSearchAreaSize (float sas) {
        searchAreaSize = sas;
    }
    public float getSearchAreaSize () {
        return searchAreaSize;
    }
    public void setValid (boolean v) {
        valid = v;
    }
    public boolean getValid () {
        return valid;
    }
    protected void setInnerBoundary (Rectangle inner) {
        innerBoundary = inner;
    }
    public Rectangle getInnerBoundary () {
        return innerBoundary;
    }
    protected void setOutterBoundary (Rectangle outer) {
        outterBoundary = outer;
    }
    public Rectangle getOutterBoundary () {
        return outterBoundary;
    }
    protected void setCroppedRegion (MBFImage rg) {
        croppedRegion = rg;
    }
    public MBFImage getCroppedRegion () {
        return croppedRegion;
    }
    protected void setTracker (MultiTracker.TrackedFace tf) {
        detectedFace = tf;
    }
    public MultiTracker.TrackedFace getTracker () {
        return detectedFace;
    }
}
