package com.phm.comp.filter.welch;

import java.util.List;
import org.apache.commons.math3.complex.Complex;

/**
 * Abstract layer for the filters
 *
 * @author knoodrake
 *
 */
public abstract class Filter {

    protected final static int X = 0;
    protected final static int Y = 1;
    protected final static int Z = 2;

    public static class Coord {

        double x;
        double y;

        public Coord(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Returns the squared value
     *
     * @param x
     */
    protected static double sq(double x) {
        return x * x;
    }

    /**
     * Returns the absolute value
     *
     * @param x
     */
    protected static double abs(double x) {
        return Math.abs(x);
    }

    /**
     * Computes the squar root
     *
     * @param x
     */
    protected static double sqrt(double x) {
        return Math.sqrt(x);
    }

    /**
     * Computes the module of a complex
     *
     * @param c
     */
    public static double module(Complex c) {
        return sqrt(sq(c.getImaginary()) + sq(c.getReal()));
    }

    /**
     * Computes the variance
     *
     * @param ds
     */
    public static double variance(double[] ds) {
        double mean = average(ds);
        double v = 0;
        for (double i : ds) {
            v += sq(mean - i);
        }
        return v / ds.length;
    }

    /**
     * Computes the mean
     *
     * @param ds
     */
    public static double average(double[] ds) {
        double t = 0;
        for (double i : ds) {
            t += i;
        }
        return t / ds.length;
    }

    /**
     * Computes the standard deviation
     *
     * @param data
     */
    public static double stdDeviation(double[] data) {
        return sqrt(variance(data));
    }

    /**
     * Erases values from a signal data array
     *
     * @param remove list of values to removes
     * @param from signal data to be cleaned
     * @return cleaned data array
     */
    public static double[][] removePoints(List<?> remove, double[][] from) {
        double[] xArr = new double[from[Y].length - remove.size()];
        double[] yArr = new double[from[Y].length - remove.size()];

        for (int i = 0, ii = 0; i < from[Y].length; i++) {
            if (!remove.contains(i)) {
                xArr[ii] = i;
                yArr[ii] = from[Y][i];
                ii++;
            }
        }

        return new double[][]{xArr, yArr};
    }

    /**
     * Advance the position to the next point where the curve start to decline
     *
     * @param pos current position in the data array
     * @param data
     * @return the new position
     */
    public static int nextDrop(int pos, double[] data) {
        while ((pos + 1 < data.length - 1) && (data[pos] < data[pos + 1] || data[pos] == data[pos + 1])) {
            pos++;
        }
        return pos;
    }

    /**
     * Advance the position to the next point where the curve start to go up
     *
     * @param pos current position in the data array
     * @param data
     * @return the new position
     */
    public static int nextAscent(int pos, double[] data) {
        while ((pos + 1 < data.length - 1) && (data[pos] > data[pos + 1] || data[pos] == data[pos + 1])) {
            pos++;
        }
        return pos;
    }

    /**
     * Advance the position to the next point where the curve isn't flat
     * (growing or declining)
     *
     * @param pos
     * @param data
     * @return the new position
     */
    public static int nextNonFlat(int pos, double[] data) {
        while ((pos + 1 < data.length - 1) && (data[pos] == data[pos + 1])) {
            pos++;
        }
        return pos;
    }

    /**
     * Adds a range of integers to an integer List
     *
     * @param list
     * @param from range start
     * @param to range end
     * @return the filled list
     */
    public static List<Integer> addRangeTo(List<Integer> list, int from, int to) {
        for (int i = from; i <= to; i++) {
            list.add(i);
        }
        return list;
    }

    /**
     * Returns true if the curve is ascending at the given position
     *
     * @param pos
     * @param data
     */
    public static boolean isAscending(int pos, double[] data) {
        return data[pos] < data[pos + 1];
    }

    /**
     * Returns true if the curve is dropping at the given position
     *
     * @param pos
     * @param data
     */
    public static boolean isDropping(int pos, double[] data) {
        return data[pos] > data[pos + 1];
    }

    /**
     * Finds the minimal y coordinate in a data
     *
     * @param data
     * @return the x/y of the minimal y value
     */
    public static Coord minCoords(double[][] data) {
        double[] x = data[X];
        double[] y = data[Y];
        double xmin = x[0];
        double ymin = y[0];
        for (int i = 1; i < y.length; i++) {
            if (y[i] < ymin) {
                xmin = x[i];
                ymin = y[i];
            }
        }
        return new Coord(xmin, ymin);
    }

    /**
     * Finds the maximal y coordinate in a data
     *
     * @param data
     * @return the x/y of the maximal y value
     */
    public static Coord maxCoords(double[][] data) {
        double[] x = data[X];
        double[] y = data[Y];
        double xmax = x[0];
        double ymax = y[0];
        for (int i = 1; i < y.length; i++) {
            if (y[i] > ymax) {
                xmax = x[i];
                ymax = y[i];
            }
        }
        return new Coord(xmax, ymax);
    }

    /**
     * Finds the minimal y value in a data
     *
     * @param data
     * @return the minimal y value
     */
    public static double min(double[][] data) {
        double[] y = data[Y];
        double ymin = y[0];
        for (int i = 1; i < y.length; i++) {
            if (y[i] < ymin) {
                ymin = y[i];
            }
        }
        return ymin;
    }

    /**
     * Finds the maximale y value in a data
     *
     * @param data
     * @return the maximale y value
     */
    public static double max(double[][] data) {
        double[] y = data[Y];
        double ymax = y[0];
        for (int i = 1; i < y.length; i++) {
            if (y[i] > ymax) {
                ymax = y[i];
            }
        }
        return ymax;
    }

    public static Double ratioMaxMin(double[][] data) {
        double min = min(data);
        double max = max(data);
        if (min >= 0 || max <= 0) {
            System.out.println("The sign of the graph never changes");
            return null;
        }
        return Math.abs(max) / Math.abs(min);
    }

    /**
     * Drops n/2 values (keeping one value every two)
     *
     * @param from
     * @return half-sized array
     */
    public static double[] oneOfTwo(double[] from) {
        double[] half = new double[from.length / 2];
        for (int i = 0; i < from.length / 2; i++) {
            half[i] = from[i * 2];
        }
        return half;
    }
}
