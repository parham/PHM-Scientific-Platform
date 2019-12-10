
package com.phm.comp.test;

import com.phm.core.data.DatasetUtils;
import com.phm.io.CSVFileVectorReader;
import com.phm.io.CSVFileVectorWriter;
import flanagan.analysis.Regression;
import flanagan.math.FourierTransform;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector; 

/**
 *
 * @author phm
 */
public class TestMain6 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException { 
        CSVFileVectorReader vr = new CSVFileVectorReader (new File("/home/phm/experiment/v2/heartinfo.ica.csv"), CSVFormat.TDF, Charset.defaultCharset());
        LinkedList<RealVector> data = new LinkedList<>();
        vr.read(data);
        List<RealVector> vecs = DatasetUtils.toChannelRows(data);
        int arrlen = FourierTransform.calcDataLength(true, 16384, 3);
        
        LinkedList<RealVector> res = new LinkedList<>();
        for (RealVector x : vecs) {
            double [] tmp = x.toArray();
            double [] dd = new double [arrlen];
            System.arraycopy(tmp, 0, dd, 0, tmp.length);
            FourierTransform fft = new FourierTransform (dd);
            fft.setDeltaT(20);
    //        fft.setSegmentNumber(3);
            fft.setOverlapOption(true);
            fft.setSegmentLength(tmp.length);
            fft.setSegmentNumber(3);
            fft.setWelch();
            double [][] restmp = fft.powerSpectrum();
            for (int index = 0; index < restmp.length; index++) {
                res.add(new ArrayRealVector(restmp[index]));
            }
        }
        
        CSVFileVectorWriter dw = new CSVFileVectorWriter (new File("/home/phm/doc1.csv"), CSVFormat.TDF);
        dw.write(res);
        dw.close();
    }
    
}
