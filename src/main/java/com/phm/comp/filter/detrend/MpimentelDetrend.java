
package com.phm.comp.filter.detrend;

import com.phm.core.data.DatasetUtils;
import com.phm.comp.filter.DatasetFilter;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * Get detrended signal (eliminate DC component) 
 * @author mpimentel
 * @year 2011
 */
public class MpimentelDetrend implements DatasetFilter {
    
    @Override
    public List<RealVector> filter(List<? extends RealVector> data) {
        if (data.size() < 1) {
            return new LinkedList<>();
        }
        List<double []> channels = DatasetUtils.toChannelArrays(data);
        double [][] mat = new double[channels.size()][channels.get(0).length];
        for (int index = 0; index < channels.size(); index++) {
            double [] chs = channels.get(index);
            mat [index] = filter (chs);
        }
        return DatasetUtils.create(mat);
    }

    protected double [] filter (double [] signalin) {
        double [] signalaux = new double[signalin.length];
        double [][] a = new double [signalaux.length][2];
        double [][] x = new double[signalaux.length][1];
        // build regressor with linear pieces + DC
        for (int i = 0; i < signalin.length - 1; ++i){
            a[i][0] = 1.0 / (double) signalin.length;
            a[i][1] = 1.0;
            x[i][0] = signalin [i];
        }
        // definition of matrices
        RealMatrix A = new Array2DRowRealMatrix (a);
        RealMatrix X = new Array2DRowRealMatrix(x);
        
        RealMatrix PA = MatrixUtils.inverse (A);
        // remove the best fit
        RealMatrix Y = X.subtract(A.multiply(PA.multiply(X)));
        double[][] y = Y.getData();
        for (int i = 0; i < signalin.length - 1; ++i) {
            signalaux[i] = y[i][0];
        }
        return signalaux;
    }
}
