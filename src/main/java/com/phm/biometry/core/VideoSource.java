
package com.phm.biometry.core;

import com.phm.core.terminal.Source;
import com.phm.core.ds.DataStream;
import java.util.HashMap; 
import java.util.Map;
import org.openimaj.image.MBFImage;
import org.openimaj.video.Video;

/**
 *
 * @author PHM
 */
public class VideoSource extends Source {

    public static final String SOURCE_NAME = "openimaj.video";
    public static final String STREAM_NAME = "stream.image";
    
    protected Video<MBFImage> videoObj;
    protected ImageStream videoStream;
    
    public VideoSource (Video<MBFImage> vo) {
        super(SOURCE_NAME);
        videoObj = vo;
    }

    public void setVideo (Video<MBFImage> vo) {
        videoObj = vo;
    }
    public Video<MBFImage> getVideo () {
        return videoObj;
    }
    
    @Override
    protected boolean initialize () {
        videoStream = new ImageStream(this);
        return true;
    }

    @Override
    protected boolean activate () {
        return true;
    }

    @Override
    protected boolean deactivate () {
        if (videoObj != null) {
            videoObj.close();
            return true;
        }
        return false;
    }

    @Override
    protected boolean terminate () {
        return true;
    }

    public boolean isActive() {
        return videoObj != null && videoObj.hasNextFrame();
    }

    public DataStream<ImageInstance> getImageStream () {
        return videoStream;
    }
    
    @Override
    public Map<String,DataStream> streams () {
        HashMap<String,DataStream> map = new HashMap<>();
        map.put(STREAM_NAME, videoStream);
        return map;
    }

    @Override
    public DataStream stream (String stname) {
        return videoStream;
    }
}
