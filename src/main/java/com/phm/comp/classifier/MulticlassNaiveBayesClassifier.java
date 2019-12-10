
package com.phm.comp.classifier;

import com.phm.annotations.PublicationDetails;
import com.phm.annotations.PublicationType;
import com.phm.core.data.DatasetUtils;
import com.phm.core.data.Instance;
import com.phm.core.data.VectorLikeInstance;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

@PublicationDetails (
        author = {"Zhang, H."},
        type = PublicationType.Journal,
        title =  "The optimality of naive Bayes",
        journal = "AA",
        volume = "1(2)",
        year = "2004",
        paperAbstract = "Naive Bayes is one of the most efficient and effective\n" +
                    "inductive learning algorithms for machine learning and\n" +
                    "data mining. Its competitive performance in classification\n" +
                    "is surprising, because the conditional independence\n" +
                    "assumption on which it is based, is rarely true in realworld\n" +
                    "applications. An open question is: what is the\n" +
                    "true reason for the surprisingly good performance of\n" +
                    "naive Bayes in classification?\n" +
                    "In this paper, we propose a novel explanation on the\n" +
                    "superb classification performance of naive Bayes. We\n" +
                    "show that, essentially, the dependence distribution; i.e.,\n" +
                    "how the local dependence of a node distributes in each\n" +
                    "class, evenly or unevenly, and how the local dependencies\n" +
                    "of all nodes work together, consistently (supporting\n" +
                    "a certain classification) or inconsistently (canceling\n" +
                    "each other out), plays a crucial role. Therefore,\n" +
                    "no matter how strong the dependences among attributes\n" +
                    "are, naive Bayes can still be optimal if the dependences\n" +
                    "distribute evenly in classes, or if the dependences cancel\n" +
                    "each other out. We propose and prove a sufficient\n" +
                    "and necessary conditions for the optimality of naive\n" +
                    "Bayes. Further, we investigate the optimality of naive\n" +
                    "Bayes under the Gaussian distribution. We present and\n" +
                    "prove a sufficient condition for the optimality of naive\n" +
                    "Bayes, in which the dependence between attributes do\n" +
                    "exist. This provides evidence that dependence among\n" +
                    "attributes may cancel out each other. In addition, we\n" +
                    "explore when naive Bayes works well."
)
public class MulticlassNaiveBayesClassifier<E> implements OfflineClassifier<VectorLikeInstance<E>> {
    
    private double numClasses = 0.0;
    private double numInstances = 0.0;
    private List<Object> clss;
    private List<E> items;
    HashMap<Object, Double> clssOccurrence;
    private RealMatrix matOccurrences;
    
    public MulticlassNaiveBayesClassifier () {
        // Empty body
    }
    
    @Override
    public void build(Collection<? extends VectorLikeInstance<E>> ds) {
        // Set of classes
        clss = new LinkedList<>(DatasetUtils.classes (ds));
        numClasses = clss.size();
        // Calculate Class occurence
        numInstances = ds.size();
        clssOccurrence = new HashMap<>();
        for (Object lbl : clss) {
            int numoc = 0;
            for (Instance x : ds) {
                if (x.getLabel().equals(lbl)) numoc++;
            }
            clssOccurrence.put(lbl, (double) numoc);
        }
        // Calculate items occurrences
        HashSet<E> it = new HashSet<>();
        for (VectorLikeInstance<E> vec : ds) {
            it.addAll(vec.items());
        }
        items = new LinkedList<>(it);
        matOccurrences = new Array2DRowRealMatrix(items.size(), clss.size());
        for (VectorLikeInstance<E> vec : ds) {
            Object lbl = vec.getLabel();
            int col = clss.indexOf(lbl);
            for (E x : vec.items()) {
                int row = items.indexOf(x);
                matOccurrences.setEntry(row, col, matOccurrences.getEntry(row, col) + 1);
            }
        }
    }

    @Override
    public Object classify(VectorLikeInstance<E> entity) {
        Map<Object, Double> map = classDistribution(entity);
        Object max = null;
        for (Object x : map.keySet()) {
            if (max == null || map.get(x) > map.get(max)) {
                max = x;
            }
        }
        return max;
    }

    @Override
    public ClassProbabilityDistribution classDistribution(VectorLikeInstance<E> data) {
        ClassProbabilityDistribution res = new ClassProbabilityDistribution();
        for (Object x : clss) {
            double pLabel = clssOccurrence.get(x) / numInstances;
            double [] pItems = new double [data.items().size()];
            // Convert probabilities to LOG SPACE to deal with multiplying small decimals
            double ptotal = Math.log(pLabel);
            for (int index = 0; index < pItems.length; index++) {
                pItems [index] = itemProbability(data.item(index), x);
                ptotal += Math.log(pItems[index]);
            }
            res.put(x, ptotal);
        }
        return res;
    }
    
    protected double itemProbability (E item, Object lbl) {
        int row = items.indexOf(item);
        int col = clss.indexOf(lbl);
        double popclss = 0.0;
        for (int r = 0; r < matOccurrences.getRowDimension(); r++) {
            popclss += matOccurrences.getEntry(r, col);
        }
        double popitem = matOccurrences.getEntry(row, col);
        return popitem / popclss;
    }

    class NBItem extends HashMap<Object, Double> {
        E feature;
        double totalOccurrences = 0.0;
        
        public NBItem (E f) {
            feature = f;
        }
        @Override
        public boolean equals (Object o) {
            NBItem tmp = (NBItem) o;
            return o != null &&
                   tmp.feature.equals(feature);
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 71 * hash + Objects.hashCode(this.feature);
            return hash;
        }
    }
}
