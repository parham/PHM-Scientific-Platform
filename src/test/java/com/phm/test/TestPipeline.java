
package com.phm.test;

import com.google.common.collect.ForwardingQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.commons.pipeline.Feeder;
import org.apache.commons.pipeline.Pipeline;
import org.apache.commons.pipeline.StageException;
import org.apache.commons.pipeline.driver.DedicatedThreadStageDriverFactory;
import org.apache.commons.pipeline.stage.BaseStage;
import org.apache.commons.pipeline.util.QueueFactory;
import org.apache.commons.pipeline.validation.ValidationException;

/**
 *
 * @author phm
 */
public class TestPipeline {

    /**
     * @param args the command line arguments
     * @throws org.apache.commons.pipeline.validation.ValidationException
     * @throws org.apache.commons.pipeline.StageException
     */
    public static void main(String[] args) throws ValidationException, StageException {
        DedicatedThreadStageDriverFactory tmp = new DedicatedThreadStageDriverFactory();
        Pipeline p = new Pipeline();
        p.addStage(new PrintStage(0), tmp);
        p.addStage(new PrintStage(1), tmp);
        p.addStage(new PrintEndLineStage(), tmp);
        p.start();
        Feeder fd = p.getSourceFeeder();
        for (int index = 0; index < 10; index++) {
            fd.feed("Parham");
        }
        p.finish();
        Queue<String> a = new ConcurrentLinkedQueue<>();
        a.add("PHM");
        
    }
    
    public static class PrintStage extends BaseStage {
        public int index = 0;
        public PrintStage (int ind) {
            index = ind;
        }
        @Override
        public void process(Object obj) throws StageException {
//            System.out.println (obj + " " + index);
            //super.process(obj);
            this.emit(obj.toString() + " " + index + " ");
        }   
    }
    public static class PrintEndLineStage extends BaseStage {
        @Override
        public void process(Object obj) throws StageException {
            System.out.println (obj);
//            super.process(obj);
        }   
    }    
}
