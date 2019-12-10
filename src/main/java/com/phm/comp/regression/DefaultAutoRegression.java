
package com.phm.comp.regression;
 
import com.phm.core.data.Features;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author Owner
 */
public class DefaultAutoRegression implements AutoRegression {
    
    protected int orderObj = 5;

    public DefaultAutoRegression (int o) {
        orderObj = o;
    }
    public DefaultAutoRegression () {
        // Empty Body
    }
    
    public void setOrder (int o) {
        orderObj = o;
    }
    public int getOrder () {
        return orderObj;
    }
    
    private double [] calculateLeastSquare (double [] inputseries, int order) throws Exception {

        int length = inputseries.length;

        double ar [] = null;
        double [] coef = new double [order];
        double [][] mat = new double [order][order];
	//create a symetric matrix of covariance values for the past timeseries elements  
        //and a vector with covariances between the past timeseries elements and the timeseries element to estimate.
        //start at "degree"-th sampel and repeat this for the length of the timeseries
        for (int i = order - 1; i < length - 1; i++) {
            for (int j = 0; j < order; j++) {
                coef[j] += inputseries[i + 1] * inputseries[i - j];
                for (int k = j; k < order; k++) { //start with k = j due to symmetry of the matrix...
                    mat[j][k] += inputseries[i - j] * inputseries[i - k];
                }
            }
        }
        //calculate the mean values for the matrix and the coefficients vector according to the length of the timeseries
        for (int i = 0; i < order; i++) {
            coef[i] /= (length - order);
            for (int j = i; j < order; j++) {
                mat[i][j] /= (length - order);
                mat[j][i] = mat[i][j]; //use the symmetry of the matrix
            }
        }
        RealMatrix matrix = new Array2DRowRealMatrix(mat);
        RealMatrix coefficients = new Array2DRowRealMatrix(coef);
        //solve the equation "matrix * X = coefficients", where x is the solution vector with the AR-coeffcients
        LUDecomposition sl = new LUDecomposition (coefficients);
        ar = sl.getSolver().solve(matrix).getColumn(0);
        return ar;
    }
    public List<double []> calculateCoefficients (List<? extends RealVector> inputs) {
        LinkedList<double []> coeffs = new LinkedList<>();
        int numdim = inputs.get(0).getDimension();
        for (int dim = 0; dim < numdim; dim++) {
            double [] ins = new double [inputs.size()];
            for (int index = 0; index < inputs.size(); index++) {
                ins [index] = inputs.get(index).getEntry(dim);
            }
            // Apply Least Squares
            double [] ctemp = coefficientOpration(ins);
            coeffs.add(ctemp);
        }
        return coeffs;
    }
    protected double [] coefficientOpration (double [] inputs) {
        try {
            return calculateLeastSquare (inputs, orderObj);
        } catch (Exception ex) {
            Logger.getLogger(DefaultAutoRegression.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
 
    protected RealVector estimate (List<? extends RealVector> inputs, List<double []> coeffs) {
        RealVector res = new ArrayRealVector(inputs.get(0).getDimension());
        for (int dim = 0; dim < res.getDimension(); dim++) {
            double [] coes = coeffs.get(dim);
            double estimate = 0.0;
            for (int index = 0; index < orderObj; index++) {
                double data = inputs.get(inputs.size() - (index + 1)).getEntry(dim);
                estimate += data * coes [index];
            }
            res.setEntry(dim, estimate);
//            res.put(dim, estimate);
        }
        return new Features (res);
    }

    @Override
    public RealVector estimate(List<? extends RealVector> inputs) {
        try {
            if (inputs.size() <= orderObj) {
                return null;
            }
            List<double []> coeffs = calculateCoefficients(inputs);
            return estimate(inputs, coeffs);
        } catch (Exception ex) {
            Logger.getLogger(DefaultAutoRegression.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<RealVector> simulate(List<? extends RealVector> inputs) {
        LinkedList<RealVector> res = new LinkedList<> ();
        try {
            List<double []> coeffs = calculateCoefficients(inputs);
            for (int index = 0; index < inputs.size() - orderObj; index++) {
                List<RealVector> data = new LinkedList<>(inputs.subList(index, index + orderObj));
                RealVector resd = estimate(data, coeffs);
                res.add(resd);
            }
        } catch (Exception ex) {
            Logger.getLogger(DefaultAutoRegression.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    @Override
    public List<RealVector> error (List<? extends RealVector> data) {
        List<RealVector> resd = simulate(data);
        int numdim = data.get(0).getDimension();
        for (int index = 0; index < data.size(); index++) {
            for (int dim = 0; dim < numdim; dim++) {
                double res = resd.get(index).getEntry(dim);
                double d = data.get(index + 1).getEntry(dim);
                resd.get(index).setEntry(dim, d - res);
            }
        }
        return resd;
    }
    
}
