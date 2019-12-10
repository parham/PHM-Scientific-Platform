
package com.phm.io;

import com.phm.core.data.Features;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/**
 *
 * @author phm
 * @param <DataType>
 */
public class CSVFileDatasetWriter<DataType extends Features> implements DatasetWriter<DataType> {
    
    protected CSVFormat format = CSVFormat.EXCEL;
    protected File file;
    private final CSVPrinter printer;
    
    public CSVFileDatasetWriter (File f, CSVFormat fmt) throws IOException {
        file = Objects.requireNonNull(f);
        format = fmt;
        printer = new CSVPrinter (new FileWriter(file), format);
    }
    
    public File getFile () {
        return file;
    }
    public CSVFormat getCSVFormat () {
        return format;
    }
    public boolean close () {
        try {
            printer.close();
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
    
    @Override
    public boolean write (Collection<? extends DataType> ds) {
        for (DataType x : ds) {
            write (x);
        }
        return true;
    }

    @Override
    public boolean write (DataType data) {
        List<Object> list = new LinkedList<>();
        if (data.code(list)) {
            try {
                printer.printRecord(list);
                printer.flush();
            } catch (IOException ex) {
                Logger.getLogger(CSVFileDatasetWriter.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
            return true;
        }
        return false;
    }
    
}
