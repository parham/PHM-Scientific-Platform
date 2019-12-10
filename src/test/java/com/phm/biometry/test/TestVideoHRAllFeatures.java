
package com.phm.biometry.test;

import com.phm.biometry.core.ImageInstance;
import com.phm.biometry.core.ImageListToFeaturesListConverter;
import com.phm.biometry.core.ImageToStatisticalFeaturesConverter;
import com.phm.biometry.core.VideoSource;
import com.phm.biometry.face.CLMInstance;
import com.phm.biometry.face.CLMToFacialRegionsConverter;
import com.phm.biometry.face.ImageToCLMConverter;
import com.phm.core.data.converter.FeaturesListToFeaturesConverter;
import com.phm.core.data.Features;
import com.phm.core.data.InstanceList;
import com.phm.core.ds.DataStream; 
import com.phm.io.CSVFileDatasetWriter;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import org.apache.commons.csv.CSVFormat;
import org.openimaj.video.xuggle.XuggleVideo;

/**
 *
 * @author phm
 */
public class TestVideoHRAllFeatures {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException { 
//        for (int index = 2; index < 40; index++) {
//            // File path = new File("/home/phm/BACKUP/Datasets/deapvideos/face_video/s02/");
//            File path = new File("/home/phm/Videos/Webcam/");
//            String fname = String.format("b.webm", index);
//            
//            System.out.println (">>>>>>>>>> " + fname);
//            
//            XuggleVideo xvs = new XuggleVideo(new File (path, fname));
//            VideoSource vs = new VideoSource (xvs);
//            vs.open();
//            DataStream<ImageInstance> ds = vs.getImageStream ();
//            DataStreamConverter<ImageInstance, CLMInstance> dsclm = 
//                    new DataStreamConverter<> (ds, new ImageToCLMConverter(1.4f));
//            DataStreamConverter<CLMInstance, InstanceList<ImageInstance>> dsreg = 
//                    new DataStreamConverter<> (dsclm, new CLMToFacialRegionsConverter());
//            DataStreamConverter<InstanceList<ImageInstance>, InstanceList<Features>> dscin =
//                    new DataStreamConverter<> (dsreg, new ImageListToFeaturesListConverter(
//                    new ImageToStatisticalFeaturesConverter()));
//            DataStreamConverter<InstanceList<Features>, Features> dsc = 
//                    new DataStreamConverter(dscin,
//                    new FeaturesListToFeaturesConverter());
//
//            LinkedList<Features> res = new LinkedList<>();
////            int index = 0;
//            while (dsc.hasMore()) {
//                System.out.println (index++);
//                Features ft = dsc.next();
//                if (ft != null) {
//                    ft.setLabel("null");
//                    res.add(ft);
//                }
//            }
//            
//            vs.close();
//            CSVFileDatasetWriter dw = new CSVFileDatasetWriter(new File(path, fname + ".csv"), CSVFormat.TDF);
//            dw.write(res);
//            dw.close();
//        }
//        
    }
    
}
