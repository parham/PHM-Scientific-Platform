
package com.phm.comp.neuralnet.art.bayesian;
 
import com.phm.comp.filter.BlankFilter;
import com.phm.comp.neuralnet.NNResult;
import com.phm.comp.neuralnet.Neuron;
import com.phm.comp.neuralnet.NeuronGroup;
import com.phm.comp.neuralnet.art.ARTTrainingSupervisor;
import com.phm.comp.neuralnet.event.NeuronAddedEvent;
import com.phm.core.data.Features;
import java.util.LinkedList;
import java.util.List; 
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * <p>
 * <b>Publication details:<br></b>
 * <b>Authors:</b> Boaz Vigdor and Boaz Lerner <br>
 * <b>Year:</b> 2007 <br>
 * <b>Title:</b> The Bayesian ARTMAP <br>
 * <b>Published In:</b> IEEE Transactions on Neural Networks 18(6) <br>
 * <b>Page:</b> 1628 - 1644 <br>
 * <b>DOI:</b> 10.1109/TNN.2007.900234 <br>
 * </p>
 * @author Parham Nooralishahi - PHM!
 * @email parham.nooralishahi@gmail.com
 */
public class BayesianARTTrainingSupervisor extends ARTTrainingSupervisor {

    public static final String BAYESIANART_MAX_HYPERVOLUME = "bayesianart.max.hypervolume";
    public static final String BAYESIANART_VARIANCE_CONST_INITIALIZER = "bayesianart.variance.const.initializer";
    public static final String BAYESIANART_P_WJ_X = "bayesianart.p.wj.x";
    
    public static final String NEURON_VARIANCE = "neuron.variance";
    public static final String NEURON_P_X_WJ = "neuron.p.x.wj";
    public static final String NEURON_P_WJ = "neuron.p.wj";
    public static final String NEURON_CODEDSIGNAL = "neuron.coded.signals";
    
    protected final int netDimension;
    
    public BayesianARTTrainingSupervisor (int dim) {
        netDimension = dim;
    }
    
    @Override
    public String getName() {
        return "bayesianart.train";
    }

    @Override
    public void initialize(Neuron ngroup) {
        ngroup.parameters.put(BAYESIANART_MAX_HYPERVOLUME, 1.0);
        ngroup.parameters.put(BAYESIANART_VARIANCE_CONST_INITIALIZER, 0.001);
        ngroup.setInputStrategy(new BlankFilter ());
    }

    protected Neuron initializeBayesianARTNeuron (NeuronGroup ngroup, Neuron n) {
        n.parameters.put(NEURON_VARIANCE, new Array2DRowRealMatrix (netDimension, netDimension));
        n.parameters.put(NEURON_P_WJ, 0.0);
        n.parameters.put(NEURON_P_X_WJ, 0.0);
        n.parameters.put(NEURON_CODEDSIGNAL, 1.0);
        n.parameters.put(NEURON_ACTIVATION_VALUE, 0.0);
        n.parameters.put(NEURON_MATCHVALUE, 0.0);
        return n;
    }
    
    @Override
    protected void beforeCalculateActivationValues (NeuronGroup ngroup, Features signal) {
        LinkedList<Neuron> ns = new LinkedList<>(ngroup.neurons);
        float p_tot = 0.0f;
        for (Neuron x : ns) {
            float p_x_wj = P_x_wj(ngroup, x, signal);
            float p_wj = P_wj(ngroup, x);
            x.parameters.put(NEURON_P_X_WJ, p_x_wj);
            x.parameters.put(NEURON_P_WJ, p_wj);
            p_tot += (p_x_wj * p_wj);
        }
        ngroup.parameters.put(BAYESIANART_P_WJ_X, (float) p_tot);
    }
    
    @Override
    protected boolean doesResonanceOccured (NeuronGroup ngroup, 
                                            List<Neuron> neuron, 
                                            Features signal) {
        float sMAX = (Float) ngroup.parameters.get(BAYESIANART_MAX_HYPERVOLUME);
        double sj = match (ngroup, neuron.get(0), signal);
        boolean res = sj < sMAX;
        ngroup.parameters.put(ART_RESONANCE_OCCURED, res);
        return res;
    }
    
    @Override
    protected Neuron insert (NeuronGroup ngroup, 
                             Features signal) {
        // Initialize new neuron
        // mu
        // variance
        float sMAX = (float) ngroup.parameters.get(BAYESIANART_MAX_HYPERVOLUME);
        float lambda = (float) ngroup.parameters.get(BAYESIANART_VARIANCE_CONST_INITIALIZER);
        int M = signal.getDimension ();
        RealMatrix variance = MatrixUtils.createRealIdentityMatrix (M);
        double mfactor = lambda * Math.pow(sMAX, (1 / (double) M));
        variance = variance.scalarMultiply (mfactor);
        /////////////////////////
        Neuron newNeuron = initializeBayesianARTNeuron (ngroup, new Neuron (signal));
        newNeuron.parameters.put(NEURON_VARIANCE, variance);
        
        ngroup.addInternalNeuron(newNeuron);
        ngroup.eventBus.post(new NeuronAddedEvent(newNeuron));
        return newNeuron;
    }

    @Override
    protected NNResult prepareResult(Neuron neuron, Features signal, List<Neuron> winners, NNResult resc) {
        return new NNResult(System.currentTimeMillis(), neuron.parameters, winners);
    }
    
