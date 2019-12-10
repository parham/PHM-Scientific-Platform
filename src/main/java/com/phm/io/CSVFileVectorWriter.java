
package com.phm.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author PHM
 */
public class CSVFileVectorWriter implements VectorWriter {

    protected CSVFormat format = CSVFormat.EXCEL;
    protected File file;
    protected CSVPrinter printer;
    
    public CSVFileVectorWriter (File f, CSVFormat fmt) throws IOException {
        file = Objects.requireNonNull(f);
        format = fmt;
        printer = new CSVPrinter (new FileWriter(file), format);
    }
    
    public CSVFormat getCSVFormat () {
        return format;
    }
    
    @Override
    public boolean write(RealVector x) {
        try {
            double [] xarr = x.toArray();
            LinkedList<String> tmp = new LinkedList<>();
            for (int index = 0; index < xarr.length; index++) {
                tmp.add(String.valueOf(xarr [index]));
            }
            printer.printRecord (tmp);
            printer.flush();
            return true;
        } catch (IOException ex) {
            Logger.getLogger(CSVFileVectorWriter.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    @Override
    public boolean write(Collection<? extends RealVector> ds) {
        ds.stream().forEach((RealVector x) -> {
            write(x);
        });
        return true;
    }
    
    public boolean close () {
        try {
            printer.close();
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
}
