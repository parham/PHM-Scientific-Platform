
package com.phm.biometry.heartrate.fusion;

import com.phm.biometry.heartrate.HeartrateInfo;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author phm
 */
public class AverageHeartrateChannelFusion implements HeartrateChannelFusion {

    @Override
    public HeartrateInfo fuse (HeartrateInfo info) {
        List<Double> hrs = new LinkedList<>(info.getPossibleHeartrate());
        double hr = 0;
        for (Double hr1 : hrs) {
            hr += hr1;
        }
        hr /= hrs.size();
        
        info.setHeartrate(hr);
        return info;
    }
    
}
