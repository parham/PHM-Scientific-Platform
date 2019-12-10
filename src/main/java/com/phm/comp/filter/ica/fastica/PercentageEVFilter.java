/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phm.comp.filter.ica.fastica;

/**
 * The <code>PercentageEVFilter</code> sorts the eigenvalues
 * and returns the first highest eigenvalues, whose sum is
 * higher than the given percentage of the sum of all 
 * eigenvalues.
 * @author Michael Lambertz
 */

public class PercentageEVFilter implements EigenValueFilter
{

private double                          percentage;
private double[]                        eigenValues;
private double[][]              eigenVectors;

public PercentageEVFilter( double percentage )
{
        this.percentage = percentage;
}

public void passEigenValues( double[] eigenValues, double[][] eigenVectors )
{
        int l = eigenValues.length;
        double power = 0.0;
        for ( int i = 0; i < l; ++ i )
                power += Math.abs(eigenValues[i]);
        power /= 100.0;
        SortingEVFilter sevf = new SortingEVFilter(true, true);
        sevf.passEigenValues(eigenValues, eigenVectors);
        double[] eigenValuesSorted = sevf.getEigenValues();
        double[][] eigenVectorsSorted = sevf.getEigenVectors();
        double per = 0.0;
        int n = 0;
        while ( per - this.percentage < 0.0 )
        {
                per += Math.abs(eigenValuesSorted[n]) / power;
                n ++;
        }
        FirstEVFilter fevf = new FirstEVFilter(n);
        fevf.passEigenValues(eigenValuesSorted, eigenVectorsSorted);
        this.eigenValues = fevf.getEigenValues();
        this.eigenVectors = fevf.getEigenVectors();
}

public double[] getEigenValues( )
{
        return(eigenValues);
}

public double[][] getEigenVectors( )
{
        return(eigenVectors);
}

}
