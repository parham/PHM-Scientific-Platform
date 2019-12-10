
package com.phm.comp.distance.dtw;

import java.util.Objects;

/**
 *
 * @author phm
 */
public abstract class SearchWindow {
    public CostMatrix costMatrix;
    
    public void initialize (CostMatrix cost) {
        costMatrix = Objects.requireNonNull(cost);
        initWindow();
    }

    protected abstract void initWindow ();
    
    public abstract int getMinimumRowByColumn (int col);
    public abstract int getMaximumRowByColumn (int col);
    public abstract int getMinimumColumnByRow (int row);
    public abstract int getMaximumColumnByRow (int row);
    public abstract boolean isValid (int row, int col);
    
    public boolean set (int row, int col, double value) {
        if (isValid(row, col)) {
            costMatrix.set(row, col, value);
            return true;
        }
        return false;
    }
    public double get (int row, int col) {
        if (isValid(row, col)) {
            return costMatrix.get(row, col);
        }
        return Float.NaN;
    }
}
