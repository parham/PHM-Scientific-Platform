
package com.phm.comp.filter;

import org.apache.commons.math3.analysis.function.Exp;
import org.apache.commons.math3.linear.RealVector; 


/**
 *
 * @author phm
 */
public class GaussianFilter implements VectorFilter {
    
    protected double alpha = 0.0;
    
    public GaussianFilter (double a) {
        alpha = a;
    }
    public double getAlpha () {
        return alpha;
    }
    public void setAlpha (double a) {
        alpha = a;
    }

    @Override
    public RealVector filter(RealVector x) {
        final double c = Math.sqrt (alpha / Math.PI); 
        RealVector res = x.ebeMultiply(x).mapMultiply(-1).map(new Exp()).mapMultiply(c);
        return res;
    }
}