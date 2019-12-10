
package com.phm.comp.distance.dtw;

import com.phm.annotations.ImplementationDetails;
import com.phm.annotations.PublicationDetails;
import com.phm.annotations.PublicationType;
import com.phm.comp.distance.BlankDimensionalDistanceMeasure;
import com.phm.comp.distance.DimensionalDistanceMeasure;
import com.phm.comp.distance.DistanceInfo;
import com.phm.comp.distance.DistanceMeasure;
import com.phm.core.ParametersContainer;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects; 
import org.apache.commons.math3.linear.RealVector;
 
@PublicationDetails (
        author = {"J. Kruskall", "M. Liberman"},
        type = PublicationType.Journal,
        title =  "The Symmetric Time Warping Problem: From Continuous to Discrete",
        journal = "Time Warps, String Edits and Macromolecules: The Theory and Practice of Sequence Comparison",
        year = "1983",
        pages = "125-161", 
        
        paperAbstract = "The main purpose of this paper is to describe a process for partitioning an A^-dimensional \n" +
                        "population into k sets on the basis of a sample."
)
@ImplementationDetails (
        className = "DTWDistanceMeasure",
        date = "2015/11/09",
        lastModification = "2015/11/09",
        version = "1.0.0"
)
public class DTWDistanceMeasure implements DistanceMeasure {
    
    protected SearchWindow searchWindow;
    protected DimensionalDistanceMeasure policy;
    protected boolean movedleft = true;
    protected boolean movedup = true;
    protected boolean moveleftup = true;
    
    
    public DTWDistanceMeasure (SearchWindow sw) {
        this (sw, new BlankDimensionalDistanceMeasure());
    }
    public DTWDistanceMeasure (SearchWindow sw, DimensionalDistanceMeasure p) {
        searchWindow = Objects.requireNonNull (sw);
        policy = Objects.requireNonNull(p);
    }
    public DTWDistanceMeasure (DimensionalDistanceMeasure p) {
        this (new FullWindowSW(), p);
    }
    public DTWDistanceMeasure () {
        searchWindow = new FullWindowSW();
    }
    
    public void moveDirectToLeft (boolean s) {
        movedleft = s;
    }
    public boolean doesMoveDirectToLeft () {
        return movedleft;
    }
    public void moveDirectToUp (boolean s) {
        movedup = s;
    }
    public boolean doesMoveDirectToUp () {
        return movedup;
    }
    public void moveLeftnUp (boolean s) {
        moveleftup = s;
    }
    public boolean doesMoveLeftnUp () {
        return moveleftup;
    }
    
    protected void afterCalculateCostMatrix (RealVector sc1, 
                                             RealVector sc2, 
                                             CostMatrix cm, ParametersContainer pc) {
        // Empty body
    }
    protected void beforeCalculateCostMatrix (RealVector sc1, 
                                              RealVector sc2, 
                                              ParametersContainer pc) {
        // Empty body
    }
    public CostMatrix onCalculateCostMatrix (RealVector sc1, 
                                             RealVector sc2, 
                                             ParametersContainer pc) {          
        final double [] s1 = sc1.toArray();
        final double [] s2 = sc2.toArray();
        
        final int maxRow = s1.length;
        final int maxCol = s2.length;
        // Initialize Cost Matrix
        CostMatrix costMatrix = new CostMatrix(maxRow, maxCol);
        
        double [][] costTemp = new double [maxRow][maxCol];
        for (int row = 0; row < maxRow; row++) {
            for (int col = 0; col < maxCol; col++) {
                double dc = s2 [col] - s1 [row];
                costTemp [row][col] = dc * dc;
            }
        }
        costMatrix.set (0, 0, costTemp [0][0]);
        
        for (int row = 1; row < maxRow; row++) {
            costMatrix.set(row, 0, costTemp [row][0] + 
                                   costMatrix.get(row - 1, 0));
        }
        for (int col = 1; col < maxCol; col++) {
            costMatrix.set (0, col, costTemp [0][col] + 
                                    costMatrix.get(0, col - 1));
        }
        for (int row = 1; row < maxRow; row++) {
            for (int col = 1; col < maxCol; col++) {
                costMatrix.set(row, col, costTemp [row][col] +
                        Math.min(costMatrix.get(row - 1, col), 
                        Math.min(costMatrix.get(row - 1, col - 1),
                                 costMatrix.get(row, col - 1))));
            }
        }
        return costMatrix;
    }
    
