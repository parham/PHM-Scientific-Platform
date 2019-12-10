
package com.phm.core.data;

import com.phm.core.ParametersContainer; 
import com.phm.core.data.Features;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author phm
 */
public class IDFeatures extends Features {
    
    public static final String PARAM_ID = "nodefeatures.id";
    public static final String ENTITY_LABEL = "entity.label";
    
    private static AtomicInteger IDGenerator = new AtomicInteger(0);
    
    private int idObj;
    public final ParametersContainer parameters = new ParametersContainer ();
    
    public IDFeatures () {
        super ();
        idObj = IDGenerator.incrementAndGet();
    }
    public IDFeatures (double [] ft) {
        super (ft);
        idObj = IDGenerator.incrementAndGet();
    }
    public IDFeatures (int ndim) {
        super (ndim);
        idObj = IDGenerator.incrementAndGet();
    }
    public IDFeatures (RealVector v) {
        super (v);
        idObj = IDGenerator.incrementAndGet();
    }
    public IDFeatures (long time, Object lbl) {
        super (time, lbl);
        setLabel(lbl);
        idObj = IDGenerator.incrementAndGet();
    }
    public IDFeatures (long time, Object lbl, double [] ft) {
        super (time, lbl, ft);
        setLabel(lbl);
        idObj = IDGenerator.incrementAndGet();
    }
    public IDFeatures (long time, Object lbl, int ndim) {
        super (time, lbl, ndim);
        setLabel(lbl);
        idObj = IDGenerator.incrementAndGet();
    }
    public IDFeatures (long time, Object lbl, RealVector v) {
        super (time, lbl, v);
        setLabel(lbl);
        idObj = IDGenerator.incrementAndGet();
    }
    public IDFeatures (int tid, long time, Object lbl, double [] ft) {
        super (time, lbl, ft);
        setLabel(lbl);
        idObj = tid;
    }
    public IDFeatures (int tid, long time, Object lbl, int ndim) {
        super (time, lbl, ndim);
        setLabel(lbl);
        idObj = tid;
    }
    public IDFeatures (int tid, long time, Object lbl, RealVector v) {
        super (time, lbl, v);
        setLabel(lbl);
        idObj = tid;
    }
    
    public int getID () {
        return idObj;
    }
    
//    public Object setParameter (String key, Object value) {
//        return parameters.put(key, value);
//    }
//    public void setParameters (Map<? extends String, ? extends Object> mp) {
//        parameters.putAll(mp);
//    }
//    public Object getParameter (String key) {
//        return parameters.get(key);
//    }
//    public ParametersContainer getParameters () {
//        return parameters;
//    }
//    public boolean containsParameter (String key) {
//        return parameters.containsKey(key);
//    }
//    public Object removeParameter (String key) {
//        return parameters.remove(key);
//    }
//    public void clearParameters () {
//        parameters.clear();
//    }
//    public boolean processOnParameter (String key, ProcessOnParameter process) {
//        return parameters.processOnParameter(key, process);
//    }
    @Override
    public boolean equals (Object obj) {
        return (obj != null && 
                obj instanceof IDFeatures &&
                obj.hashCode() == hashCode());
    }
    @Override
    public int hashCode() {
        return this.idObj;
    }
    @Override
    public IDFeatures copy () {
        return new IDFeatures (getID(), getTime(), getLabel(), super.copy());
    }
    @Override
    public IDFeatures clone () {
        return copy ();
    }
    @Override
    public String toString () {
        return String.valueOf(idObj) + "\t" + super.toString();
    }
    @Override
    public Object getLabel () {
        return this.parameters.get(ENTITY_LABEL);
    }
    @Override
    public void setLabel (Object lbl) {
        super.setLabel(lbl);
        parameters.put(ENTITY_LABEL, lbl);
    }
    @Override
    public boolean code (Map<String, Object> map) {
        if (super.code(map)) {
            map.put(PARAM_ID, getID());
            return true;
        }
        
        return false;
    }
    @Override
    public boolean decode(Map<String, Object> map) {
        if (super.decode(map)) {
            Object id = map.get(PARAM_ID);
            idObj = Integer.valueOf((String) id);
            return true;
        }
        return false;
    }
    @Override
    public boolean code(List<Object> list) {
        if (super.code (list)) {
            list.add(getID());
            return true;
        }
        
        return false;
    }
    @Override
    public boolean decode(List<Object> list) {
        if (super.decode(list)) {
            Object id = list.get(2);
            idObj = Integer.valueOf((String) id);            
            return true;
        }
        
        return false;
    }
}
