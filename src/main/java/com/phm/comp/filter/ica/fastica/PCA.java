/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phm.comp.filter.ica.fastica;

/**
 * This class computes the Principal Component Analysis of a given set of
 * vectors.
 *
 * @author Michael Lambertz
 */
public class PCA {

    private double[] meanValues;
    private double[][] vectorsZeroMean;
    private double[][] covarianceMatrix;
    private double[] eigenValues;
    private double[][] eigenVectors;
    private double[][] resVectors;

    /**
     * This constructor computes the Principal Component Analysis of the given
     * set of vectors.
     *
     * @param inVectors the given set of vectors
     */
    public PCA(double[][] inVectors) {
        // calculate the mean along each row
        meanValues = calcMeanValues(inVectors);
        // subtract the mean vector from each data vector
        vectorsZeroMean = Vector.addVecToSet(inVectors, Vector.scale(-1.0,
                meanValues));
        // calculate the covariance matrix
        covarianceMatrix = Matrix.scale(Matrix.square(vectorsZeroMean),
                1.0 / Matrix.getNumOfColumns(inVectors));
        // calculate the eigenvalue decomposition
        EigenValueDecompositionSymm eigenDeco = new EigenValueDecompositionSymm(
                covarianceMatrix);
        eigenVectors = eigenDeco.getEigenVectors();
        eigenValues = eigenDeco.getEigenValues();
        // calculate the resulting vectors
        resVectors = Matrix.mult(Matrix.transpose(eigenVectors),
                vectorsZeroMean);
    }

    /**
     * Returns the eigenvalues of the computed covariance matrix.
     *
     * @return the eigenvalues of the covariance matrix
     */
    public double[] getEigenValues() {
        return (eigenValues);
    }

    /**
     * Returns the eigenvectors of the computed covariance matrix.
     *
     * @return the eigenvectors of the covariance matrix
     */
    public double[][] getEigenVectors() {
        return (eigenVectors);
    }

    /**
     * Returns the computed mean vector for the set of given vectors.
     *
     * @return the mean vector
     */
    public double[] getMeanValues() {
        return (meanValues);
    }

    /**
     * Returns the given vectors without mean.
     *
     * @return the zero-mean vectors
     */
    public double[][] getVectorsZeroMean() {
        return (vectorsZeroMean);
    }

    /**
     * Returns the set of vectors, which results after the Principal Component
     * Analysis.
     *
     * @return the resulting set of vectors
     */
    public double[][] getResultingVectors() {
        return (resVectors);
    }

    /**
     * Calculates the mean vector from a set of vectors.
     *
     * @param inVectors the set of vectors
     * @return the resulting mean vector
     */
    private static double[] calcMeanValues(double[][] inVectors) {
        int m = Matrix.getNumOfRows(inVectors);
        int n = Matrix.getNumOfColumns(inVectors);
        double[] meanValues = Vector.newVector(m);
        for (int i = 0; i < m; ++i) {
            meanValues[i] = 0.0;
            for (int j = 0; j < n; ++j) {
                meanValues[i] += inVectors[i][j];
            }
            meanValues[i] /= n;
        }
        return (meanValues);
    }

}
