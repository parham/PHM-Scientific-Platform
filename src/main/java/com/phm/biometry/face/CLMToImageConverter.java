
package com.phm.biometry.face;

import com.phm.biometry.core.ImageInstance;
import com.phm.core.data.converter.InstanceConverter;
import org.openimaj.image.MBFImage;

/**
 *
 * @author PHM
 */
public class CLMToImageConverter implements InstanceConverter<CLMInstance, ImageInstance> {

    public CLMToImageConverter () {
        // Empty body
    }

    @Override
    public ImageInstance apply (CLMInstance data) {
        if (data != null) {
            MBFImage clm = data.getCroppedRegion();
            return clm != null ? new ImageInstance(data.getTime(), 
                                                   data.getLabel(), 
                                                   data.getCroppedRegion()) : null;            
        }
        return null;
    }
}
