
package com.phm.comp.regression;

import java.util.Arrays;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.filter.DefaultMeasurementModel;
import org.apache.commons.math3.filter.DefaultProcessModel;
import org.apache.commons.math3.filter.KalmanFilter;
import org.apache.commons.math3.filter.MeasurementModel;
import org.apache.commons.math3.filter.ProcessModel;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * Publication details:
 * Authors: Lin, Z., Yang, Q., Guo, Z., Li, J.
 * Year: 2011
 * Title: An Improved Autoregressive Method with Kalman Filtering Theory for Vessel Motion Predication
 * Published In: International Journal of Intelligent Engineering and Systems
 * Volume: 4 (4)
 * @author Parham Nooralishahi - PHM!
 * @email parham.nooralishahi@gmail.com
 */
public class Lin2011AutoRegression extends DefaultAutoRegression {
    
    public Lin2011AutoRegression (int order) {
        super (order);
    }
    public Lin2011AutoRegression () {
        //Empty body
    }
    
    @Override
    protected double [] coefficientOpration (double [] inputs) {
        
        ////// Define Process Model
        // Transition Matrix : B
        RealMatrix B = new Array2DRowRealMatrix (orderObj, orderObj);
        for (int dim = 0; dim < orderObj; dim++) {
            B.setEntry(dim, dim, 1);
        }
        // Process Noise Covariance Matrix
        RealMatrix Q = new Array2DRowRealMatrix (orderObj, orderObj);
        NormalDistribution nd = new NormalDistribution(0, 0.4);
        for (int dim = 0; dim < orderObj; dim++) {
            Q.setEntry(dim, dim, nd.sample());
        }
        // Initial Error Matrix
        double [][] errs = new double[orderObj][orderObj];
        for (int y = 0; y < orderObj; y++) {
            for (int x = 0; x < orderObj; x++) {
                errs [x][y] = 1;
            }
        }
        RealMatrix P0 = new Array2DRowRealMatrix(errs);
        ProcessModel processModel = new DefaultProcessModel(B, null, Q, new ArrayRealVector(new double [orderObj]), P0);
        /////// Measurement Model
        // Measurement Matrix
        double [] hx = Arrays.copyOfRange(inputs, 0, orderObj);
        double [][] h = {hx};
//        double [][] h = new double[order][1];
//        for (int dim = 0; dim < order; dim++) {
//            h [dim][0] = hx [dim];
//        }
        RealMatrix H = new Array2DRowRealMatrix (h);
        // Measurement Noise Covariance Matrix
        NormalDistribution end = new NormalDistribution (0, 0.2);
        double ex = end.sample();
        double [][] e = {{ex}};
        RealMatrix E = new Array2DRowRealMatrix (e);
        MeasurementModel measureModel = new DefaultMeasurementModel(H, E);
        ///////////////////////
        KalmanFilter kf = new KalmanFilter(processModel, measureModel);
        for (int index = orderObj + 1; index < inputs.length; index++) {
            //System.out.println (index);
            double d_t = inputs [index];
            kf.predict();
            kf.correct(new double [] {d_t});
            double [] coeffs = kf.getStateEstimation();
            // Update for next iteration
            hx = Arrays.copyOfRange(inputs, index - orderObj, index);
            double [][] htmp = {hx};
            H = new Array2DRowRealMatrix (htmp);
            processModel = new DefaultProcessModel(B, null, Q, new ArrayRealVector(coeffs), P0);
            //processModel = new DefaultProcessModel(B, null, Q, new ArrayRealVector(coeffs), kf.getErrorCovarianceMatrix());
            measureModel = new DefaultMeasurementModel(H, E);
            kf = new KalmanFilter(processModel, measureModel);
        }
        
        //double [] coeffs = kf.getStateEstimation();
        return kf.getStateEstimation();
    }
}
