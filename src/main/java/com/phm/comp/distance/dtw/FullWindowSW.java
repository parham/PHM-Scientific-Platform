
package com.phm.comp.distance.dtw;

/**
 *
 * @author phm
 */
public class FullWindowSW extends SearchWindow {
    
    @Override
    public int getMinimumRowByColumn(int col) {
        return 0;
    }

    @Override
    public int getMaximumRowByColumn(int col) {
        return costMatrix.maxRow - 1;
    }

    @Override
    public int getMinimumColumnByRow(int row) {
        return 0;
    }

    @Override
    public int getMaximumColumnByRow(int row) {
        return costMatrix.maxColumn - 1;
    }

    @Override
    public boolean isValid(int row, int col) {
        return (row >= 0 && row < costMatrix.maxRow) &&
               (col >= 0 && col < costMatrix.maxColumn);
    }

    @Override
    protected void initWindow() {
        // Empty body
    }
}
