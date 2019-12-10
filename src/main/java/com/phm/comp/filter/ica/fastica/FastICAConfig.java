/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phm.comp.filter.ica.fastica;

/**
 * The <code>FastICAConfig</code> class is used to configure the FastICA
 * algorithm.
 *
 * @author Michael Lambertz
 */
public class FastICAConfig {

    public enum Approach {

        SYMMETRIC,
        DEFLATION
    }

    private int numICs;
    private Approach approach;
    private double stepSize;
    private double epsilon;
    private int maxIterations;
    private double[][] initialMixingMatrix;

    /**
     * Creates a FastICA configuration.
     *
     * @param numICs the number of independent components
     * @param approach deflation or symmetric approach algorithm
     * @param stepSize the step size of an approach
     * @param epsilon accuracy exit condition
     * @param maxIterations the maximum number of iterations
     * @param initialMixingMatrix the initial mixing matrix, can be
     * <code>null</code>
     */
    public FastICAConfig(
            int numICs,
            Approach approach,
            double stepSize,
            double epsilon,
            int maxIterations,
            double[][] initialMixingMatrix) {
        this.numICs = numICs;
        this.approach = approach;
        this.stepSize = stepSize;
        this.epsilon = epsilon;
        this.maxIterations = maxIterations;
        this.initialMixingMatrix = initialMixingMatrix;
    }

    /**
     * Creates a FastICA standard configuration:<br>
     * - deflation approach<br>
     * - step size is one<br>
     * - epsilon is 1.0e-12<br>
     * - a maximum of 1000 iterations<br>
     * - no initial mixing matrix<br>
     *
     * @param numICs the number of independent components
     */
    public FastICAConfig(
            int numICs) {
        this(numICs, Approach.DEFLATION, 1.0, 1.0e-12, 1000, null);
    }

    /**
     * Creates a FastICA standard configuration:<br>
     * - one independent component<br>
     * - deflation approach<br>
     * - step size is one<br>
     * - epsilon is 1.0e-12<br>
     * - a maximum of 1000 iterations<br>
     * - no initial mixing matrix<br>
     */
    public FastICAConfig() {
        this(1);
    }

// Getter and Setter Methods
    public int getNumICs() {
        return (numICs);
    }

    public Approach getApproach() {
        return (approach);
    }

    public double getEpsilon() {
        return (epsilon);
    }

    public double[][] getInitialMixingMatrix() {
        return (initialMixingMatrix);
    }

    public int getMaxIterations() {
        return (maxIterations);
    }

    public double getStepSize() {
        return (stepSize);
    }

    public void setNumICs(int numICs) {
        this.numICs = numICs;
    }

    public void setApproach(Approach approach) {
        this.approach = approach;
    }

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    public void setInitialMixingMatrix(double[][] initialMixingMatrix) {
        this.initialMixingMatrix = initialMixingMatrix;
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public void setStepSize(double stepSize) {
        this.stepSize = stepSize;
    }

}
