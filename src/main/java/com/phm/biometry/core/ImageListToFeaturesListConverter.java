
package com.phm.biometry.core;

import com.phm.core.data.Features;
import com.phm.core.data.converter.InstanceConverter;
import com.phm.core.data.InstanceList;
import java.util.LinkedList;

/**
 *
 * @author Parham Nooralishahi - PHM!
 */
public class ImageListToFeaturesListConverter 
    implements InstanceConverter<InstanceList<ImageInstance>, InstanceList<Features>> {

    protected InstanceConverter<ImageInstance,Features> featureExtractor;
    
    public ImageListToFeaturesListConverter (InstanceConverter<ImageInstance,Features> fe) {
        featureExtractor = fe;
    }
    
    public void setFeaturesExtractor (InstanceConverter<ImageInstance,Features> fe) {
        featureExtractor = fe;
    }
    public InstanceConverter<ImageInstance,Features> getFeaturesExtractor () {
        return featureExtractor;
    }
    
    @Override
    public InstanceList<Features> apply (InstanceList<ImageInstance> data) {
        LinkedList<Features> list = new LinkedList<>();
        data.stream().forEach((x) -> {
            list.add(featureExtractor.apply (x));
        });
        InstanceList<Features> ftlist = new InstanceList<>(data.getTime(), data.getLabel(), list);
        return ftlist;
    }
    
}
