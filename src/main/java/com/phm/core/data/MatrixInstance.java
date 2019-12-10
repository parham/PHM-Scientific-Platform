
package com.phm.core.data;

import java.util.List;
import java.util.Map;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixDimensionMismatchException;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixChangingVisitor;
import org.apache.commons.math3.linear.RealMatrixPreservingVisitor;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author phm
 */
public class MatrixInstance implements Instance, Cloneable, RealMatrix {

    public static final String PARAM_MATRIX = "instance.matrix";
    
    protected Array2DRowRealMatrix dataObj = new Array2DRowRealMatrix();
    private long timeObj = System.currentTimeMillis();
    private Object labelObj;
    
    public MatrixInstance (long time, Object lbl) {
        setTime(time);
        setLabel(lbl);
    }
    public MatrixInstance (long time, Object lbl, RealMatrix vs) {
        dataObj = new Array2DRowRealMatrix(vs.getData());
        setLabel(lbl);
        setTime(time);
    }
    public MatrixInstance (RealMatrix vs) {
        dataObj = new Array2DRowRealMatrix(vs.getData());
    }
    public MatrixInstance (int row, int col) {
        dataObj = new Array2DRowRealMatrix(row, col);
    }
    public MatrixInstance (double [] v) {
        dataObj = new Array2DRowRealMatrix(v);
    }
    public MatrixInstance () {
        // Empty body
    }
    
    @Override
    public void setTime(long time) {
        timeObj = time;
    }

    @Override
    public long getTime() {
        return timeObj;
    }

    @Override
    public void setLabel(Object lbl) {
        labelObj = lbl;
    }

    @Override
    public Object getLabel() {
        return labelObj;
    }

    @Override
    public MatrixInstance copy() {
        return new MatrixInstance(timeObj, labelObj, this);
    }

    @Override
    public boolean code(Map<String, Object> map) {
        map.put(PARAM_TIME, getTime());
        map.put(PARAM_LABEL, getLabel());
        map.put(PARAM_MATRIX, this);
        return true;
    }

    @Override
    public boolean code(List<Object> list) {
        list.add(getTime());
        list.add(getLabel());
        list.add (this);
        return true;
    }

    @Override
    public boolean decode(Map<String, Object> map) {
        setTime((long) map.get(PARAM_TIME));
        setLabel(map.get(PARAM_LABEL));
        dataObj = new Array2DRowRealMatrix(((RealMatrix) map.get(PARAM_MATRIX)).getData());
        return true;
    }

    @Override
    public boolean decode(List<Object> list) {
        setTime((long) list.get(0));
        setLabel(list.get(1));
        dataObj = new Array2DRowRealMatrix(((RealMatrix) list.get(2)).getData());
        return true;
    }

    @Override
    public RealMatrix createMatrix(int rowDimension, int columnDimension) throws NotStrictlyPositiveException {
        return new Array2DRowRealMatrix(rowDimension, columnDimension);
    }

    @Override
    public RealMatrix add(RealMatrix m) throws MatrixDimensionMismatchException {
        return dataObj.add(m);
    }

    @Override
    public RealMatrix subtract(RealMatrix m) throws MatrixDimensionMismatchException {
        return dataObj.subtract(m);
    }

    @Override
    public RealMatrix scalarAdd(double d) {
        return dataObj.scalarAdd(d);
    }

    @Override
    public RealMatrix scalarMultiply(double d) {
        return dataObj.scalarMultiply(d);
    }

    @Override
    public RealMatrix multiply(RealMatrix m) throws DimensionMismatchException {
        return dataObj.multiply(m);
    }

    @Override
    public RealMatrix preMultiply(RealMatrix m) throws DimensionMismatchException {
        return dataObj.preMultiply(m);
    }

    @Override
    public RealMatrix power(int p) throws NotPositiveException, NonSquareMatrixException {
        return dataObj.power(p);
    }

    @Override
    public double[][] getData() {
        return dataObj.getData();
    }

    @Override
    public double getNorm() {
        return dataObj.getNorm();
    }

    @Override
    public double getFrobeniusNorm() {
        return dataObj.getFrobeniusNorm();
    }

    @Override
    public RealMatrix getSubMatrix(int startRow, int endRow, int startColumn, int endColumn) throws OutOfRangeException, NumberIsTooSmallException {
        return dataObj.getSubMatrix(startRow, endRow, startColumn, endColumn);
    }

    @Override
    public RealMatrix getSubMatrix(int[] selectedRows, int[] selectedColumns) throws NullArgumentException, NoDataException, OutOfRangeException {
        return dataObj.getSubMatrix(selectedRows, selectedColumns);
    }

    @Override
    public void copySubMatrix(int startRow, int endRow, int startColumn, int endColumn, double[][] destination) throws OutOfRangeException, NumberIsTooSmallException, MatrixDimensionMismatchException {
        dataObj.copySubMatrix(startRow, endRow, startColumn, endColumn, destination);
    }

    @Override
    public void copySubMatrix(int[] selectedRows, int[] selectedColumns, double[][] destination) throws OutOfRangeException, NullArgumentException, NoDataException, MatrixDimensionMismatchException {
        dataObj.copySubMatrix(selectedRows, selectedColumns, destination);
    }

    @Override
    public void setSubMatrix(double[][] subMatrix, int row, int column) throws NoDataException, OutOfRangeException, DimensionMismatchException, NullArgumentException {
        dataObj.setSubMatrix(subMatrix, row, column);
    }

    @Override
    public RealMatrix getRowMatrix(int row) throws OutOfRangeException {
        return dataObj.getRowMatrix(row);
    }

