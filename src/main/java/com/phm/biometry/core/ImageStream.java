
package com.phm.biometry.core;

import com.phm.core.ds.DataStream;

/**
 *
 * @author PHM
 */
public class ImageStream extends DataStream<ImageInstance> {

    public ImageStream (VideoSource src) {
        super (VideoSource.STREAM_NAME, src);
    }

    @Override
    public ImageInstance next () {
        VideoSource vs = (VideoSource) getSource();
        return new ImageInstance(vs.getVideo().getTimeStamp(), vs.getVideo().getNextFrame());
    }

    @Override
    public boolean hasMore () {
        VideoSource vs = (VideoSource) getSource();
        return vs.isActive() && vs.getVideo().hasNextFrame();
    }
}
