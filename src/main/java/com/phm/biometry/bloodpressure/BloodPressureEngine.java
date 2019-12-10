
package com.phm.biometry.bloodpressure;

import com.phm.core.data.Features;
import java.util.Collection;

/**
 *
 * @author phm
 */
public interface BloodPressureEngine {
    public BloodPressureInfo estimate (Collection<? extends Features> ds);
}