    @Override
    public void setRowMatrix(int row, RealMatrix matrix) throws OutOfRangeException, MatrixDimensionMismatchException {
        dataObj.setRowMatrix(row, matrix);
    }

    @Override
    public RealMatrix getColumnMatrix(int column) throws OutOfRangeException {
        return dataObj.getColumnMatrix(column);
    }

    @Override
    public void setColumnMatrix(int column, RealMatrix matrix) throws OutOfRangeException, MatrixDimensionMismatchException {
        dataObj.setColumnMatrix(column, matrix);
    }

    @Override
    public RealVector getRowVector(int row) throws OutOfRangeException {
        return dataObj.getRowVector(row);
    }

    @Override
    public void setRowVector(int row, RealVector vector) throws OutOfRangeException, MatrixDimensionMismatchException {
        dataObj.setRowVector(row, vector);
    }

    @Override
    public RealVector getColumnVector(int column) throws OutOfRangeException {
        return dataObj.getColumnVector(column);
    }

    @Override
    public void setColumnVector(int column, RealVector vector) throws OutOfRangeException, MatrixDimensionMismatchException {
        dataObj.setColumnVector(column, vector);
    }

    @Override
    public double[] getRow(int row) throws OutOfRangeException {
        return dataObj.getRow(row);
    }

    @Override
    public void setRow(int row, double[] array) throws OutOfRangeException, MatrixDimensionMismatchException {
        dataObj.setRow(row, array);
    }

    @Override
    public double[] getColumn(int column) throws OutOfRangeException {
        return dataObj.getColumn(column);
    }

    @Override
    public void setColumn(int column, double[] array) throws OutOfRangeException, MatrixDimensionMismatchException {
        dataObj.setColumn(column, array);
    }

    @Override
    public double getEntry(int row, int column) throws OutOfRangeException {
        return dataObj.getEntry(row, column);
    }

    @Override
    public void setEntry(int row, int column, double value) throws OutOfRangeException {
        dataObj.setEntry(row, column, value);
    }

    @Override
    public void addToEntry(int row, int column, double increment) throws OutOfRangeException {
        dataObj.addToEntry(row, column, increment);
    }

    @Override
    public void multiplyEntry(int row, int column, double factor) throws OutOfRangeException {
        dataObj.multiplyEntry(row, column, factor);
    }

    @Override
    public RealMatrix transpose() {
        return dataObj.transpose();
    }

    @Override
    public double getTrace() throws NonSquareMatrixException {
        return dataObj.getTrace();
    }

    @Override
    public double[] operate(double[] v) throws DimensionMismatchException {
        return dataObj.operate(v);
    }

    @Override
    public RealVector operate(RealVector v) throws DimensionMismatchException {
        return dataObj.operate(v);
    }

    @Override
    public double[] preMultiply(double[] v) throws DimensionMismatchException {
        return dataObj.preMultiply(v);
    }

    @Override
    public RealVector preMultiply(RealVector v) throws DimensionMismatchException {
        return dataObj.preMultiply(v);
    }

    @Override
    public double walkInRowOrder(RealMatrixChangingVisitor visitor) {
        return dataObj.walkInRowOrder(visitor);
    }

    @Override
    public double walkInRowOrder(RealMatrixPreservingVisitor visitor) {
        return dataObj.walkInRowOrder(visitor);
    }

    @Override
    public double walkInRowOrder(RealMatrixChangingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws OutOfRangeException, NumberIsTooSmallException {
        return dataObj.walkInRowOrder(visitor, startRow, endRow, startColumn, endColumn);
    }

    @Override
    public double walkInRowOrder(RealMatrixPreservingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws OutOfRangeException, NumberIsTooSmallException {
        return dataObj.walkInRowOrder(visitor, startRow, endRow, startColumn, endColumn);
    }

    @Override
    public double walkInColumnOrder(RealMatrixChangingVisitor visitor) {
        return dataObj.walkInColumnOrder(visitor);
    }

    @Override
    public double walkInColumnOrder(RealMatrixPreservingVisitor visitor) {
        return dataObj.walkInColumnOrder(visitor);
    }

    @Override
    public double walkInColumnOrder(RealMatrixChangingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws OutOfRangeException, NumberIsTooSmallException {
        return dataObj.walkInColumnOrder(visitor, startRow, endRow, startColumn, endColumn);
    }

    @Override
    public double walkInColumnOrder(RealMatrixPreservingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws OutOfRangeException, NumberIsTooSmallException {
        return dataObj.walkInColumnOrder(visitor, startRow, endRow, startColumn, endColumn);
    }

    @Override
    public double walkInOptimizedOrder(RealMatrixChangingVisitor visitor) {
        return dataObj.walkInOptimizedOrder(visitor);
    }

    @Override
    public double walkInOptimizedOrder(RealMatrixPreservingVisitor visitor) {
        return dataObj.walkInOptimizedOrder(visitor);
    }

    @Override
    public double walkInOptimizedOrder(RealMatrixChangingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws OutOfRangeException, NumberIsTooSmallException {
        return dataObj.walkInOptimizedOrder(visitor, startRow, endRow, startColumn, endColumn);
    }

    @Override
    public double walkInOptimizedOrder(RealMatrixPreservingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws OutOfRangeException, NumberIsTooSmallException {
        return dataObj.walkInOptimizedOrder(visitor, startRow, endRow, startColumn, endColumn);
    }

    @Override
    public boolean isSquare() {
        return dataObj.isSquare();
    }

    @Override
    public int getRowDimension() {
        return dataObj.getRowDimension();
    }

    @Override
    public int getColumnDimension() {
        return dataObj.getColumnDimension();
    }
}
