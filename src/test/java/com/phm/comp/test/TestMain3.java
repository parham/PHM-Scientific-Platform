
package com.phm.comp.test;

import com.phm.core.data.Features;
import com.phm.core.data.IDFeatures; 
import com.phm.io.CSVFileDatasetReader;
import com.phm.io.CSVFileDatasetWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.math3.distribution.BetaDistribution;
import org.apache.commons.math3.distribution.RealDistribution;

/**
 *
 * @author phm
 */
public class TestMain3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        RealDistribution d = new BetaDistribution(0.0, 0.0);
        Random rand = new Random(System.currentTimeMillis());
        List<IDFeatures> list = new LinkedList<>();
        for (int index = 0; index < 100; index++) {
            double [] tmp = new double[20];
            for (int dim = 0; dim < tmp.length; dim++) {
                tmp [dim] = rand.nextDouble();
            }
            IDFeatures f = new IDFeatures (index, System.currentTimeMillis(), "LABEL_" + index, tmp);
            list.add(f);
        }
        CSVFileDatasetWriter<IDFeatures> csv = new CSVFileDatasetWriter (new File("/home/phm/a.csv"), CSVFormat.TDF);
        
        if (!csv.write(list)) {
            System.out.println ("Writing is failed ...");
            return;
        }
        csv.close();
        System.out.println ("Writing is finished ...");
        
        CSVFileDatasetReader csvr = new CSVFileDatasetReader(new File("/home/phm/a.csv"), CSVFormat.TDF, Charset.defaultCharset());
        List<Features> rlist = new LinkedList<>();
        if (!csvr.read(rlist)) {
            System.out.println ("Reading is failed ...");
            return;
        }
        csvr.close();
    }
    
}
