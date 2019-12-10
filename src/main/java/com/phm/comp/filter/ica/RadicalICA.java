
package com.phm.comp.filter.ica;

import com.phm.core.data.DatasetUtils;
import com.phm.comp.filter.DatasetFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.apache.commons.math3.analysis.function.Log;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.stat.correlation.Covariance;

/**
 *
 * @author Erik G. Learned-Miller
 * @since 2003
 */
public class RadicalICA implements DatasetFilter {
    
//    % The recommended default parameter values are:
//    % K=150;
//    % AUG_FLAG=1;
//    % reps=30;
//    % stdev=0.175;
    
    protected int numberOfAnglesEvaluate = 150;
    protected int augFlag = 1;
    protected int numberOfReplicatedPoints = 30;
    protected double stdReplicatedPoints = 0.175;
    
    private double thetaStar = 0;
    private RealMatrix rotStar;
    private RealMatrix Wopt;
    
    public void setNumberOfAnglesEvaluate (int nae) {
        numberOfAnglesEvaluate = nae;
    }
    public int getNumberOfAnglesEvaluate () {
        return numberOfAnglesEvaluate;
    }
    public void setAuGFlag (int af) {
        augFlag = af;
    }
    public int getAUGFlag () {
        return augFlag;
    }
    public void setNumberOfReplicatedPoints (int nrp) {
        numberOfReplicatedPoints = nrp;
    }
    public int getNumberOfReplicatedPoints () {
        return numberOfReplicatedPoints;
    }
    public void setSTDReplicatedPoints (double std) {
        stdReplicatedPoints = std;
    }
    public double getSTDReplicatedPoints () {
        return stdReplicatedPoints;
    }
    
    protected void radicalOptTheta (RealMatrix x, double stdev, int m, int reps, int k, double range) {
        // m is the number of intervals in an m-spacing
        // reps is the number of points used in smoothing
        // K is the number of angles theta to check for each Jacobi rotation.
        int d = x.getRowDimension();
        int N = x.getColumnDimension();
        // This routine assumes that it gets whitened data.
        // First, we augment the points with reps near copies of each point.
        RealMatrix xAug;
        if (reps == 1) {
            xAug = x.copy();
        } else {
            xAug = random(d, N * reps).scalarMultiply(stdev).add(repmat(x, 1, reps));
        }
        // Then we rotate this data to various angles, evaluate the sum of 
        // the marginals, and take the min.
        double perc = range / (Math.PI / 2);
        double numberK = perc * k;
        int start = (int) Math.floor ((k / 2) - (numberK / 2) + 1);
        int endPt = (int) Math.ceil((k / 2) + (numberK / 2));
        LinkedList<ENTItem> ent = new LinkedList<>();
        for (int i = 0; i < k; i++) { // i = 1
            // Map theta from -pi/4 to pi/4 instead of 0 to pi/2.
            // This will allow us to use Amari-distance for test of
            // convergence.
            double theta = (i - 1) / (k - 1) * (Math.PI / 2) - (Math.PI / 4);
            RealMatrix rot = new Array2DRowRealMatrix(2, 2);
            rot.setEntry(0, 0, Math.cos(theta));
            rot.setEntry(0, 1, -1 * Math.sin(theta));
            rot.setEntry(1, 0, Math.sin(theta));
            rot.setEntry(1, 1, Math.cos(theta));
            RealMatrix rotPts = rot.multiply(xAug);
            RealVector marginalAtTheta = new ArrayRealVector(d);
            double tmpsum = 0;
            for (int j = 0; j < d; j++) {
                double tt = vasicekm (new ArrayRealVector(rotPts.getRow(j)), m);
                marginalAtTheta.setEntry(j, tt);
                tmpsum += tt;
            }
            ent.add(new ENTItem(i, tmpsum));
        }
        Collections.sort(ent);
        double target = ent.getFirst().index;
        thetaStar = (target - 1) / (k - 1) * (Math.PI / 2) - (Math.PI / 4);
        rotStar = new Array2DRowRealMatrix(2, 2);
        rotStar.setEntry(0, 0, Math.cos(thetaStar));
        rotStar.setEntry(0, 1, -1 * Math.sin(thetaStar));
        rotStar.setEntry(1, 0, Math.sin(thetaStar));
        rotStar.setEntry(1, 1, Math.cos(thetaStar));
    }
    protected double vasicekm (RealVector v, int m) {
        int len = v.getDimension();
        double [] vals = v.toArray();
        Arrays.sort (vals);
        // Note that the intervals overlap for this estimator.
        RealVector tvals = new ArrayRealVector(vals);
        RealVector intvals = tvals.getSubVector(m + 1, tvals.getDimension() - m + 1).subtract (tvals.getSubVector(1, tvals.getDimension() - m - 1));
        RealVector hvec = intvals.map(new Log());
        double h = 0;
        for (int dim = 0; dim < hvec.getDimension(); dim++) {
            h += hvec.getEntry(dim);
        }
        return h;
    }
    
