
package com.phm.core.data;

import com.phm.annotations.ImplementationDetails;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

@ImplementationDetails (
    className = "Vector Instance",
    date = "10/20/2015",
    lastModification = "10/20/2015",
    version = "1.0.0",
    description = "This is an atomic part of the library that descripes basic vector of features."
)
public class Features extends RealVector implements VectorLikeInstance<Double>, Cloneable {
    
    protected ArrayRealVector data;
    private long timeObj = 0;
    private Object labelObj;
    
    public Features () {
        data = new ArrayRealVector();
    }
    public Features (double [] ft) {
        data = new ArrayRealVector (ft);
    }
    public Features (int ndim) {
        data = new ArrayRealVector (ndim);
    }
    public Features (RealVector v) {
        data = new ArrayRealVector (v);
    }
    public Features (long time, Object lbl) {
        this ();
        timeObj = time;
        labelObj = lbl;
    }
    public Features (long time, Object lbl, double [] ft) {
        this (ft);
        timeObj = time;
        labelObj = lbl;
    }
    public Features (long time, Object lbl, int ndim) {
        this (ndim);
        timeObj = time;
        labelObj = lbl;
    }
    public Features (long time, Object lbl, RealVector v) {
        this (v);
        timeObj = time;
        labelObj = lbl;
    }
    
    @Override
    public void setTime (long time) {
        timeObj = time;
    }
    @Override
    public long getTime () {
        return timeObj;
    }
    @Override
    public void setLabel (Object lbl) {
        labelObj = lbl;
    }
    @Override
    public Object getLabel () {
        return labelObj;
    }
    @Override
    public Features copy () {
        return new Features(getTime(), getLabel(), data.copy());
    }
    @Override
    public Features clone () {
        return copy ();
    }
    public RealMatrix toMatrix () {
        return new Array2DRowRealMatrix (toArray()).transpose();
    }
    @Override
    public String toString () {
        String str = timeObj + "\t" + (labelObj != null ? labelObj.toString() : "") + "\t[";
        for (int index = 0; index < getDimension() - 1; index++) {
            str += getEntry(index) + ",";
        }
        str += getEntry(getDimension() - 1) + "]";
        return str;
    }

    @Override
    public boolean code (Map<String, Object> map) {
        // Time
        map.put(PARAM_TIME, timeObj);
        // Label
        map.put(PARAM_LABEL, labelObj);
        // Features
        map.putAll(InstanceUtils.toMap(this));
        return true;
    }

    @Override
    public boolean decode(Map<String, Object> map) {
        // Time
        String timestr = (String) map.get(PARAM_TIME);
        if (timestr != null) {
            setTime(Long.valueOf(timestr));
        }
        // Label
        Object lbl = map.get(PARAM_LABEL);
        setLabel(lbl);
        // Features
        RealVector vec = InstanceUtils.create(map);
        setData(vec);
        return true;
    }

    @Override
    public boolean code(List<Object> list) {
        list.add(timeObj);
        list.add(labelObj);
        list.addAll(InstanceUtils.toList(this));
        return true;
    }

    @Override
    public boolean decode(List<Object> list) {
        String timestr = (String) list.get(0);
        if (timestr != null) {
            setTime(Long.valueOf(timestr));
        }
        Object lbl = list.get(1);
        setLabel(lbl);
        List<Object> flist = list.subList(2, list.size());
        RealVector tmp = InstanceUtils.create(flist);
        setData(tmp);
        return true;
    }

    public void setData (RealVector v) {
        data = new ArrayRealVector(v);
    }
    
    @Override
    public RealVector append(RealVector v) {
        return new Features(timeObj, labelObj, data.append(v));
    }

    @Override
    public RealVector append(double d) {
        return new Features(timeObj, labelObj, data.append(d));
    }

    @Override
    public RealVector getSubVector(int index, int n) throws NotPositiveException, OutOfRangeException {
        return new Features(timeObj, labelObj, data.getSubVector(index, n));
    }

    @Override
    public void setSubVector(int index, RealVector v) throws OutOfRangeException {
        data.setSubVector(index, v);
    }

    @Override
    public boolean isNaN() {
        return data.isNaN();
    }

    @Override
    public boolean isInfinite() {
        return data.isInfinite();
    }

    @Override
    public RealVector ebeDivide(RealVector v) throws DimensionMismatchException {
        return new Features(timeObj, labelObj, data.ebeDivide(v));
    }

    @Override
    public RealVector ebeMultiply(RealVector v) throws DimensionMismatchException {
        return new Features(timeObj, labelObj, data.ebeMultiply(v));
    }

    @Override
    public int getDimension() {
        return data.getDimension();
    }

    @Override
    public double getEntry(int index) throws OutOfRangeException {
        return data.getEntry(index);
    }

    @Override
    public void setEntry(int index, double value) throws OutOfRangeException {
        data.setEntry(index, value);
    }

    @Override
    public Double item(int index) {
        return data.getEntry(index);
    }

    @Override
    public List<Double> items() {
        LinkedList<Double> tmp = new LinkedList<>();
        for (int index = 0; index < getDimension(); index++) {
            tmp.add(data.getEntry(index));
        }
        return tmp;
    }
}
