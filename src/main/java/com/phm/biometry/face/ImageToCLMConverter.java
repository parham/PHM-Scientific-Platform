
package com.phm.biometry.face;

import com.phm.biometry.core.ImageInstance;
import com.phm.core.data.converter.InstanceConverter;
import java.util.LinkedList;
import org.openimaj.image.processing.face.tracking.clm.CLMFaceTracker;
import org.openimaj.image.processing.face.tracking.clm.MultiTracker;

/**
 *
 * @author PHM
 */
public class ImageToCLMConverter implements InstanceConverter<ImageInstance, CLMInstance> {

    
    protected CLMFaceTracker tracker = new CLMFaceTracker();
    protected float searchSize = 1.4f;
    
    public ImageToCLMConverter () {
        // Empty body
    }
    public ImageToCLMConverter (float ssize) {
        setSearchSize(ssize);
    }
    
    public void setSearchSize (float ssize) {
        searchSize = ssize;
    }
    public float getSearchSize () {
        return searchSize;
    }

    public void setCLMFaceTracker (CLMFaceTracker ct) {
        tracker = ct;
    }
    public CLMFaceTracker getCLMFaceTracker () {
        return tracker;
    }
    
    @Override
    public CLMInstance apply (ImageInstance data) {
        tracker.track (data.getImage());
        LinkedList<MultiTracker.TrackedFace> faces = new LinkedList<> (tracker.getTrackedFaces());
        CLMInstance en = null;
        if (faces.size() > 0) {
            MultiTracker.TrackedFace f = faces.get(0);
            en = new CLMInstance (data.getTime(), 
                                  data.getLabel(), 
                                  data.getImage(), f, 
                                  getSearchSize());
        }
        return en;
    }
}
