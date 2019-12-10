
package com.phm.comp.test;

import com.phm.comp.filter.detrend.FlindersDetrendFilter;
import com.phm.comp.filter.detrend.MpimentelDetrend;
import com.phm.comp.filter.detrend.SimpleDetrendFilter;
import com.phm.comp.filter.welch.WelchMethodFilter;
import com.phm.core.data.Features;
import java.util.LinkedList;
import java.util.Random;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author phm
 */
public class TestDetrend {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Random rand = new Random();
        LinkedList<Features> ds = new LinkedList<>();
        
//        double [] arr = new double [200];
//        for (int index = 0; index < arr.length; index++) {
//            arr [index] = rand.nextDouble();
//        }
//        FlindersDetrendFilter ff = new FlindersDetrendFilter (1);
//        SimpleDetrendFilter ff = new SimpleDetrendFilter(3);
//        RealVector resvec = ff.filter(new ArrayRealVector(arr));
        double [][] arr = new double [2][200];
        for (int index = 0; index < arr.length; index++) {
            arr [0][index] = rand.nextDouble();
            arr [1][index] = index;
        }
        WelchMethodFilter welch = new WelchMethodFilter();
        welch.compute (arr, 128, 0, 4, arr.length, false, true);
        System.out.println ();
    }
    
}
