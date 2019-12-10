/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phm.comp.filter.ica.fastica;

/**
 * This function is useful, when the independent components are highly
 * super-Gaussian, or when robustness is very important.
 *
 * @author Michael Lambertz
 */
public class GaussCFunction implements ContrastFunction {

    private double a;

    public GaussCFunction(double a) {
        this.a = a;
    }

    public double function(double x) {
        return (x * Math.exp(-a * x * x / 2));
    }

    public double derivative(double x) {
        return ((1 - a * x * x) * Math.exp(-a * x * x / 2));
    }

}
