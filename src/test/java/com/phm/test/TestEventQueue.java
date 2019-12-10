
package com.phm.test;

import com.google.common.collect.EvictingQueue;
import com.google.common.eventbus.Subscribe;

/**
 *
 * @author phm
 */
public class TestEventQueue {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        double [][] b = {{4, 25, 12}, {57, 13, 4}, {2, 95, 34}};
//        RealMatrix B = new Array2DRowRealMatrix(b);
//        double [] a = {32, 12, 53};
//        RealMatrix A = new Array2DRowRealMatrix(a);
//        RealMatrix x = new LUDecomposition(B).getSolver().solve(A);
//        System.out.println(x.getColumnDimension());
        EvictingQueue<Integer> tmp = EvictingQueue.create(10);
        for (int index = 0; index < 5; index++) {
            tmp.add(index);
            System.out.print (index + "\t");
        }
        System.out.println ();
        tmp.remove();
        tmp.remove();
        tmp.stream().forEach((x) -> {
            System.out.print (x + "\t");
        });
        System.out.println ();
    }
    
    @Subscribe
    public void test (String str) {
        System.out.println (str);
    }
    @Subscribe
    public void test2 (Integer index) {
        System.out.println ("INDEX : " + index);
    }
}
