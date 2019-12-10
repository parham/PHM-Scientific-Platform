/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phm.biometry.test;

import com.phm.biometry.core.ImageInstance;
import com.phm.biometry.core.ImageToStatisticalFeaturesConverter;
import com.phm.biometry.core.VideoSource;
import com.phm.biometry.face.CLMInstance;
import com.phm.biometry.face.CLMToFaceRegionConverter;
import com.phm.biometry.face.ImageToCLMConverter;
import com.phm.core.data.Features;
import com.phm.core.distrb.DefaultDistributor;
import com.phm.core.ds.DataStream; 
import com.phm.core.stage.InstanceConverterStage;
import com.phm.io.CSVFileVectorWriter;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.pipeline.Pipeline;
import org.apache.commons.pipeline.StageDriverFactory;
import org.apache.commons.pipeline.StageException;
import org.apache.commons.pipeline.driver.DedicatedThreadStageDriverFactory;
import org.apache.commons.pipeline.stage.AddToCollectionStage;
import org.apache.commons.pipeline.validation.ValidationException;
import org.openimaj.video.xuggle.XuggleVideo;

/**
 *
 * @author phm
 */
public class TestMain3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ValidationException, StageException {
        XuggleVideo xvs = new XuggleVideo("/home/phm/BACKUP/Datasets/deapvideos/face_video/s01/s01_trial01.avi");
        VideoSource vs = new VideoSource(xvs);
        vs.start();
        DataStream<ImageInstance> ds = vs.getImageStream ();
        
        StageDriverFactory drstg = new DedicatedThreadStageDriverFactory();
        Pipeline pip = new Pipeline();
        InstanceConverterStage dsclm = new InstanceConverterStage(new ImageToCLMConverter(1.4f));
        pip.addStage(dsclm, drstg);
        InstanceConverterStage dsreg = new InstanceConverterStage(new CLMToFaceRegionConverter());
        pip.addStage(dsreg, drstg);
        InstanceConverterStage dsc = new InstanceConverterStage(new ImageToStatisticalFeaturesConverter());
        pip.addStage(dsc, drstg);
        LinkedList<Features> dataset = new LinkedList<>();
        AddToCollectionStage<Features> resds = new AddToCollectionStage<>(dataset);
        pip.addStage(resds, drstg);
        pip.start();
        
        DefaultDistributor dist = new DefaultDistributor(ds, pip);
        dist.distribute();
//        DataStreamConverter<ImageInstance, CLMInstance> dsclm = 
//                new DataStreamConverter<> (ds, new ImageToCLMConverter(1.4f));
//        DataStreamConverter<CLMInstance, ImageInstance> dsreg = 
//                new DataStreamConverter<> (dsclm, new CLMToFaceRegionConverter());
//        DataStreamConverter<ImageInstance, Features> dsc =
//                new DataStreamConverter<> (dsreg, new ImageToStatisticalFeaturesConverter());
        vs.stop();
        
        CSVFileVectorWriter vw = new CSVFileVectorWriter(new File("/home/phm/res.csv"), CSVFormat.EXCEL);
        vw.write(dataset);
        vw.close();
    }
    
}
