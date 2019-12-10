
package com.phm.core.data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author phm
 */
public class InstanceUtils {
    
    public static final String PARAM_FEATURE_LENGTH = "feature.length";
    
    public static <DataType extends Features> DataType deepcopy (DataType src) {
        return (DataType) src.copy();
    }
    public static <DataType extends RealVector> RealMatrix toMatrix (DataType src) {
        return new Array2DRowRealMatrix (src.toArray()).transpose();
    }
    public static <DataType extends RealVector> RealMatrix toMatrix (double [] src) {
        return new Array2DRowRealMatrix (src).transpose();
    }
    public static <DataType extends RealVector> Map<String, Object> toMap (DataType src) {
        HashMap<String, Object> map = new HashMap<>();
        int fnum = src.getDimension();
        map.put(PARAM_FEATURE_LENGTH, fnum);
        for (int index = 0; index < fnum; index++) {
            map.put(String.valueOf(String.valueOf(index)), src.getEntry(index));
        }
        return map;
    }
    public static <DataType extends RealVector> List<Object> toList (DataType src) {
        LinkedList<Object> list = new LinkedList<>();
        int fnum = src.getDimension();
        list.add(String.valueOf(fnum));
        for (int index = 0; index < src.getDimension(); index++) {
            list.add (String.valueOf(src.getEntry(index)));
        }
        return list;
    }
    public static RealVector create (List<Object> list) {
        int fnum = Integer.valueOf((String) list.get(0));
        RealVector vec = new ArrayRealVector(fnum);
        int vecnum = Math.min(list.size() - 1, fnum);
        for (int index = 0; index < vecnum; index++) {
            vec.setEntry(index, Double.valueOf((String) list.get(index + 1)));
        }
        return vec;
    }
    public static RealVector create (Map<String, Object> map) {
        int fnum = (int) map.get(PARAM_FEATURE_LENGTH);
        RealVector res = new ArrayRealVector(fnum);
        for (int index = 0; index < fnum; index++) {
            double f = Double.valueOf((String) map.get(String.valueOf(index)));
            res.setEntry(index, f);
        }
        return res;
    }
}
