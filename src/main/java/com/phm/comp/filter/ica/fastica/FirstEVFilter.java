/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phm.comp.filter.ica.fastica;

/**
 * The <code>FirstEVFilter</code> filters all eigenvalues after a given number
 * of eigenvalues.
 *
 * @author Michael Lambertz
 */
public class FirstEVFilter implements EigenValueFilter {

    private int firstN;
    private double[] eigenValues;
    private double[][] eigenVectors;

    public FirstEVFilter(int n) {
        this.firstN = n;
    }

    public void passEigenValues(
            double[] eigenValues,
            double[][] eigenVectors) {
        if (firstN >= eigenValues.length) {
            this.eigenValues = eigenValues;
            this.eigenVectors = eigenVectors;
        } else {
            int m = Matrix.getNumOfRows(eigenVectors);
            int n = firstN;
            this.eigenValues = Vector.newVector(n);
            this.eigenVectors = Matrix.newMatrix(m, n);
            for (int j = 0; j < n; ++j) {
                this.eigenValues[j] = eigenValues[j];
                for (int i = 0; i < m; ++i) {
                    this.eigenVectors[i][j] = eigenVectors[i][j];
                }
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
