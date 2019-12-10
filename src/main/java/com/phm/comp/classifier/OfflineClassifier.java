
package com.phm.comp.classifier;

import com.phm.core.data.Instance;
import java.util.Collection;

/**
 *
 * @author phm
 * @param <DataType>
 */
public interface OfflineClassifier<DataType extends Instance> extends Classifier<DataType> {
    public void build (Collection<? extends DataType> dtst);
}
