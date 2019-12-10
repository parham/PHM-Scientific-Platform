
package com.phm.comp.window;

import com.phm.annotations.ImplementationDetails;
import com.phm.annotations.PublicationDetails;
import com.phm.annotations.PublicationType;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

@PublicationDetails (
        author = {"Fredric J. Harris"},
        type = PublicationType.Proceedings,
        title =  "On the use of Windows for Harmonic Analysis with the Discrete Fourier Transform",
        journal = "Proceedings of the IEEE",
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
        className = "ParzenWindow",
        date = "2015/10/10",
        lastModification = "2015/10/14",
        version = "1.0.0",
        description = "The Parzen window, also known as the de la Vallée Poussin window, is the 4th order B-spline window"
)
public class ParzenWindow extends Window {

    public ParzenWindow (int order) {
        super (order);
    }
    
    @Override
    public RealVector getWindow () {
        double [] weights = new double[orderObj];
        final int N = orderObj;
        for (int n = 0; n < N-1; n++) {
            if (0 <= n && n <= N/4) {
                
            }
        }
        return new ArrayRealVector(weights);
    }
}
