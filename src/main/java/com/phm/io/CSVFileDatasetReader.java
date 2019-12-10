
package com.phm.io;

import com.phm.core.data.Features;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author PHM
 */
public class CSVFileDatasetReader implements DatasetReader<Features> {

    protected Charset charset;
    protected CSVFormat format;
    protected File fins;
    private final CSVParser parser;
    protected LinkedList<CSVRecord> records = new LinkedList<>();
    
    public CSVFileDatasetReader (File fin, CSVFormat frmt, Charset chs) throws IOException {
        format = Objects.requireNonNull(frmt);
        charset = Objects.requireNonNull(chs);
        fins = Objects.requireNonNull(fin);
        parser = CSVParser.parse (fins, charset, format);
        records.addAll(parser.getRecords());
    }
    
    public CSVFormat getCSVFormat () {
        return format;
    }
    public Charset getCharset () {
        return charset;
    }
    public boolean close () {
        try {
            parser.close();
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    @Override
    public boolean read(Collection<Features> ds) {
        while (!records.isEmpty()) {
            Features rec = read();
            if (rec != null) {
                ds.add(rec);
            }
        }
        return true;
    }

    @Override
    public Features read() {
        CSVRecord crec = records.removeFirst();
        Features ft = new Features();
        LinkedList<Object> list = new LinkedList<>();
        for (int index = 0; index < crec.size(); index++) {
            list.add(crec.get(index));
        }
        if (ft.decode(list)) {
            return ft;
        }
        return null;
    }
}
