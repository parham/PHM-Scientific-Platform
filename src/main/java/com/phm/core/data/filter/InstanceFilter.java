
package com.phm.core.data.filter;

import com.phm.annotations.ImplementationDetails;
import com.phm.core.data.Instance;

@ImplementationDetails (
    className = "EntityFilter",
    date = "10/20/2015",
    lastModification = "10/20/2015",
    version = "1.0.0",
    description = "entity filter..."
)
public interface InstanceFilter<DataType extends Instance> {
    public DataType apply (DataType dt);
}
