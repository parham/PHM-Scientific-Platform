
package com.phm.biometry.test;

import com.phm.comp.regression.AutoRegression;
import com.phm.comp.regression.DefaultAutoRegression;
import com.phm.comp.regression.Lin2011AutoRegression;
import com.phm.io.CSVFileVectorReader;
import com.phm.io.CSVFileVectorWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.openimaj.ml.regression.LinearRegression;

/**
 *
 * @author phm
 */
public class TestMain4 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        CSVFileVectorReader vr = new CSVFileVectorReader(new File("/home/phm/Dropbox/Projects/Improved Heart Rate Estimation/Experiments/experiments/multiple_hr.csv"), CSVFormat.TDF, Charset.defaultCharset());
        LinkedList<RealVector> list = new LinkedList<>();
        vr.read(list);
        RealVector vec = new ArrayRealVector(list.size());
        for (int index = 0; index < list.size(); index++) {
            vec.setEntry(index, list.get(index).getEntry(0));
        }
        AutoRegression ar = new Lin2011AutoRegression(5);
        
        List<RealVector> reslist = ar.simulate(list);
        CSVFileVectorWriter vw = new CSVFileVectorWriter(new File("/home/phm/Dropbox/Projects/Improved Heart Rate Estimation/Experiments/experiments/regression_hr.csv"), CSVFormat.TDF);
        vw.write(reslist);
    }
    
}
