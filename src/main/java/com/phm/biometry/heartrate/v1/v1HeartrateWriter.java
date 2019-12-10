
package com.phm.biometry.heartrate.v1;

import com.phm.biometry.heartrate.HeartrateInfo;
import com.phm.core.data.Features;
import com.phm.io.CSVFileDatasetWriter;
import com.phm.io.CSVFileVectorWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Properties;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author phm
 */
public class v1HeartrateWriter {
    
    protected File dirFile;
    
    public v1HeartrateWriter (String dirpath) throws IOException {
        dirFile = new File(dirpath);
    }
    
    public void setDirectory (String dir) {
        dirFile = new File(dir);
    }
    public File getDirectory () {
        return dirFile;
    }
    
    public boolean write (v1HeartrateInfo info) throws IOException {
        HashMap<String, Object> map = new HashMap<>();
        if (info.code(map)) {
            // HeartrateInfo
            Properties prop = new Properties();
            prop.put(HeartrateInfo.PARAM_START, String.valueOf(info.getStartTime()));
            prop.put(HeartrateInfo.PARAM_END, String.valueOf(info.getEndTime()));
            prop.put(HeartrateInfo.PARAM_CONFIDENT, String.valueOf (info.getConfidence()));
            prop.put(HeartrateInfo.PARAM_HEARTRATE, String.valueOf(info.getHeartrate()));
            prop.put(HeartrateInfo.PARAM_VALID, String.valueOf(info.isValid()));
            /////
            ArrayList<Double> arr  = new ArrayList<>(info.getPossibleHeartrate());
            String arrhr = "";
            for (Double x : arr) {
                arrhr += x + " ";
            }
            prop.put(HeartrateInfo.PARAM_POSSIBLE_HEARTRATE, arrhr);
            /////
            File hrinfo = new File(dirFile, "heartrate_info.csv");
            FileWriter hrw = new FileWriter(hrinfo);
            prop.store(hrw, "Parham Nooralishahi");
            hrw.close();
            // Signal
            File dataf = new File(dirFile, v1HeartrateInfo.PARAM_DATA + ".csv");
            CSVFileDatasetWriter<Features> csvdata = new CSVFileDatasetWriter<>(dataf, CSVFormat.TDF);
            csvdata.write(info.data);
            csvdata.close();
            // Interpolation
            File interpf = new File(dirFile, v1HeartrateInfo.PARAM_INTERPOLATE + ".csv");
            CSVFileDatasetWriter<Features> csvinterp = new CSVFileDatasetWriter<>(interpf, CSVFormat.TDF);
            csvinterp.write (info.interpolate);
            csvinterp.close();
            // Normalization
            File normf = new File(dirFile, v1HeartrateInfo.PARAM_NORMALIZE + ".csv");
            CSVFileDatasetWriter<Features> csvnorm = new CSVFileDatasetWriter<>(normf, CSVFormat.TDF);
            csvnorm.write(info.normalize);
            csvnorm.close();
            // ICA
            File icaf = new File(dirFile, v1HeartrateInfo.PARAM_ICA + ".csv");
            CSVFileVectorWriter csvica = new CSVFileVectorWriter (icaf, CSVFormat.TDF);
//            LinkedList<RealVector> vecica = new LinkedList<>();
//            double [] arrica = info.ica.toArray();
//            for (int index = 0; index < arrica.length; index++) {
//                double [] tmp = {arrica [index]};
//                vecica.add(new ArrayRealVector(tmp));
//            }
            csvica.write(info.ica);
            csvica.close();
            // FFT
            File fftf = new File(dirFile, v1HeartrateInfo.PARAM_FFT + ".csv");
            CSVFileVectorWriter csvfft = new CSVFileVectorWriter (fftf, CSVFormat.TDF);
            LinkedList<RealVector> vecfft = new LinkedList<>();
            double [] arrfft = info.fft.toArray();
            for (int index = 0; index < arrfft.length; index++) {
                double [] tmp = {arrfft [index]};
                vecfft.add(new ArrayRealVector(tmp));
            }
            csvfft.write(vecfft);
            csvfft.close();
            // peakpoints
            File ppf = new File(dirFile, v1HeartrateInfo.PARAM_PEAKPOINT + ".csv");
            CSVFileVectorWriter csvpp = new CSVFileVectorWriter (ppf, CSVFormat.TDF);
            LinkedList<RealVector> vecpp = new LinkedList<>();
            double [] arrpp = info.peakpoints.toArray();
            for (int index = 0; index < arrpp.length; index++) {
                double [] tmp = {arrpp [index]};
                vecpp.add(new ArrayRealVector(tmp));
            }
            csvpp.write(vecpp);
            csvpp.close();
            // Convert
            File convf = new File(dirFile, v1HeartrateInfo.PARAM_CONVERT + ".csv");
            CSVFileVectorWriter csvconv = new CSVFileVectorWriter (convf, CSVFormat.TDF);
            LinkedList<RealVector> vecconv = new LinkedList<>();
            double [] arrconv = info.convert.toArray();
            for (int index = 0; index < arrconv.length; index++) {
                double [] tmp = {arrconv [index]};
                vecconv.add(new ArrayRealVector(tmp));
            }
            csvconv.write(vecconv);
            csvconv.close();
            // Calculate
            File calf = new File(dirFile, v1HeartrateInfo.PARAM_CALCULATE + ".csv");
            CSVFileVectorWriter csvcal = new CSVFileVectorWriter (calf, CSVFormat.TDF);
            LinkedList<RealVector> veccal = new LinkedList<>();
            double [] arrcal = info.calculate.toArray();
            for (int index = 0; index < arrcal.length; index++) {
                double [] tmp = {arrcal [index]};
                veccal.add(new ArrayRealVector(tmp));
            }
            csvcal.write(veccal);
            csvcal.close();
            // final process
            File fpf = new File(dirFile, v1HeartrateInfo.PARAM_FINALPROCESS + ".csv");
            CSVFileVectorWriter csvfp = new CSVFileVectorWriter (fpf, CSVFormat.TDF);
            LinkedList<RealVector> vecfp = new LinkedList<>();
            double [] arrfp = info.finalprocess.toArray();
            for (int index = 0; index < arrfp.length; index++) {
                double [] tmp = {arrfp [index]};
                vecfp.add(new ArrayRealVector(tmp));
            }
            csvfp.write(vecfp);
            csvfp.close();
            
            return true;
        }
        return false;
    }
}
