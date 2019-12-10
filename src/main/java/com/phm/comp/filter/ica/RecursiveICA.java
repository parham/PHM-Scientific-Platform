
package com.phm.comp.filter.ica;

import com.phm.core.data.DatasetUtils;
import com.phm.comp.filter.DatasetFilter;
import java.util.LinkedList; 
import java.util.List;
import java.util.Random;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author phm
 */
public class RecursiveICA implements DatasetFilter {
    
    private Random rand = new Random(System.currentTimeMillis());
    
    public RealMatrix random (int row, int col) {
        RealMatrix w = new Array2DRowRealMatrix(row, col);
        for (int r = 0; r < w.getRowDimension(); r++) {
            for (int c = 0; c < w.getColumnDimension(); c++) {
                w.setEntry(r, c, rand.nextDouble());
            }
        }
        return w;
    }
    
    protected RealMatrix mTanh (RealMatrix d) {
        RealMatrix res = d.copy();
        for (int row = 0; row < res.getRowDimension(); row++) {
            for (int col = 0; col < res.getColumnDimension(); col++) {
                double temp = res.getEntry(row, col);
                res.setEntry (row, col, Math.tanh(temp));
            }
        }
        return res;
    }
    
    @Override
    public List<RealVector> filter (List<? extends RealVector> ds) {
        int nch = ds.get(0).getDimension();
        RealMatrix data = DatasetUtils.toMatrix(ds);
        RealMatrix w = random (nch, nch); //.subtract(random(nch, nch));
        w = w.subtract(random (nch, nch));

        double lmdinit = 0.995;
        double iterate_num = 0;
        int [] row_indexes = new int [nch]; // {0,1,2};
        for (int index = 0; index < nch; index++) {
            row_indexes [index] = index;
        }
        
        for (int f = 0; f < data.getColumnDimension(); f++) {
            iterate_num++;
            int [] col_indexes = {f};
            RealMatrix buffer = data.getSubMatrix (row_indexes, col_indexes);
//            Matrix buffer = data.getMatrix(row_indexes, f, f);
            RealMatrix y = w.multiply(buffer);
//            Matrix y = w.times(buffer);
            RealMatrix nonlin = mTanh (y);
//            Matrix nonlin = mTanh(y);
            RealMatrix gn = w.transpose().multiply(nonlin);
            double lmd = lmdinit / Math.pow(iterate_num, 0.7);
            double t = 1 + lmd * (nonlin.transpose().multiply(y).getEntry(0, 0) - 1);
            w = w.add(w.subtract(y.multiply(gn.transpose()).scalarMultiply(1/t)).scalarMultiply(lmd/(1-lmd)));
        }
        RealMatrix y = w.multiply (data.transpose());
        LinkedList<double []> dims = new LinkedList<>();
        for (int ch = 0; ch < y.getRowDimension(); ch++) {
            RealMatrix ye = y.getSubMatrix (ch, ch, 0, y.getColumnDimension() - 1);
            ye = ye.scalarMultiply(1 / ye.getNorm());
            double [] dimd = new double [ye.getColumnDimension()];
            for (int dim = 0; dim < dimd.length; dim++) {
                dimd [dim] = ye.getEntry (0, dim);
            }
            dims.add(dimd);
        }
        int lens = dims.get(0).length;
        for (int index = 0; index < lens; index++) {
            RealVector inst = new ArrayRealVector (dims.size());
            for (int ch = 0; ch < dims.size(); ch++) {
                inst.setEntry(ch, dims.get(ch) [index]);
            }
        }
        LinkedList<RealVector> dres = new LinkedList<>();
        for (int index = 0; index < y.getColumnDimension(); index++) {
            RealVector inst = new ArrayRealVector (y.getRowDimension());
            for (int ch = 0; ch < y.getRowDimension(); ch++) {
                inst.setEntry(ch, y.getEntry (ch, index));
            }
            dres.add (inst);
        }
        return dres;
    }
}
