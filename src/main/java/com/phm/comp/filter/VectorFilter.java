
package com.phm.comp.filter;

import com.phm.annotations.ImplementationDetails;
import org.apache.commons.math3.linear.RealVector;

@ImplementationDetails (
    className = "VectorFilter",
    date = "10/20/2015",
    lastModification = "10/20/2015",
    version = "1.0.0",
    description = "vector filter ..."
)
public interface VectorFilter {
    public RealVector filter (RealVector data);
}
