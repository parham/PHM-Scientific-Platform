
package com.phm.core.data;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author phm
 */
public class VectorInstance<E> extends Vector<E> implements VectorLikeInstance<E> {
    
    public static final String PARAM_VECTORSIZE = "param.vectorsize";
    
    private long timeObj = System.currentTimeMillis();
    private Object labelObj;
    
    public VectorInstance (long time, Object lbl) {
        setTime(time);
        setLabel(lbl);
    }
    public VectorInstance (long time, Object lbl, Collection<? extends E> cs) {
        super (cs);
        setTime(time);
        setLabel(lbl);
    }
    public VectorInstance (long time, Object lbl, int sz) {
        super (sz);
        setTime(time);
        setLabel(lbl);
    }
    public VectorInstance (Collection<? extends E> cs) {
        super (cs);
    }
    public VectorInstance (int sz) {
        super (sz);
    }

    @Override
    public void setTime(long time) {
        timeObj = time;
    }

    @Override
    public long getTime() {
        return timeObj;
    }

    @Override
    public void setLabel(Object lbl) {
        labelObj = lbl;
    }

    @Override
    public Object getLabel() {
        return labelObj;
    }

    @Override
    public Instance copy() {
        return new VectorInstance(timeObj, labelObj, this);
    }

    @Override
    public boolean code(Map<String, Object> map) {
        map.put(PARAM_TIME, getTime());
        map.put(PARAM_LABEL, getLabel());
        map.put(PARAM_VECTORSIZE, this.size());
        for (int index = 0; index < this.size(); index++) {
            map.put(String.valueOf(index), get(index));
        }
        return true;
    }

    @Override
    public boolean code(List<Object> list) {
        list.add(getTime());
        list.add(getLabel());
        list.add(size());
        list.addAll(this);
        return true;
    }

    @Override
    public boolean decode(Map<String, Object> map) {
        setTime((long) map.get(PARAM_TIME));
        setLabel(map.get(PARAM_LABEL));
        int siz = (int) map.get(PARAM_VECTORSIZE);
        for (int index = 0; index < siz; index++) {
            add ((E) map.get(String.valueOf(index)));
        }
        return true;
    }

    @Override
    public boolean decode(List<Object> list) {
        LinkedList<Object> tmp = new LinkedList<>(list);
        setTime ((long) tmp.removeFirst());
        setLabel (tmp.removeFirst());
        tmp.removeFirst();
        tmp.stream().forEach((x) -> {
            this.add((E) x);
        });
        return true;
    }

    @Override
    public E item(int index) {
        return get(index);
    }

    @Override
    public List<E> items() {
        return new LinkedList<>(this);
    }
    
}
