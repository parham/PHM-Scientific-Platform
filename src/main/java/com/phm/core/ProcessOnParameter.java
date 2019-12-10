
package com.phm.core;

import com.phm.annotations.ImplementationDetails;

@ImplementationDetails (
    className = "ProcessOnParameter",
    date = "10/20/2015",
    lastModification = "10/20/2015",
    version = "1.0.0",
    description = "The class is an interface for applying "
                + "a procedure on an specific field in the "
                + "container."
)
public interface ProcessOnParameter {
    public Object process (Object data, ParametersContainer c);
}
