    
package com.phm.core.terminal;

import com.phm.core.data.Features;
import com.phm.core.ds.DataStream;
import com.phm.core.ds.DefaultOfflineDataStream;
import com.phm.io.CSVFileDatasetReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;

/**
 *
 * @author phm
 */
public class CSVFeaturesSource extends Source {
    
    public static final String FEATURES_STREAM = "csv.stream";
    public static final String SOURCE_NAME = "csv.features.source";
    
    protected DefaultOfflineDataStream<Features> streamObj;
    protected File streamFile;
    protected CSVFormat formatObj;
    protected Charset charsetObj;

    public CSVFeaturesSource (File file, CSVFormat format, Charset chset) {
        super (SOURCE_NAME);
        init (file, format, chset);
    }
    
    private void init (File file, CSVFormat frm, Charset chset) {
        streamFile = file;
        formatObj = frm;
        charsetObj = chset;
    }
    
    @Override
    public Map<String, DataStream> streams() {
        HashMap<String, DataStream> map = new HashMap<>();
        map.put(FEATURES_STREAM, streamObj);
        return map;
    }

    @Override
    public DataStream stream(String stname) {
        if (stname.contentEquals(FEATURES_STREAM)) {
            return streamObj;
        }
        return null;
    }

    @Override
    protected boolean initialize() {
        CSVFileDatasetReader csvreader = null;
        streamObj = new DefaultOfflineDataStream (FEATURES_STREAM, this);
        try {
            csvreader = new CSVFileDatasetReader (streamFile, formatObj, charsetObj);
            LinkedList<Features> list = new LinkedList<> ();
            if (csvreader.read(list)) {
                return streamObj.addAll(list);
            }
            return false;
        } catch (IOException ex) {
            log.error(ex);
            return false;
        }
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
        return true;
    }
}
