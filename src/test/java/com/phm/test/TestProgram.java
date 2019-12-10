/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phm.test;

import com.phm.core.data.Features;
import com.phm.core.distrb.DefaultDistributor;
import com.phm.core.distrb.DelayedDistributor;
import org.apache.commons.pipeline.Pipeline;
import org.apache.commons.pipeline.driver.DedicatedThreadStageDriverFactory;
import com.phm.io.CSVFileVectorReader;
import com.phm.core.ds.DefaultOfflineDataStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.pipeline.StageException;
import org.apache.commons.pipeline.stage.AddToCollectionStage;
import org.apache.commons.pipeline.stage.BaseStage;
import org.apache.commons.pipeline.validation.ValidationException;

/**
 *
 * @author phm
 */
public class TestProgram {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ValidationException, StageException {

        CSVFileVectorReader rd = new CSVFileVectorReader(new File("/home/phm/hr1453641815699.csv"), CSVFormat.RFC4180, Charset.defaultCharset());
        LinkedList<RealVector> dd = new LinkedList<>();
        rd.read(dd);
        DefaultOfflineDataStream<Features> ds = new DefaultOfflineDataStream<>("test", null);
        dd.stream().forEach((x) -> {
            ds.add(new Features(x));
        });
        
        DedicatedThreadStageDriverFactory tmp = new DedicatedThreadStageDriverFactory();
        Pipeline pip = new Pipeline();
        
        LinkedList list = new LinkedList();
        pip.addStage(new PrintStage(), tmp);
        pip.addStage(new AddToCollectionStage(list), tmp);
        pip.start();
        
        DelayedDistributor dist = new DelayedDistributor(ds, pip, 1000);
//        DefaultDistributor dist = new DefaultDistributor(ds, pip);
        dist.distribute();
        System.out.println (list.size());
    }
    
    public static class PrintStage extends BaseStage {
        @Override
        public void process(Object obj) throws StageException {
            System.out.println (obj);
            super.emit(obj);
        }
    }
}
