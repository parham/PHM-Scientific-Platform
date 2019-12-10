
package com.phm.core.terminal;

import com.phm.core.data.Features;
import com.phm.core.ds.BufferedDataStream;
import com.phm.core.ds.DefaultOfflineDataStream;
import com.phm.io.CSVFileDatasetWriter;
import java.io.File;
import java.io.IOException; 
import org.apache.commons.csv.CSVFormat;

/**
 *
 * @author phm
 */
public class CSVFeaturesOutcome extends Outcome {

    public static final String FEATURES_STREAM = "csv.stream";
    public static final String OUTCOME_NAME = "csv.features.outcome";
    
    protected BufferedDataStream<Features> streamObj;
    protected File streamFile;
    protected CSVFormat formatObj;
    protected CSVFileDatasetWriter writer;
    
    public CSVFeaturesOutcome (File file, CSVFormat frm) {
        super(OUTCOME_NAME);
        init(file, frm);
    }

    private void init (File file, CSVFormat frm) {
        streamFile = file;
        formatObj = frm;
    }
    
    @Override
    protected boolean initialize() {
        streamObj = new DefaultOfflineDataStream(FEATURES_STREAM, null);
        streams.put(FEATURES_STREAM, streamObj);
        return true;
    }

    @Override
    protected boolean activate() {
        return true;
    }

    @Override
    protected boolean deactivate() {
        return true;
    }

    @Override
    protected boolean terminate() {
        try {
            writer = new CSVFileDatasetWriter(streamFile, formatObj);
            writer.write(streamObj);
            writer.close();
            return true;
        } catch (IOException ex) {
            log.error(ex);
            return false;
        }
    }
}
