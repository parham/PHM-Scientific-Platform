
package com.phm.comp.filter; 

import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author phm
 */
public class BlankFilter implements VectorFilter {
    @Override
    public RealVector filter (RealVector data) {
        return data.copy();
    }
}
