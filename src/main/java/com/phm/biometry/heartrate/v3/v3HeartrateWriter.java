
package com.phm.biometry.heartrate.v3;

import com.phm.biometry.heartrate.HeartrateInfo;
import com.phm.core.data.Features;
import com.phm.io.CSVFileDatasetWriter;
import com.phm.io.CSVFileVectorWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import org.apache.commons.csv.CSVFormat;

/**
 *
 * @author phm
 */
public class v3HeartrateWriter {
    
    protected File dirFile;
    
    public v3HeartrateWriter (String dirpath) throws IOException {
        dirFile = new File(dirpath);
    }
    
    public void setDirectory (String dir) {
        dirFile = new File(dir);
    }
    public File getDirectory () {
        return dirFile;
    }
    
    public boolean write (v3HeartrateInfo info) throws IOException {
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
            File dataf = new File(dirFile, v3HeartrateInfo.PARAM_DATA + ".csv");
            CSVFileDatasetWriter<Features> csvdata = new CSVFileDatasetWriter<>(dataf, CSVFormat.TDF);
            csvdata.write(info.data);
            csvdata.close();
            // Interpolation
            File interpf = new File(dirFile, v3HeartrateInfo.PARAM_INTERPOLATE + ".csv");
            CSVFileVectorWriter csvinterp = new CSVFileVectorWriter (interpf, CSVFormat.TDF);
            csvinterp.write (info.interpolate);
            csvinterp.close();
            // Normalization
            File normf = new File(dirFile, v3HeartrateInfo.PARAM_NORMALIZE + ".csv");
            CSVFileVectorWriter csvnorm = new CSVFileVectorWriter (normf, CSVFormat.TDF);
            csvnorm.write(info.normalize);
            csvnorm.close();
            // ICA
            File icaf = new File(dirFile, v3HeartrateInfo.PARAM_ICA + ".csv");
            CSVFileVectorWriter csvica = new CSVFileVectorWriter (icaf, CSVFormat.TDF);
            csvica.write(info.ica);
            csvica.close();
            // FFT
            File fftf = new File(dirFile, v3HeartrateInfo.PARAM_DETRENDING + ".csv");
            CSVFileVectorWriter csvfft = new CSVFileVectorWriter (fftf, CSVFormat.TDF);
            csvfft.write(info.detrending);
            csvfft.close();
            // peakpoints
            File ppf = new File(dirFile, v3HeartrateInfo.PARAM_NOISEFILTER + ".csv");
            CSVFileVectorWriter csvpp = new CSVFileVectorWriter (ppf, CSVFormat.TDF);
            csvpp.write(info.noiseFilter);
            csvpp.close();
            // Convert
            File convf = new File(dirFile, v3HeartrateInfo.PARAM_SMOOTH + ".csv");
            CSVFileVectorWriter csvconv = new CSVFileVectorWriter (convf, CSVFormat.TDF);
            csvconv.write(info.smooth);
            csvconv.close();
            // Welch
            File welchf = new File(dirFile, v3HeartrateInfo.PARAM_WELCH + ".csv");
            CSVFileVectorWriter csvwelch = new CSVFileVectorWriter (welchf, CSVFormat.TDF);
            csvconv.write(info.welch);
            csvconv.close();
            
            return true;
        }
        return false;
    }
}
