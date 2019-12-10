
package com.phm.biometry.core;

import com.phm.core.data.Features;
import com.phm.core.data.converter.InstanceConverter;
import com.phm.core.data.InstanceList;
import java.util.LinkedList;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author phm
 */
public class ImageListToFeaturesConverter implements InstanceConverter<InstanceList<ImageInstance>, Features> {

    protected InstanceConverter<ImageInstance,Features> featureExtractor;
    
    public ImageListToFeaturesConverter(InstanceConverter<ImageInstance,Features> fe) {
        featureExtractor = fe;
    }
    
    public void setFeaturesExtractor (InstanceConverter<ImageInstance,Features> fe) {
        featureExtractor = fe;
    }
    public InstanceConverter<ImageInstance,Features> getFeaturesExtractor () {
        return featureExtractor;
    }
    
    @Override
    public Features apply(InstanceList<ImageInstance> data) {
        LinkedList<Features> list = new LinkedList<>();
        data.stream().forEach((x) -> {
            list.add(featureExtractor.apply(x));
        });
        
        RealVector vec = new ArrayRealVector();
        for (Features x : list) {
            vec = vec.append(x);
        }
        Features res = new Features (data.getTime(), data.getLabel(), vec);
        return res;    
    }
}
