/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phm.comp.filter.ica.fastica;

/**
 * The eigenvalue filter can be used to exclude some unfit data after it has
 * been analysed using the Principal Component Analysis. It is mainly used to
 * exlude data, whose variance is too small. An instance of this interface has
 * to be passed to the FastICA algorithm.
 *
 * @author Michael Lambertz
 */
public interface EigenValueFilter {

    /**
     * This function is called after the data has been analysed using the
     * Principal Component Analysis.
     *
     * @param eigenValues the computed eigenvalues
     * @param eigenVectors the computed eigenvectors
     */
    public void passEigenValues(double[] eigenValues, double[][] eigenVectors);

    /**
     * Returns the remaining eigenvalues, which were not excluded.
     *
     * @return the remaining eigenvalues
     */
    public double[] getEigenValues();

    /**
     * Returns the remaining eigenvectors, which were not excluded. The
     * eigenvectors must be stored in the columns of the matrix. Furthermore the
     * first eigenvector must belong to the first eigenvalue and so on.
     *
     * @return the remaining eigenvectors
     */
    public double[][] getEigenVectors();

}