    protected List<DTWDistanceInfo.WarpingIndex> findNeighbors (SearchWindow win, int row, int col) {
        LinkedList<DTWDistanceInfo.WarpingIndex> list = new LinkedList<>();
        int r, c;
        if (doesMoveDirectToUp()) {
            r = row - 1;
            c = col;
            if (win.isValid(r,c)) list.add(new DTWDistanceInfo.WarpingIndex(r, c, win.get(r,c)));
        }
        if (doesMoveDirectToLeft()) {
            r = row;
            c = col - 1;
            if (win.isValid(r,c)) list.add(new DTWDistanceInfo.WarpingIndex(r, c, win.get(r,c)));
        }
        if (doesMoveLeftnUp()) {
            r = row - 1;
            c = col - 1;
            if (win.isValid(r,c)) list.add(new DTWDistanceInfo.WarpingIndex(r, c, win.get(r,c)));            
        }
       
        return list;
    }
    public void beforeCalculateDTWDistance (RealVector sc1, 
                                            RealVector sc2, 
                                            CostMatrix costMatrix,
                                            ParametersContainer pc) {
        // Empty body
    }
    public DTWDistanceInfo onCalculateDTWDistance (RealVector sc1, 
                                                   RealVector sc2, 
                                                   CostMatrix costMatrix,
                                                   ParametersContainer pc) {
        final int maxRow = sc1.getDimension();
        final int maxCol = sc2.getDimension();
        
        final double dist = costMatrix.get(maxRow - 1, maxCol - 1);
        LinkedList<DTWDistanceInfo.WarpingIndex> path = new LinkedList<>();
        
        float k = 1;
        int row = maxRow - 1;
        int col = maxCol - 1;
        DTWDistanceInfo.WarpingIndex temp = new DTWDistanceInfo.WarpingIndex(row, col, dist);
        path.add (temp);
        while (row >= 0 && col >= 0) {
            List<DTWDistanceInfo.WarpingIndex> neighbors = findNeighbors(searchWindow, row, col);
            if (neighbors.size() < 1) break;
            DTWDistanceInfo.WarpingIndex minw = neighbors.get(0);
            for (DTWDistanceInfo.WarpingIndex wi : neighbors) {
                if (minw.value > wi.value) {
                    minw = wi;
                }
            }
            row = minw.row;
            col = minw.col;
            temp = new DTWDistanceInfo.WarpingIndex(minw.row, minw.col, minw.value);
            k++;
            path.add(temp);
        }
       
        //(dist, new float[s1.length], costMatrix, path, k);
        DTWDistanceInfo dinfo = new DTWDistanceInfo ();
        dinfo.entityOne = sc1;
        dinfo.entityTwo = sc2;
        dinfo.distance = dist;
        dinfo.costMatrix = costMatrix;
        dinfo.normalizingFactor = k;
        dinfo.warpingPath = path;
        dinfo.distancedim = policy.measure (sc1, sc2, dinfo);

        return dinfo;
    }
    public void afterCalculateDTWDistance (RealVector sc1, 
                                           RealVector sc2, 
                                           CostMatrix costMatrix,
                                           ParametersContainer pc) {
        // Empty body
    }
    
    @Override
    public DistanceInfo measure (RealVector sc1, 
                                 RealVector sc2, 
                                 ParametersContainer pc) {
        
        beforeCalculateCostMatrix(sc1, sc2, pc);
        final CostMatrix costMatrix = onCalculateCostMatrix(sc1, sc2, pc);
        afterCalculateCostMatrix(sc1, sc2, costMatrix, pc);
        searchWindow.initialize(costMatrix);
        
        beforeCalculateDTWDistance(sc1, sc2, costMatrix, pc);
        DTWDistanceInfo dinfo = onCalculateDTWDistance(sc1, sc2, costMatrix, pc);
        afterCalculateDTWDistance(sc1, sc2, costMatrix, pc);
        return dinfo;
    }
}
