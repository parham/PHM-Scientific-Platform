
package com.phm.comp.clusterer;

import com.phm.annotations.ImplementationDetails;
import com.phm.annotations.PublicationDetails;
import com.phm.annotations.PublicationType;
import com.phm.core.data.DatasetUtils;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;

@PublicationDetails (
        author = {"J. B. MacQueen"},
        type = PublicationType.Proceedings,
        title =  "Some Methods for classification and Analysis of Multivariate Observations",
        journal = "Proceedings of 5-th Berkeley Symposium on Mathematical Statistics and Probability",
        year = "1967",
        pages = "281-297",
        publisher = "Berkeley, University of California Press",
        customData = {"The main purpose of this paper is to describe a process for partitioning an A^-dimensional \n" +
                      "population into k sets on the basis of a sample."}
)
@ImplementationDetails (
        className = "KMeansClusterer",
        developers = {"Thomas Abeel"},
        emails = {"thomas@abeel.be"},
        url = "http://www.abeel.be",
        date = "2015/11/09",
        lastModification = "2015/11/09",
        version = "1.0.0",
        description = "Implements the K-means algorithms as described by Mac Queen in 1967. modified by Parham Nooralishahi"
)
public class KMeansClusterer implements OfflineClusterer<RealVector> {
    
    // The number of clusters.
    private int numberOfClusters = -1;

    /**
     * The number of iterations the algorithm should make. If this value is
     * Integer.INFINITY, then the algorithm runs until the centroids no longer
     * change.
     */
    private int numberOfIterations = -1;

    /**
     * Random generator for this clusterer.
     */
    private Random rg;

    /**
     * The distance measure used in the algorithm, defaults to Euclidean
     * distance.
     */
    private DistanceMeasure dm;


    /**
     * The centroids of the different clusters.
     */
    private RealVector [] centroids;

    /**
     * Constuct a default Simple K-means clusterer with 100 iterations, 2
     * clusters, a default random generator and using the Euclidean distance.
     */
    public KMeansClusterer() {
    	this (4, 100);
    }
    
    /**
     * Create a new Simple K-means clusterer with the given number of clusters
     * and iterations. The internal random generator is a new one based upon the
     * current system time. For the distance we use the Euclidean n-space
     * distance.
     * 
     * @param clusters
     *            the number of clusters
     * @param iterations
     *            the number of iterations
     */
    public KMeansClusterer (int clusters, int iterations) {
        this(clusters, iterations, new EuclideanDistance());
    }

    /**
     * Create a new K-means clusterer with the given number of clusters and
     * iterations. Also the Random Generator for the clusterer is given as
     * parameter.
     * 
     * @param clusters
     *            the number of clustesr
     * @param iterations
     *            the number of iterations 
     * @param dm
     *            the distance measure to use
     */
    public KMeansClusterer (int clusters, int iterations, DistanceMeasure dm) {
        this.numberOfClusters = clusters;
        this.numberOfIterations = iterations;
        this.dm = dm;
        rg = new Random(System.currentTimeMillis());
    }

