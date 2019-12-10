
package com.phm.core;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Parham Nooralishahi - PHM!
 */
public interface Tableizeable {
    public boolean code (Map<String, Object> map);
    public boolean code (List<Object> list);
    public boolean decode (Map<String,Object> map);
    public boolean decode (List<Object> map);
}
