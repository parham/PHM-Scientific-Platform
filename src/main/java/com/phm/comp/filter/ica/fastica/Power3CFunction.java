/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phm.comp.filter.ica.fastica;

/**
 * The x^3 function is useful for estimating sub-Gaussian independent components
 * when there are no outliers.
 *
 * @author Michael Lambertz
 */
public class Power3CFunction implements ContrastFunction {

    public double function(double x) {
        return (x * x * x);
    }

    public double derivative(double x) {
        return (3 * x * x);
    }

}
