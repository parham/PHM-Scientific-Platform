
package com.phm.comp.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author phm
 */
public class NormalizeDatasetFilter implements DatasetFilter {

    public NormalizeDatasetFilter () {
        // Empty body
    }

    @Override
    public List<RealVector> filter(List<? extends RealVector> tds) {
        List<RealVector> ds = new LinkedList<>();
        for (RealVector x : tds) {
            ds.add(x.copy());
        }
        int numdim = ds.get(0).getDimension();
        for (int dim = 0; dim < numdim; dim++) {
            ArrayList<Double> dt = new ArrayList<> ();
            for (int index = 0; index < ds.size(); index++) {
                dt.add(ds.get(index).getEntry(dim));
            }
            double min = Collections.min (dt);
            double max = Collections.max (dt);
            // Normalize dataset
            for (RealVector d : ds) {
                double v = d.getEntry(dim);
                v = (v - min) / (max - min);
                d.setEntry(dim, v);
            }
        }
        return ds;
    }
}
