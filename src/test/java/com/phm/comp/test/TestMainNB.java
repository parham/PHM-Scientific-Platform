
package com.phm.comp.test;

import com.phm.comp.classifier.MulticlassNaiveBayesClassifier;
import com.phm.core.data.VectorInstance;
import java.util.Arrays;
import java.util.LinkedList;

/**
 *
 * @author phm
 */
public class TestMainNB {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LinkedList<VectorInstance<String>> ds = new LinkedList<>();
        ds.add(new VectorInstance(0, "1", Arrays.asList("Parham", "PHM", "PTE", "Cool")));
        ds.add(new VectorInstance(0, "1", Arrays.asList("Parham", "PHM", "Boy", "Cool")));
        ds.add(new VectorInstance(0, "1", Arrays.asList("Pouyan", "PON", "Boy", "Cool")));
        ds.add(new VectorInstance(0, "1", Arrays.asList("Mehdi", "PON", "QWE", "Cool")));
        ds.add(new VectorInstance(0, "2", Arrays.asList("Maryam", "MYR", "Girl", "Awesome")));
        ds.add(new VectorInstance(0, "2", Arrays.asList("Narges", "MYR", "GirVC", "Awesqwome")));
        ds.add(new VectorInstance(0, "2", Arrays.asList("Maryam", "MYR", "Girczl", "Awesvnome")));
        ds.add(new VectorInstance(0, "2", Arrays.asList("Maryam", "MYR", "Girl", "Awesewqtvome")));
        MulticlassNaiveBayesClassifier<String> cs = new MulticlassNaiveBayesClassifier<>();
        cs.build(ds);
        Object d = cs.classify(new VectorInstance(0, null, Arrays.asList("Maryam", "MYR", "GirVC", "Awesewqtvome")));
        System.out.println (d);
    }
    
}