    @Override
    public Map<String, List<RealVector>> cluster (Collection<? extends RealVector> data) {
        if (data.isEmpty())
            throw new RuntimeException("The dataset should not be empty");
        if (numberOfClusters == 0)
            throw new RuntimeException("There should be at least one cluster");
        LinkedList<RealVector> ds = new LinkedList<>(data);
        // Place K points into the space represented by the objects that are
        // being clustered. These points represent the initial group of
        // centroids.
        RealVector min = DatasetUtils.getMinimumInstance(data);
        RealVector max = DatasetUtils.getMaximumInstance(data);
        this.centroids = new RealVector [numberOfClusters];
        int instanceLength = ds.getFirst().getDimension();
        for (int j = 0; j < numberOfClusters; j++) {
            double[] randomInstance = new double[instanceLength];
            for (int i = 0; i < instanceLength; i++) {
                double dist = Math.abs(max.getEntry(i) - min.getEntry(i));
                randomInstance[i] = (float) (min.getEntry(i) + rg.nextDouble() * dist);

            }
            this.centroids[j] = new ArrayRealVector (randomInstance);
        }

        int iterationCount = 0;
        boolean centroidsChanged = true;
        boolean randomCentroids=true;
        while (randomCentroids ||(iterationCount < this.numberOfIterations && centroidsChanged)) {
            iterationCount++;
            // Assign each object to the group that has the closest centroid.
            int[] assignment = new int[data.size()];
            for (int i = 0; i < data.size(); i++) {
                int tmpCluster = 0;
                double minDistance = dm.compute (centroids[0].toArray(), ds.get(i).toArray());
                for (int j = 1; j < centroids.length; j++) {
                    double dist = dm.compute(centroids[j].toArray(), ds.get(i).toArray());
                    if (dist < minDistance) {
                        minDistance = dist;
                        tmpCluster = j;
                    }
                }
                assignment[i] = tmpCluster;
            }
            // When all objects have been assigned, recalculate the positions of
            // the K centroids and start over.
            // The new position of the centroid is the weighted center of the
            // current cluster.
            double[][] sumPosition = new double [this.numberOfClusters][instanceLength];
            int[] countPosition = new int[this.numberOfClusters];
            for (int i = 0; i < data.size(); i++) {
                RealVector in = ds.get(i);
                for (int j = 0; j < instanceLength; j++) {
                    sumPosition[assignment[i]][j] += in.getEntry(j);
                }
                countPosition[assignment[i]]++;
            }
            centroidsChanged = false;
            randomCentroids=false;
            for (int i = 0; i < this.numberOfClusters; i++) {
                if (countPosition[i] > 0) {
                    double[] tmp = new double[instanceLength];
                    for (int j = 0; j < instanceLength; j++) {
                        tmp[j] = (float) sumPosition[i][j] / countPosition[i];
                    }
                    RealVector newCentroid = new ArrayRealVector (tmp);
                    if (dm.compute (newCentroid.toArray(), centroids[i].toArray()) > 0.0001) {
                        centroidsChanged = true;
                        centroids[i] = newCentroid;
                    }
                } else {
                    double[] randomInstance = new double[instanceLength];
                    for (int j = 0; j < instanceLength; j++) {
                        double dist = Math.abs(max.getEntry(j) - min.getEntry(j));
                        randomInstance[j] = (float) (min.getEntry(j) + rg.nextDouble() * dist);

                    }
                    randomCentroids = true;
                    this.centroids[i] = new ArrayRealVector (randomInstance);
                }
            }
        }
        
        Map<String, List<RealVector>> output = new HashMap<>(centroids.length);
        for (int i = 0; i < centroids.length; i++) {
            output.put(String.valueOf(i), new LinkedList<>());
        }
        for (RealVector d : ds) {
            int tmpCluster = 0;
            double minDistance = dm.compute(centroids[0].toArray(), d.toArray());
            for (int j = 0; j < centroids.length; j++) {
                double dist = dm.compute(centroids[j].toArray(), d.toArray());
                if (dist < minDistance) {
                    minDistance = dist;
                    tmpCluster = j;
                }
            }
            output.get(String.valueOf(tmpCluster)).add(d);
        }
        return output;
    }
    
    @Override
    public String cluster (RealVector d) {
        int tmpCluster = 0;
        double minDistance = dm.compute(centroids[0].toArray(), d.toArray());
        for (int j = 0; j < centroids.length; j++) {
            double dist = dm.compute(centroids[j].toArray(), d.toArray());
            if (dist < minDistance) {
                minDistance = dist;
                tmpCluster = j;
            }
        }
        return String.valueOf(tmpCluster);
    }

    @Override
    public Map<String, List<RealVector>> getClusters() {
        return new HashMap<>();
    }
    
}
