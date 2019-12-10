/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phm.comp.filter.ica.fastica;

/**
 * This <code>EigenValueDecompositionSymm</code> class computes the eigenvalue
 * decomposition of a real symmetric matrix.
 *
 * @author Michael Lambertz
 */
public class EigenValueDecompositionSymm {

    private double[] eigenValues;
    private double[][] eigenVectors;

    /**
     * This constructor computes the eigenvalue decomposition of the real
     * symmetric matrix object.
     *
     * @param matrix the real symmetric matrix to decomposite
     */
    public EigenValueDecompositionSymm(
            double[][] matrix) {
        // initialize some variables  
        int m = Matrix.getNumOfRows(matrix);
        int n = Matrix.getNumOfColumns(matrix);
        eigenVectors = Matrix.clone(matrix);
        eigenValues = Vector.newVector(m);
        double[] accu = Vector.newVector(m);

        // tridiagonalise the matrix
        for (int i = 0; i < m; i++) {
            eigenValues[i] = eigenVectors[m - 1][i];
        }
        for (int i = m - 1; i > 0; i--) {
            double sc = 0.0;
            double h1 = 0.0;
            for (int j = 0; j < i; j++) {
                sc = sc + Math.abs(eigenValues[j]);
            }
            if (sc == 0.0) {
                accu[i] = eigenValues[i - 1];
                for (int j = 0; j < i; j++) {
                    eigenValues[j] = eigenVectors[i - 1][j];
                    eigenVectors[i][j] = 0.0;
                    eigenVectors[j][i] = 0.0;
                }
            } else {
                for (int j = 0; j < i; j++) {
                    double evj = eigenValues[j] / sc;
                    eigenValues[j] = evj;
                    h1 += evj * evj;
                }
                double f = eigenValues[i - 1];
                double g = Math.sqrt(h1);
                if (f > 0) {
                    g = -g;
                }
                accu[i] = sc * g;
                h1 = h1 - f * g;
                eigenValues[i - 1] = f - g;
                for (int j = 0; j < i; j++) {
                    accu[j] = 0.0;
                }
                for (int j = 0; j < i; j++) {
                    f = eigenValues[j];
                    eigenVectors[j][i] = f;
                    g = accu[j] + eigenVectors[j][j] * f;
                    for (int k = j + 1; k <= i - 1; k++) {
                        g += eigenVectors[k][j] * eigenValues[k];
                        accu[k] += eigenVectors[k][j] * f;
                    }
                    accu[j] = g;
                }
                f = 0.0;
                for (int j = 0; j < i; j++) {
                    accu[j] /= h1;
                    f += accu[j] * eigenValues[j];
                }
                double h2 = f / (h1 + h1);
                for (int j = 0; j < i; j++) {
                    accu[j] -= h2 * eigenValues[j];
                }
                for (int j = 0; j < i; j++) {
                    f = eigenValues[j];
                    g = accu[j];
                    for (int k = j; k <= i - 1; k++) {
                        eigenVectors[k][j] -= (f * accu[k] + g * eigenValues[k]);
                    }
                    eigenValues[j] = eigenVectors[i - 1][j];
                    eigenVectors[i][j] = 0.0;
                }
            }
            eigenValues[i] = h1;
        }

        // memorise transformations
        for (int i = 0; i < m - 1; i++) {
            eigenVectors[m - 1][i] = eigenVectors[i][i];
            eigenVectors[i][i] = 1.0;
            double h = eigenValues[i + 1];
            if (h != 0.0) {
                for (int j = 0; j <= i; j++) {
                    eigenValues[j] = eigenVectors[j][i + 1] / h;
                }
                for (int j = 0; j <= i; j++) {
                    double g = 0.0;
                    for (int k = 0; k <= i; k++) {
                        g += eigenVectors[k][i + 1] * eigenVectors[k][j];
                    }
                    for (int k = 0; k <= i; k++) {
                        eigenVectors[k][j] -= g * eigenValues[k];
                    }
                }
            }
            for (int j = 0; j <= i; j++) {
                eigenVectors[j][i + 1] = 0.0;
            }
        }
        for (int i = 0; i < m; i++) {
            eigenValues[i] = eigenVectors[m - 1][i];
            eigenVectors[m - 1][i] = 0.0;
        }
        eigenVectors[m - 1][m - 1] = 1.0;
        accu[0] = 0.0;

        // diagonalize
        double a = 0.0;
        double piv = 0.0;
        double eps = 1.0E-16;
        for (int i = 1; i < n; i++) {
            accu[i - 1] = accu[i];
        }
        accu[n - 1] = 0.0;
        for (int i = 0; i < n; i++) {
            piv = Math.max(piv, Math.abs(eigenValues[i]) + Math.abs(accu[i]));
            int l = i;
            while (l < n) {
                if (Math.abs(accu[l]) <= eps * piv) {
                    break;
                }
                l++;
            }
            if (l > i) {
                int iter = 0;
                do {
                    iter = iter + 1;
                    double f = eigenValues[i];
                    double g = (eigenValues[i + 1] - f) / (2.0 * accu[i]);
                    double r = dist(g, 1.0);
                    if (g < 0) {
                        r = -r;
                    }
                    eigenValues[i] = accu[i] / (g + r);
                    eigenValues[i + 1] = accu[i] * (g + r);
                    double ev1 = eigenValues[i + 1];
                    double h = f - eigenValues[i];
                    for (int j = i + 2; j < n; j++) {
                        eigenValues[j] -= h;
                    }
                    a = a + h;
                    g = eigenValues[l];
                    double ac1 = accu[i + 1];
                    double b1 = 1.0;
                    double b2 = 1.0;
                    double b3 = 1.0;
                    double s1 = 0.0;
                    double s2 = 0.0;
                    for (int j = l - 1; j >= i; j--) {
                        b3 = b2;
                        b2 = b1;
                        s2 = s1;
                        f = b1 * accu[j];
                        h = b1 * g;
                        r = dist(g, accu[j]);
                        accu[j + 1] = s1 * r;
                        s1 = accu[j] / r;
                        b1 = g / r;
                        g = b1 * eigenValues[j] - s1 * f;
                        eigenValues[j + 1] = h + s1 * (b1 * f + s1 * eigenValues[j]);
                        for (int k = 0; k < n; k++) {
                            h = eigenVectors[k][j + 1];
                            eigenVectors[k][j + 1] = s1 * eigenVectors[k][j] + b1 * h;
                            eigenVectors[k][j] = b1 * eigenVectors[k][j] - s1 * h;
                        }
                    }
                    g = -s1 * s2 * b3 * ac1 * accu[i] / ev1;
                    accu[i] = s1 * g;
                    eigenValues[i] = b1 * g;
                } while (Math.abs(accu[i]) > eps * piv);
            }
            eigenValues[i] = eigenValues[i] + a;
            accu[i] = 0.0;
        }
    }

    /**
     * Calculates sqrt(a^2 + b^2).
     *
     * @param a
     * @param b
     * @return sqrt(a^2 + b^2)
     */
    private static double dist(
            double a,
            double b) {
        double r;
        if (Math.abs(a) > Math.abs(b)) {
            r = b / a;
            return (Math.abs(a) * Math.sqrt(1 + r * r));
        } else if (b != 0) {
            r = a / b;
            return (Math.abs(b) * Math.sqrt(1 + r * r));
        } else {
            return (0.0);
        }
    }

    /**
     * Returns the vector of eigenvalues computed.
     *
     * @return the eigenvalue's vector
     */
    public double[] getEigenValues() {
        return (eigenValues);
    }

    /**
     * Returns the matrix of eigenvectors computed.
     *
     * @return the eigenvector's matrix
     */
    public double[][] getEigenVectors() {
        return (eigenVectors);
    }
}