    protected RealMatrix random (int row, int col) {
        RealMatrix mat = new Array2DRowRealMatrix(row, col);
        Random rand = new Random(System.currentTimeMillis());
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {
                mat.setEntry(r, c, rand.nextDouble());
            }
        }
        return mat;
    }
    protected RealMatrix repmat (RealMatrix mat, int nrow, int ncol) {
        RealMatrix res = new Array2DRowRealMatrix(mat.getRowDimension() * nrow, 
                                                  mat.getColumnDimension() * ncol);
        int row = mat.getRowDimension();
        int col = mat.getColumnDimension();
        for (int r = 0; r < res.getRowDimension(); r++) {
            for (int c = 0; c < res.getColumnDimension(); c++) {
                int tr = r % row;
                int tc = c % col;
                res.setEntry(r, c, mat.getEntry(tr, tc));
            }
        }
        return res;
    }
    
    public RealMatrix getWeightMatrix () {
        return Wopt;
    }
    
    public static RealMatrix scalarPowerEqual (RealMatrix mat, double p) {
        for (int r = 0; r < mat.getRowDimension(); r++) {
            for (int c = 0; c < mat.getColumnDimension(); c++) {
                mat.setEntry(r, c, Math.pow (mat.getEntry(r, c), p));
            }
        }
        return mat;
    }
    
    @Override
    public List<RealVector> filter (List<? extends RealVector> ds) {
        RealMatrix x = DatasetUtils.toMatrix (ds);
        // When AUG_FLAG is off, do not augment data. Use original data only.
        if (augFlag == 0) {
            numberOfReplicatedPoints = 1;
        }
        int dim = x.getRowDimension();
        int N = x.getColumnDimension();
        double m = Math.floor (Math.sqrt(N));
        // Whiten the data. Store the whitening operation to combine with
        // rotation matrix for total solution.
        Covariance cov = new Covariance(x.transpose());
        SingularValueDecomposition svd = new SingularValueDecomposition(cov.getCovarianceMatrix());
        RealMatrix s = svd.getS();
        RealMatrix u = svd.getU();
        RealMatrix v = svd.getV();
        ////////////////////////
        // Whitening_mat=v*s^(-.5)*u'
        RealMatrix Whitening_mat = v.multiply(scalarPowerEqual(s, -0.5)).multiply(u.transpose());
        // X_white=Whitening_mat*X
        RealMatrix X_white = Whitening_mat.multiply(x);
        int sweeps = dim - 1;
        RealMatrix oldTotalRot = MatrixUtils.createRealIdentityMatrix(dim);
        double sweepIter = 0;
        RealMatrix totalRot = MatrixUtils.createRealIdentityMatrix(dim);
        RealMatrix xcur = X_white;
	// K represents the number of rotations to examine on the FINAL
	// sweep. To optimize performance, we start with a smaller number of
	// rotations to examine. Then, we increase the
	// number of angles to get better resolution as we get closer to the
	// solution. For the first half of the sweeps, we use a constant
	// number for K. Then we increase it exponentially toward the finish.
        int finalK = numberOfAnglesEvaluate;
        double startKfloat = finalK / Math.pow(1.3, Math.ceil(sweeps / 2));
        double newKfloat = startKfloat;
        for (int sweepNum = 1; sweepNum < sweeps; sweepNum++) {
            double range = Math.PI / 2.0;
            double newK = 0.0;
            // Compute number of angle samples for this sweep.
            if (sweepNum > (sweeps / 2)) {
                newKfloat = newKfloat * 1.3;
                newK = Math.floor(newKfloat);
            } else {
                newKfloat = startKfloat;
                newK= Math.max(30, Math.floor(newKfloat));
            }
            // Iterate over all possible Jacobi rotations.
            int [] cols = new int [xcur.getColumnDimension()];
            for (int index = 0; index < cols.length; index++) {
                cols [index] = index;
            }
            for (int i = 0; i < dim - 1; i++) {
                for (int j = i + 1; j < dim; j++) {
                    // Extract dimensions (i,j) from the current data.
                    int [] rows = {i, j};
                    // Find the best angle theta for this Jacobi rotation
                    RealMatrix curSubSpace = xcur.getSubMatrix(rows, cols);
                    radicalOptTheta (curSubSpace, stdReplicatedPoints, (int) m, 
                                     numberOfReplicatedPoints, (int) newK, range);
                    // Incorporate Jacobi rotation into solution.
                    RealMatrix newRotComponent = MatrixUtils.createRealIdentityMatrix(dim);
                    newRotComponent.setEntry(i, i, Math.cos(thetaStar));
                    newRotComponent.setEntry(i, j, -1 * Math.sin(thetaStar));
                    newRotComponent.setEntry(j, i, Math.sin(thetaStar));
                    newRotComponent.setEntry(j, j, Math.cos(thetaStar));
                    totalRot = newRotComponent.multiply(totalRot);
                    xcur = totalRot.multiply(X_white);
                }
            }
            oldTotalRot = totalRot;
        }
        Wopt = totalRot.multiply(Whitening_mat);
        RealMatrix yopt = Wopt.multiply(x);
        
        return new LinkedList<>(DatasetUtils.create (yopt));
    }
    
    class ENTItem implements Comparable<ENTItem> {
        public double ent;
        public int index;
        public ENTItem (int ind, double e) {
            ent = e;
            index = ind;
        }

        @Override
        public int compareTo(ENTItem o) {
            return (int) (this.ent - o.ent);
        }
        
    }
}
