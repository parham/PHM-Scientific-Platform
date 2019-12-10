
package com.phm.core.data;

import com.phm.annotations.ImplementationDetails;
import com.phm.core.Tableizeable;
import java.io.Serializable;

@ImplementationDetails (
    className = "Instance",
    date = "10/20/2015",
    lastModification = "10/20/2015",
    version = "1.0.0",
    description = "This is an atomic part of the library that descripes basic datatype."
)
public interface Instance extends Tableizeable, Serializable, Cloneable {
    
    public static final String PARAM_TIME = "instance.time";
    public static final String PARAM_LABEL = "instance.label";
    
    public void setTime (long time);
    public long getTime ();
    public void setLabel (Object lbl);
    public Object getLabel ();
    public Instance copy ();
}