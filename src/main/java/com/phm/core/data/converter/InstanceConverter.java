
package com.phm.core.data.converter;

import com.phm.annotations.ImplementationDetails;
import com.phm.core.data.Instance;

@ImplementationDetails (
    className = "EntityConverter",
    date = "10/20/2015",
    lastModification = "10/20/2015",
    version = "1.0.0",
    description = " "
)
public interface InstanceConverter<InputType extends Instance, OutputType extends Instance> {
    public OutputType apply (InputType data);
}