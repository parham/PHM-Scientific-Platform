/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phm.core.data.converter;

import com.phm.core.data.Features;
import com.phm.core.data.InstanceList;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author phm
 */
public class FeaturesListToFeaturesConverter implements InstanceConverter<InstanceList<Features>, Features> {

    @Override
    public Features apply(InstanceList<Features> data) {
        RealVector res = new ArrayRealVector();
        for (Features x : data) {
            res = res.append(x);
        }
        return new Features (data.getTime(), data.getLabel(), res);
    }
    
}
