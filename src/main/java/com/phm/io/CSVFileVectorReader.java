
package com.phm.io;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author PHM
 */
public class CSVFileVectorReader implements VectorReader {

    protected Charset charset;
    protected CSVFormat format;
    protected File fins;
    protected CSVParser parser;
    
    public CSVFileVectorReader (File fin, CSVFormat frmt, Charset chs) throws IOException {
        format = Objects.requireNonNull(frmt);
        charset = Objects.requireNonNull(chs);
        fins = Objects.requireNonNull(fin);
        parser = CSVParser.parse (fins, charset, format);
    }

    public CSVFormat getCSVFormat () {
        return format;
    }
    public Charset getCharset () {
        return charset;
    }
    
    @Override
    public boolean read(Collection<RealVector> ds) {
        try {
            List<CSVRecord> records = parser.getRecords();
            for (CSVRecord c : records) {
                double [] values = new double [c.size()];
                for (int dim = 0; dim < values.length; dim++) {
                    String v = c.get(dim);
                    values [dim] = v != null ? Double.valueOf(v) : Double.NaN;
                }
                RealVector inst = new ArrayRealVector(values);
                ds.add(inst);
            }
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
}
