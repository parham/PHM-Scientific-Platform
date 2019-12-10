
package com.phm.comp.clusterer.graph;
 
import com.phm.annotations.ImplementationDetails;
import com.phm.annotations.PublicationDetails;
import com.phm.annotations.PublicationType;
import com.phm.comp.distance.ApacheWrapperDistanceMeasure;
import com.phm.core.ArraySet;
import com.phm.core.ParametersContainer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList; 
import java.util.Map;
import java.util.Set; 
import org.apache.commons.math3.linear.RealVector;
import org.jgraph.graph.Edge;
import org.jgrapht.EdgeFactory;
import org.jgrapht.Graph;

 
@PublicationDetails (
        author = {"Y. Xia", "X. Peng"},
        type = PublicationType.Proceedings,
        title =  "A clustering algorithm based on Delaunay Triangulation",
        journal = "7th World Congress on Intelligent Control and Automation",
        year = "2008",
        pages = "4517 – 4521",
        customData = {"10.1109/WCICA.2008.4593651"},
        paperAbstract = "Most clustering methods require user-specified parameters or prior knowledge to produce their best results, this demands pre-processing or several trials. Both are extremely expensive and inefficient, because the best-fit parameters are not easy to get. This paper presents a new approach (CBDTM) which is on the basis of Delaunay Triangulation. This approach introduces the median length of k-nearest edges as measure to divide edges for each point. The parameters of CBDTM are not specified by users, and the experiment shows to us that it can find different shape clusters not only in different density data sets, but also in data sets with noise. All operations complete within expected time O(nlogn) , where n is the number of the data sets. The performance comparison experiments show to us, CBDTM more efficient and it has better quality than AUTOCLUST."
)
@ImplementationDetails (
        className = "Xia2008Clusterer", 
        date = "2015/11/09",
        lastModification = "2015/11/09",
        version = "1.0.0"
)
public class Xia2008Clusterer<V extends RealVector, E extends Edge>  
    extends EnhancedConnectivityBasedGraphClusterer<V, E>  {
    
    protected int numNeuronCount = 1;
    
    public Xia2008Clusterer (EdgeFactory<V, E> ef) {
        super (ef);
    }
    public Xia2008Clusterer (EdgeFactory<V, E> ef, int numn) {
        this (ef);
        numNeuronCount = numn;
    }
    
    protected HashMap<E, Double> initialize (Graph<V,E> g) {
        Set<E> edges = g.edgeSet();
        ApacheWrapperDistanceMeasure ed = new ApacheWrapperDistanceMeasure ();
        HashMap<E, Double> hm = new HashMap<> ();
        for (E e : edges) {
            V s = (V) e.getSource();
            V t = (V) e.getTarget();
            double v = Math.abs (ed.measure(s, t, new ParametersContainer()).distance);
            hm.put (e, v);
        }
        return hm;
    }
    protected double medianEdges (Graph<V,E> g, V n, HashMap<E, Double> dis) {
        LinkedList<E> es = new LinkedList<>(g.edgesOf(n));
        double [] vs = new double [es.size()];
        for (int dim = 0; dim < vs.length; dim++) {
            vs [dim] = dis.get(es.get(dim));
        }
        Arrays.sort (vs);
        int m = vs.length / 2;
        if (vs.length == 0) {
            return -1;
        } else if (vs.length == 1) {
            return vs [0];
        } else if (vs.length % 2 == 0) {
            return vs [m];
        }
        return (vs [m] + vs [m + 1]) / 2;
    }
    protected HashMap<V, Double> medianForAllVerteces (Graph<V,E> g, HashMap<E, Double> dis) {
        HashMap<V, Double> med = new HashMap<>();
        Set<V> vs = g.vertexSet();
        vs.stream().forEach((v) -> {
            double d = medianEdges(g, v, dis);
            med.put(v, d);
        });
        return med;
    }
    protected double localStdEdges (Graph<V,E> g, V n, HashMap<E, Double> dis, double meds) {
        LinkedList<E> es = new LinkedList<>(g.edgesOf(n));
        double res = 0;
        for (E e : es) {
            double ve = dis.get(e);
            res += (meds - ve) * (meds - ve);
        }
        res = Math.sqrt(res) / es.size();
        return res;
    }
    protected HashMap<V, Double> localStdForAllVerteces (Graph<V,E> g, HashMap<E, Double> dis, HashMap<V, Double> meds) {
        HashMap<V, Double> stds = new HashMap<>();
        Set<V> vs = g.vertexSet();
        vs.stream().forEach((v) -> {
            double m = meds.get(v);
            double stdv = localStdEdges(g, v, dis, m);
            stds.put(v, stdv);
        });
        return stds;
    }
    protected double localMeanEdges (Graph<V,E> g, V n,
                              HashMap<E, Double> dis) {
        LinkedList<E> es = new LinkedList<>(g.edgesOf(n));
        double res = 0;
        for (E e : es) {
            res += dis.get(e);
        }
        res /= es.size();
        return res;
    }
    protected HashMap<V, Double> localMeanForAllVerteces (Graph<V,E> g,
                                                        HashMap<E, Double> dis) {
        HashMap<V, Double> means = new HashMap<>();
        Set<V> vs = g.vertexSet();
        for (V v : vs) {
            double vt = localMeanEdges(g, v, dis);
            means.put(v, vt);
        }
        return means;
    }
    protected HashMap<E, Boolean> determineEdgesType (Graph<V,E> g, V n,
                                                      HashMap<E, Double> dis,
                                                      HashMap<V, Double> meds,
                                                      HashMap<V, Double> stds) {
        HashMap<E, Boolean> types = new HashMap<>();
        ArraySet<E> edges = new ArraySet<>(g.edgesOf(n));
        if (edges.size() == 1) {
            E e = edges.get(0);
            types.put(e, false);
        } else {
            for (E e : edges) {
                double v = meds.get(n) + stds.get(n);
                double ev = dis.get(e);
                types.put(e, ev > v);
            }
        }
        return types;
    }
    protected HashMap<V, HashMap<E, Boolean>> determinesAllVerteces (Graph<V,E> g,
                                                      HashMap<E, Double> dis,
                                                      HashMap<V, Double> meds,
                                                      HashMap<V, Double> stds) {
        HashMap<V, HashMap<E, Boolean>> dav = new HashMap<>();
        Set<V> vers = g.vertexSet();
        for (V v : vers) {
            dav.put(v, determineEdgesType(g, v, dis, meds, stds));
        }
        return dav;
    }
    
    @Override
    public boolean enhance (Graph<V, E> g) {
        if (g.vertexSet().size() > 3 && g.edgeSet().size() > 0) {
            HashMap<E, Double> distances = initialize (g);
            HashMap<V, Double> medians = medianForAllVerteces(g, distances);
            HashMap<V, Double> stds = localStdForAllVerteces(g, distances, medians);
            HashMap<V, Double> means = localMeanForAllVerteces(g, distances);
            HashMap<V, HashMap<E, Boolean>> davs = determinesAllVerteces(g, distances, means, stds);
            // eliminate
            Set<V> verts = g.vertexSet();
            for (V v : verts) {
                HashMap<E, Boolean> dav = davs.get(v);
                Set<E> eds = dav.keySet();
                for (E e : eds) {
                    boolean islong = dav.get(e);
                    if (islong) {
                        g.removeEdge(e);
                    }
                }
            }
//            normalizeGraph(g);        
        }
        return true;
    }

    @Override
    public boolean normalize (Map<String, Graph<V,E>> gs) {
        // Remove noisy clusters which number of their neurons are lower
        // than predefined threshold.
        LinkedList<String> keys = new LinkedList<>(gs.keySet());
        for (String x : keys) {
            Graph<V,E> g = gs.get(x);
            if (g.vertexSet().size() > numNeuronCount) {
                gs.remove (x);
            }
        }
        return true;
    }
}
