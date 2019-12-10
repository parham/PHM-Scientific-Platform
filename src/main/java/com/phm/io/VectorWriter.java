
package com.phm.io;

import java.util.Collection;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author PHM
 */
public interface VectorWriter {
    boolean write (RealVector d);
    boolean write (Collection<? extends RealVector> ds);
}