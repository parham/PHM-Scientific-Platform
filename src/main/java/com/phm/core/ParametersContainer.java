
package com.phm.core;

import com.phm.annotations.ImplementationDetails;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ImplementationDetails (
    className = "ParametersContainer",
    date = "10/20/2015",
    lastModification = "10/20/2015",
    version = "1.0.0",
    description = "The parameter container saves and retrieves parameters and "
                + "applies specific procedure on the parameters."
)
public class ParametersContainer extends ConcurrentHashMap<String, Object>
                                 implements Cloneable {
    
    public ParametersContainer () {
        // Empty body
    }
    public ParametersContainer (Map<? extends String,? extends Object> maps) {
        super (maps);
    }
    public ParametersContainer (int init) {
        super (init);
    }
    
    public boolean processOnParameter (String key, ProcessOnParameter process) {
        Object value = get(key);
        if (value == null) {
            return false;
        }
        Object result = process.process(value, this);
        if (result == null) return false;
        put(key, result);
        return true;
    }
    
    @Override
    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
