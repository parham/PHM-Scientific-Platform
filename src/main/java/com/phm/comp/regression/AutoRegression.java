
package com.phm.comp.regression;

import java.util.List;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author phm
 */
public interface AutoRegression {
    public abstract RealVector estimate (List<? extends RealVector> inputs);
    public abstract List<RealVector> simulate (List<? extends RealVector> inputs);
    public abstract List<RealVector> error (List<? extends RealVector> inputs);
}
