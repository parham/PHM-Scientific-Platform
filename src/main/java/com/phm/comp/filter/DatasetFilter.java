
package com.phm.comp.filter;

import java.util.List;
import org.apache.commons.math3.linear.RealVector;


/**
 *
 * @author phm
 */
public interface DatasetFilter {
    public List<RealVector> filter (List<? extends RealVector> ds);
}
