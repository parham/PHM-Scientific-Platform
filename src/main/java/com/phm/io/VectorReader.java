
package com.phm.io;

import java.util.Collection;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author Parham Nooralishahi - PHM!
 */
public interface VectorReader {
    public boolean read (Collection<RealVector> ds);
}
