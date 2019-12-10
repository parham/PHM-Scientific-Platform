
package com.phm.comp.test;

import com.phm.comp.filter.ica.fastica.BelowEVFilter;
import com.phm.comp.filter.ica.fastica.CompositeEVFilter;
import com.phm.comp.filter.ica.fastica.FastICA;
import com.phm.comp.filter.ica.fastica.FastICAConfig;
import com.phm.comp.filter.ica.fastica.Power3CFunction;
import com.phm.comp.filter.ica.fastica.SortingEVFilter;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

/**
 *
 * @author phm
 */
public class TestFastICA {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RealMatrix mat = new Array2DRowRealMatrix(2, 3);
        int index = 0;
        for (int r = 0; r < mat.getRowDimension(); r++) {
            for (int c = 0; c < mat.getColumnDimension(); c++) {
                mat.setEntry(r, c, index++);
            }
        }
        print (mat);
        System.out.println ();
        print (repmat(mat, 1, 2));
//        print (mat.getData());
//        System.out.println ();
//        for (int r = 0; r < mat.getColumnDimension(); r++) {
//            print (mat.getColumn(r));
//        }
//        System.out.println ();
//        ////////////////////////////////////////////////////////
//        CompositeEVFilter filter = new CompositeEVFilter();
//        filter.add(new BelowEVFilter(1.0e-8, false));
//        filter.add(new SortingEVFilter(true, true));
//        FastICAConfig config = new FastICAConfig (3, FastICAConfig.Approach.DEFLATION, 
//                                                  1.0, 1.0e-16, 1000, null);
//        System.out.println("Performing ICA");
//        FastICA fica = new FastICA (config, new Power3CFunction(), filter);
//        System.out.println();
//        fica.filter(new Dataset());
    }
    
    public static void print (double [] arr) {
        for (int r = 0; r < arr.length; r++) {
            System.out.print (arr [r] + "\t");
        }
        System.out.println ();
    }
    public static void print (RealMatrix mat) {
        for (int r = 0; r < mat.getRowDimension(); r++) {
            for (int c = 0; c < mat.getColumnDimension(); c++) {
                System.out.print (mat.getEntry(r, c) + "\t");
            }
            System.out.println ();
        }
    }
    public static void print (double [][] mat) {
        for (int r = 0; r < mat.length; r++) {
            for (int c = 0; c < mat[r].length; c++) {
                System.out.print (mat [r][c] + "\t");
            }
            System.out.println ();
        }
    }
    
    public static RealMatrix repmat (RealMatrix mat, int nrow, int ncol) {
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
}
