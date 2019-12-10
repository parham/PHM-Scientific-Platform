
package com.phm.biometry.core;

import com.phm.core.data.Features;
import com.phm.core.data.converter.InstanceConverter;
import org.apache.commons.math3.stat.descriptive.UnivariateStatistic;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.openimaj.image.MBFImage;

/**
 *
 * @author phm
 */
public class ImageToStatisticalFeaturesConverter 
    implements InstanceConverter<ImageInstance, Features> {

    protected UnivariateStatistic featureMethod;
    
    public ImageToStatisticalFeaturesConverter (UnivariateStatistic us) {
        featureMethod = us;
    }
    public ImageToStatisticalFeaturesConverter () {
        featureMethod = new Mean();
    }
    
    public UnivariateStatistic getStatistical () {
        return featureMethod;
    }

    @Override
    public Features apply(ImageInstance data) {
//        if (data == null || data.getImage() == null) {
//            System.out.println ("ERROR");
//        }
        final int imgsize = data.getImage().getWidth() * data.getImage().getHeight();
        MBFImage img = data.getImage();
        double [] red = new double [imgsize];
        double [] green = new double[imgsize];
        double [] blue = new double[imgsize];
        int index = 0;
        // Build pixels array
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                Float [] pixel = img.getPixel(x, y);
                red [index] = pixel [0];
                green [index] = pixel [1];
                blue [index++] = pixel [2];
            }
        }
        // Extract features
        double [] features = new double [3];
        features [0] = featureMethod.evaluate(red);
        features [1] = featureMethod.evaluate(green);
        features [2] = featureMethod.evaluate(blue);
        return new Features (data.getTime(), data.getLabel(), features);
    }
}
