
package com.phm.biometry.core;

import com.phm.core.data.Instance;
import java.util.List;
import java.util.Map;
import org.openimaj.image.MBFImage;

/**
 *
 * @author Parham Nooralishahi - PHM!
 */
public class ImageInstance implements Instance {
    
    public static final String PARAM_IMAGE = "instance.image";
    
    private long timeObj = 0;
    private Object labelObj;    
    protected MBFImage imgObj = null;
    protected int fpsObj = 25;

    public ImageInstance (long time, Object lbl, MBFImage img, int fps) {
        timeObj = time;
        labelObj = lbl;
        imgObj = img;
        fpsObj = fps;
    }
    public ImageInstance (long time, Object lbl, MBFImage img) {
        timeObj = time;
        labelObj = lbl;
        imgObj = img;
    }
    public ImageInstance (long time, MBFImage img) {
        timeObj = time;
        imgObj = img;
    }
    public ImageInstance (MBFImage img) {
        imgObj = img;
    }
    public ImageInstance () {
        super ();
    }
    
    @Override
    public void setTime (long time) {
        timeObj = time;
    }
    @Override
    public long getTime () {
        return timeObj;
    }
    @Override
    public void setLabel (Object lbl) {
        labelObj = lbl;
    }
    @Override
    public Object getLabel () {
        return labelObj;
    }
    
    public void setFPS (int fps) {
        fpsObj = fps;
    }
    public int getFPS () {
        return fpsObj;
    }
    public void setImage (MBFImage img) {
        imgObj = img;
    }
    public MBFImage getImage () {
        return imgObj;
    }
    @Override
    public ImageInstance clone () {
        return copy ();
    }

    @Override
    public ImageInstance copy() {
        return new ImageInstance(getTime(), getLabel(), imgObj.clone(), fpsObj);
    }

    @Override
    public boolean code(Map<String, Object> map) {
        // Time
        map.put(PARAM_TIME, timeObj);
        // Label
        map.put(PARAM_LABEL, labelObj);
        // Image
        map.put(PARAM_IMAGE, getImage());
        return true;
    }

    @Override
    public boolean code(List<Object> list) {
        list.add(timeObj);
        list.add(labelObj);
        list.add(getImage());
        return true;
    }

    @Override
    public boolean decode(Map<String, Object> map) {
        // Time
        String timestr = (String) map.get(PARAM_TIME);
        if (timestr != null) {
            setTime(Long.valueOf(timestr));
        }
        // Label
        Object lbl = map.get(PARAM_LABEL);
        setLabel(lbl);
        // Image
        setImage((MBFImage) map.get(PARAM_IMAGE));
        return true;
    }

    @Override
    public boolean decode(List<Object> list) {
        // Time
        String timestr = (String) list.get(0);
        if (timestr != null) {
            setTime(Long.valueOf(timestr));
        }
        // Label
        Object lbl = list.get(1);
        setLabel(lbl);
        // Image
        setImage((MBFImage) list.get(2));
        return true;
    }
}
