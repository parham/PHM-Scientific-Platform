
package com.phm.core.data;

import com.phm.core.ArraySet;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author phm
 */
public class DatasetUtils {
    
    public static RealVector getMinimumInstance (Collection<? extends RealVector> ds) {
        LinkedList<RealVector> list = new LinkedList<>(ds);
        RealVector min = new ArrayRealVector(list.getFirst().getDimension());
        for (int ch = 0; ch < min.getDimension(); ch++) {
            for (RealVector x : ds) {
                if (min.getEntry(ch) > x.getEntry(ch)) {
                    min.setEntry(ch, x.getEntry(ch));
                }
            }
        }
        return min;
    }
    public static RealVector getMaximumInstance (Collection<? extends RealVector> ds) {
        LinkedList<RealVector> list = new LinkedList<>(ds);
        RealVector max = new ArrayRealVector(list.getFirst().getDimension());
        for (int ch = 0; ch < max.getDimension(); ch++) {
            for (RealVector x : ds) {
                if (max.getEntry(ch) < x.getEntry(ch)) {
                    max.setEntry(ch, x.getEntry(ch));
                }
            }
        }
        return max;
    }
    
    public static <DataType extends RealVector> List<DataType> deepcopy (Collection<DataType> ds) {
        LinkedList<DataType> res = new LinkedList<>();
        ds.stream().forEach((DataType x) -> {
            res.add((DataType) x.copy());
        });
        return res;
    }
    public static <DataType extends Instance> Set<Object> classes (Collection<DataType> ds) {
        ArraySet<Object> arrc = new ArraySet<>();
        ds.stream().forEach((Instance f) -> {
            arrc.add(f.getLabel());
        });
        return arrc;
    }
    public static <DataType extends RealVector> List<RealVector> toChannelRows (List<DataType> ds) {
        LinkedList<RealVector> results = new LinkedList<>();
        int numsamples = ds.size();
        int numchannels = ds.get(0).getDimension();
        for (int ch = 0; ch < numchannels; ch++) {
            double [] tmp = new double[numsamples];
            for (int index = 0; index < numsamples; index++) {
                tmp [index] = ds.get(index).getEntry(ch);
            }
            results.add(new ArrayRealVector(tmp));
        }
        return results;
    }
    public static <DataType extends RealVector> List<Features> channelsToFeatures (List<DataType> ds) {
        LinkedList<Features> fts = new LinkedList<>();
        int numchannels = ds.size();
        int numsamples = ds.get(0).getDimension();
        for (int index = 0; index < numsamples; index++) {
            double [] tmp = new double [numchannels];
            for (int ch = 0; ch < numchannels; ch++) {
                tmp [ch] = ds.get(ch).getEntry(index);
            }
            fts.add(new Features(tmp));
        }
        return fts;
    }
    public static <DataType extends RealVector> List<double []> toChannelArrays (List<DataType> vds) {
        LinkedList<double []> list = new LinkedList<>();
        int nch = vds.get(0).getDimension();
        for (int ch = 0; ch < nch; ch++) {
            double [] chs = new double [vds.size()];
            for (int index = 0; index < vds.size (); index++) {
                chs [index] = vds.get(index).getEntry(ch);
            }
            list.add(chs);
        }
        return list;
    }
    public static <DataType extends RealVector> RealMatrix toMatrix (List<DataType> ds) {
        int col = ds.get(0).getDimension();
        int row = ds.size();
        RealMatrix data = new Array2DRowRealMatrix (row, col);
        for (int index = 0; index < ds.size(); index++) {
            data.setRowVector(index, ds.get(index));
        }
        
        return data;
    }
    public static List<RealVector> create (double [][] data) {
        LinkedList<RealVector> res = new LinkedList<>();
        if (data != null && data.length > 0) {
            RealMatrix mat = new Array2DRowRealMatrix(data);
            for (int c = 0; c < mat.getColumnDimension(); c++) {
                res.add(new ArrayRealVector(mat.getColumn(c)));
            }
        }
        return res;
    }
    public static List<RealVector> create (RealMatrix data) {
        return create(data.getData());
    }
}
