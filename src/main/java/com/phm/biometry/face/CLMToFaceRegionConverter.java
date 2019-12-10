
package com.phm.biometry.face;

import com.phm.biometry.core.ImageInstance;
import com.phm.core.data.converter.InstanceConverter;

/**
 *
 * @author phm
 */
public class CLMToFaceRegionConverter 
        implements InstanceConverter<CLMInstance, ImageInstance> {

    @Override
    public ImageInstance apply (CLMInstance data) {
        return data != null ? new ImageInstance(data.getTime (), 
                                                data.getLabel (), 
                                                data.extractSkin ()) : null;
    }    
}
