
package com.phm.biometry.face;

import com.phm.biometry.core.ImageInstance;
import com.phm.core.data.converter.InstanceConverter;
import com.phm.core.data.InstanceList;
import java.util.LinkedList;

/**
 *
 * @author phm
 */
public class CLMToFacialRegionsConverter implements InstanceConverter<CLMInstance, InstanceList<ImageInstance>> {

    public CLMToFacialRegionsConverter() {
        // Empty body
    }

    @Override
    public InstanceList<ImageInstance> apply(CLMInstance data) {
        LinkedList<ImageInstance> list = new LinkedList<>();
        list.add(new ImageInstance(data.getTime(), data.getLabel(), data.extractForehead()));
        list.add(new ImageInstance(data.getTime(), data.getLabel(), data.extractLeftside()));
        list.add(new ImageInstance(data.getTime(), data.getLabel(), data.extractRightside()));
        InstanceList<ImageInstance> insts = new InstanceList<>(data.getTime(), 
                                                               data.getLabel(), list);
        return insts;
    }
    
}
