/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phm.comp.filter.ica.fastica;

/**
 * The contrast functions used to estimate negentropy. One of this functions has
 * to be passed to the FastICA algorithm.
 *
 * @author Michael Lambertz
 */
public interface ContrastFunction {

    /**
     * Computes the function value at position <code>x</code>.
     *
     * @param x the desired position
     * @return the function value
     */
    public double function(double x);

    /**
     * Computes the value of the function's derivative at position
     * <code>x</code>.
     *
     * @param x the desired position
     * @return the function value
     */
    public double derivative(double x);

}
