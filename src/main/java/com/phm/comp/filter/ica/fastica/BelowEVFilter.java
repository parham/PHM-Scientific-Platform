/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phm.comp.filter.ica.fastica;

import java.util.ArrayList;

/**
 * The <code>BelowEVFilter</code> filters all eigenvalues, which are lower than
 * a given value.
 *
 * @author Michael Lambertz
 */
public class BelowEVFilter implements EigenValueFilter {

    private double limit;
    private boolean relative;
    private double[] eigenValues;
    private double[][] eigenVectors;

    public BelowEVFilter(
            double limit,
            boolean relative) {
        this.limit = limit;
        this.relative = relative;
    }

    @Override
    public void passEigenValues(
            double[] eigenValues,
            double[][] eigenVectors) {
        double limit;
        if (relative) {
            double max = -1.0;
            for (int i = 0; i < eigenValues.length; ++i) {
                if (max < Math.abs(eigenValues[i])) {
                    max = Math.abs(eigenValues[i]);
                }
            }
            limit = max * this.limit;
        } else {
            limit = this.limit;
        }
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < eigenValues.length; ++i) {
            if (Math.abs(eigenValues[i]) >= limit) {
                list.add(new Integer(i));
            }
        }
        int m = Matrix.getNumOfRows(eigenVectors);
        int n = list.size();
        this.eigenValues = Vector.newVector(n);
        this.eigenVectors = Matrix.newMatrix(m, n);
        for (int j = 0; j < n; ++j) {
            int k = list.get(j).intValue();
            this.eigenValues[j] = eigenValues[k];
            for (int i = 0; i < m; ++i) {
                this.eigenVectors[i][j] = eigenVectors[i][k];
            }
        }
    }

    public double[] getEigenValues() {
        return (eigenValues);
    }

    public double[][] getEigenVectors() {
        return (eigenVectors);
    }

}
