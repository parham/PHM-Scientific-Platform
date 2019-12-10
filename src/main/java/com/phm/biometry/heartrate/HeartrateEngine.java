
package com.phm.biometry.heartrate;

import com.phm.core.data.Features; 
import java.util.Collection;

/**
 *
 * @author phm
 */
public interface HeartrateEngine {
    public HeartrateInfo estimate (Collection<? extends Features> ds);
}
