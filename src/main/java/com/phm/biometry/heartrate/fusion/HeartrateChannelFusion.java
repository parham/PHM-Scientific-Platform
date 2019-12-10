
package com.phm.biometry.heartrate.fusion;

import com.phm.biometry.heartrate.HeartrateInfo;

/**
 *
 * @author phm
 */
public interface HeartrateChannelFusion {
    public abstract HeartrateInfo fuse (HeartrateInfo info);
}