    protected float P_x_wj (NeuronGroup parent, Neuron n, Features signal) {
        final int dim = signal.getDimension();
        final RealMatrix var = (RealMatrix) n.parameters.get(NEURON_VARIANCE);
        final LUDecomposition lud = new LUDecomposition(var);
        //debuging
        double vdet = lud.getDeterminant();
//        double vdet = var.det();
        double tmpv1 = Math.sqrt(vdet);
        double tmpv2 = (tmpv1 * Math.pow(Math.sqrt(2 * Math.PI), dim));
        double part1 = 1.0f / tmpv2;
        /////////////////
        //float [] centroid = (float []) n.centroid.cdata(0);
        double [] dis = new double[n.getDimension()];
        for (int index = 0; index < dis.length; index++) {
            dis [index] = signal.getEntry(index) - n.getEntry(index);
        }
        RealMatrix dism = new Array2DRowRealMatrix (dis);
        RealMatrix invm = MatrixUtils.inverse(var);
        invm = invm.multiply (dism.transpose());
        RealMatrix tmp = dism.multiply(invm);
        RealMatrix tmp2 = tmp.scalarMultiply(-0.5);
        double part2 = Math.exp(tmp2.getEntry(0, 0));
        float res = (float) (part1 * part2);
        return res;
    }
    protected float P_wj (NeuronGroup parent, Neuron n) {
        float nj = (float) n.parameters.get(NEURON_CODEDSIGNAL);
        int ntot = (int) parent.parameters.get(Neuron.RECIEVED_SIGNALS_NUM);
        float res = nj / ntot;
        return res;
    }
    protected float P_wj_x (NeuronGroup parent, Neuron n, Features signal) {
        float p_x_wj = (float) n.parameters.get(NEURON_P_X_WJ);
        float p_wj = (float) n.parameters.get(NEURON_P_WJ);
        float part1 = p_x_wj * p_wj;
        float part2 = (float) parent.parameters.get(BAYESIANART_P_WJ_X);
        float res = part1 / part2;
        return res;
    }
    protected double calculatePwjbyx (NeuronGroup parent, Neuron n, Features signal) {
        double p_x_wj = P_x_wj(parent, n, signal);
        float p_wj = P_wj(parent, n);
        double part1 = p_x_wj * p_wj;
        LinkedList<Neuron> ns = new LinkedList<>(parent.neurons);
        double part2 = 0;
        for (Neuron nx : ns) {
            double tp_xwj = P_x_wj(parent, nx, signal);
            float tp_wj = P_wj(parent, nx);
            part2 += (tp_wj * tp_xwj);
        }
        double res = part1 / part2;
        return res;
    }

    @Override
    protected double activate(NeuronGroup ngroup, Neuron neuron, Features signal) {
        double tmp = P_wj_x(ngroup, neuron, signal);
        return (float) tmp;
    }

    @Override
    protected double match(NeuronGroup ngroup, Neuron neuron, Features signal) {
        RealMatrix covar = (RealMatrix) neuron.parameters.get(NEURON_VARIANCE);
        double mv = new LUDecomposition(covar).getDeterminant();
        neuron.parameters.put(NEURON_MATCHVALUE, mv);
        return mv;
    }

   public static RealMatrix identity (int m, int n) {
      RealMatrix A = new Array2DRowRealMatrix (m,n);
      for (int row = 0; row < A.getRowDimension(); row++) {
         for (int col = 0; col < A.getColumnDimension(); col++) {
             A.setEntry(row, col, row == col ? 1.0 : 0.0);
         }
      }
      return A;
   }
   public static RealMatrix identity (RealMatrix data) {
      RealMatrix A = new Array2DRowRealMatrix(data.getRowDimension(), data.getColumnDimension());
      for (int row = 0; row < A.getRowDimension(); row++) {
         for (int col = 0; col < A.getColumnDimension(); col++) {
             A.setEntry(row, col, row == col ? data.getEntry(row, col) : 0.0);
         }
      }
      return A;
   }
    
    @Override
    protected void update(NeuronGroup ngroup, List<Neuron> neuron, Features signal) {
        Neuron n = neuron.get(0);
        float nj = (Float) n.parameters.get(NEURON_CODEDSIGNAL);
        RealMatrix var = (RealMatrix) n.parameters.get(NEURON_VARIANCE);
        float ca = 1 / (nj + 1);
        float ca2 = nj * ca;
        final int ndims = signal.getDimension();
        // Update mean vector
//        double [] vx_mu = new double[ndims];
//        double [] disdim = new double[ndims];
        RealVector vx_mu = signal.mapMultiply(ca2).subtract(n.mapMultiply(ca));
        RealVector disdim = signal.subtract(n);
        n.setSubVector(0, vx_mu);
//        n.setValues(vx_mu);
//        for (int dim = 0; dim < ndims; dim++) {
//            vx_mu[dim] = (ca2 * signal.value(dim)) - (ca * n.value(dim));
//            disdim[dim] = signal.value(dim) - n.value(dim);
//            n.put (dim, vx_mu [dim]);
//        }
        // Update variance matrix
        RealMatrix x_mu = new Array2DRowRealMatrix (disdim.toArray ());
        x_mu = x_mu.transpose().multiply(x_mu);
        x_mu = identity(x_mu).scalarMultiply(ca);
//        x_mu = x_mu.arrayTimes(Matrix.identity(x_mu.getRowDimension(), x_mu.getColumnDimension()));
//        x_mu.times(ca);
        RealMatrix dvar = var.scalarMultiply(ca2);
//        Matrix dvar = var.times(ca2);
        //var.plusEquals(var.minus(x_mu.times(x_mu.transpose())).times(ca));
//            x_mu = x_mu.transpose().times(x_mu);
//            x_mu = x_mu.arrayTimes(Matrix.identity(x_mu.getRowDimension(), x_mu.getColumnDimension()));
//            Matrix dvar = var.minus(x_mu).times(ca);
        var = dvar.add(x_mu);
        n.parameters.put(NEURON_VARIANCE, var);
        n.parameters.put(NEURON_CODEDSIGNAL, nj + 1);
    }
}
