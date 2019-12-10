
package com.phm.comp.filter.ica.fastica;

import com.phm.core.data.DatasetUtils;
import com.phm.comp.filter.DatasetFilter;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author Parham Nooralishahi - PHM!
 */
public class FastICA implements DatasetFilter {
    
    FastICAConfig icaConfig;
    ContrastFunction conObj;
    EigenValueFilter eigenObj;
    
    public FastICA (FastICAConfig config,
                    ContrastFunction con,
                    EigenValueFilter eigenfilter) {
        icaConfig = config;
        conObj = con;
        eigenObj = eigenfilter;
    }

    @Override
    public List<RealVector> filter(List<? extends RealVector> ds) {
        LinkedList<RealVector> res = new LinkedList<>();
        if (ds != null && ds.size() > 0) {
            RealMatrix mat = DatasetUtils.toMatrix(ds);
            try {
                FastICAAlgorithm fica = new FastICAAlgorithm(mat.getData(), icaConfig, conObj, eigenObj);
                double [][] resmat = fica.getICVectors();
                res.addAll (DatasetUtils.create(resmat));
            } catch (FastICAException ex) {
                return res;
            }
        }
        return res; 
    }
    
}
