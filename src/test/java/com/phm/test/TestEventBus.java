
package com.phm.test;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

/**
 *
 * @author phm
 */
public class TestEventBus {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TM0<String> tm0 = new TM0<>();
        tm0.register(new TM2());
        tm0.register(new TM4());
        tm0.start();
    }
    
    public static class TM0<DT> {
        EventBus eb = new EventBus("TEST");
        public void start () {
            for (int index = 0; index < 10; index++) {
//                eb.post(" ID : " + index);
                eb.post((float) index);
            }
        }
        public void register (Object obj) {
            eb.register(obj);
        }
    }
    
    public static interface TM1<DT> {
        public void test (DT d);
    }
    
    public static class TM2 implements TM1<String> {
        @Subscribe 
        public void test (String d) {
            System.out.println ("STRING " +  d);
//            if (d instanceof String) {
//                System.out.println ("String " +  d + " " + id);
//            } else if (d instanceof Float) {
//                System.out.println ("Float " +  d + " " + id);
//            }
        }
    }
    public static class TM3<DT> implements TM1<DT> {
        @Override
        public void test(DT d) {
            
        }
    }
    public static class TM4 extends TM3<Float> {
        @Subscribe 
        public void test (Float d) {
            System.out.println ("FLOAT " +  d);
//            if (d instanceof String) {
//                System.out.println ("String " +  d + " " + id);
//            } else if (d instanceof Float) {
//                System.out.println ("Float " +  d + " " + id);
//            }
        }
    }
}
