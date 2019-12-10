
package com.phm.comp.distance.dtw;

/**
 *
 * @author phm
 */
public class CostMatrix {
    public final double [][] cost;
    public final int maxRow;
    public final int maxColumn;
    
    public CostMatrix (int nrow, int ncol) {
        cost = new double [nrow][ncol];
        maxRow = nrow;
        maxColumn = ncol;
    }
    
    public void set (int row, int col, double value) {
        cost [row][col] = value;
    }
    public double get (int row, int col) {
        return cost [row][col];
    }
}
