
package com.phm.comp.window;

import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author phm
 */
public abstract class Window {
    
    protected final int orderObj;
    
    public Window (int order) {
        orderObj = order;
    }
    
    public int getOrder () {
        return orderObj;
    }
    
    public abstract RealVector getWindow ();
}
