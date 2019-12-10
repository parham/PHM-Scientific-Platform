
package com.phm.core.data.filter;

import com.phm.core.data.Instance;

/**
 *
 * @author phm
 * @param <InputType>
 */
public class BlankInstanceFilter<InputType extends Instance> implements InstanceFilter<InputType> {
    @Override
    public InputType apply(InputType dt) {
        return dt;
    }
}
