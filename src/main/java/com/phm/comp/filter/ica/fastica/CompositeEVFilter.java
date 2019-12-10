/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phm.comp.filter.ica.fastica;

import java.util.ArrayList;

/**
 * The <code>CompositeEVFilter</code> can be used to build a chain of eigenvalue
 * filters.
 *
 * @author Michael Lambertz
 */
public class CompositeEVFilter extends ArrayList<EigenValueFilter> implements EigenValueFilter {

    private static final long serialVersionUID = 3256439222608213049L;

    private double[] eigenValues;
    private double[][] eigenVectors;

    public CompositeEVFilter() {
        super(0);
    }

    public CompositeEVFilter(
            EigenValueFilter evf1,
            EigenValueFilter evf2) {
        super(2);
        set(0, evf1);
        set(1, evf2);
    }

    public void passEigenValues(
            double[] eigenValues,
            double[][] eigenVectors) {
        if (size() == 0) {
            this.eigenValues = eigenValues;
            this.eigenVectors = eigenVectors;
        } else {
            get(0).passEigenValues(eigenValues, eigenVectors);
            for (int i = 0; i < size() - 1; ++i) {
                get(i + 1).passEigenValues(get(i).getEigenValues(), get(i).getEigenVectors());
            }
            this.eigenValues = get(size() - 1).getEigenValues();
            this.eigenVectors = get(size() - 1).getEigenVectors();
        }
    }

    public double[] getEigenValues() {
        return (eigenValues);
    }

    public double[][] getEigenVectors() {
        return (eigenVectors);
    }

}
