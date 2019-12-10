
package com.phm.comp.window;

import com.phm.annotations.ImplementationDetails;
import com.phm.annotations.PublicationDetails;
import com.phm.annotations.PublicationType;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

@PublicationDetails (
        author = {"Fredric J. Harris"},
        title =  "On the use of Windows for Harmonic Analysis with the Discrete Fourier Transform",
        journal = "Proceedings of the IEEE",
        type = PublicationType.Proceedings,
        volume = "66(1)",
        year = "1978",
        pages = "51 - 83",
        customData = {"This paper makes available a concise review of data windows and their affect on the detection of " +
                        "harmonic signals in the presence of broad-band noise, and in the presence of nearby strong harmonic " +
                        "interference. We also call attention to a number of common errors in the application of windows when " +
                        "used with the fast Fourier transform. This paper includes a comprehensive catalog of data windows " +
                        "along with their significant performance parameters from which the different windows can be compared. " +
                        "Finally, an example demonstrates the use and value of windows to resolve closely spaced harmonic " + 
                        "signals characterized by large differences in amplitude."}
)
@ImplementationDetails (
        className = "TriangularWindow",
        date = "2015/10/10",
        lastModification = "2015/10/14",
        version = "1.0.0"
)
public class TriangularWindow extends Window {

    protected double initValue = 1.0;
    protected LOption L;
    
    public TriangularWindow (int order, double initv, LOption l) {
        super (order);
        initValue = initv;
        L = l;
    }
    
    public double getInitialValue () {
        return initValue;
    }
    public LOption getL() {
        return L;
    }
    
    @Override
    public RealVector getWindow () {
        double [] result = new double [orderObj];
        double l = (double) orderObj;
        switch (L) {
            case N_minus_1: l -= 1; break;
            case N_plus_1: l += 1; break;
        }
        for (int n = 0; n < l; n++) {
            result [n] = initValue - Math.abs(((double) n - (((double) orderObj - 1) / 2)) / (l / 2));
        }
        return new ArrayRealVector(result);
    }
    
    public static enum LOption {
        N_minus_1,
        N,
        N_plus_1;
    }
}
