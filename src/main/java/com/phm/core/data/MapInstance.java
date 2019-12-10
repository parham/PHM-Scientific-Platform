
package com.phm.core.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author phm
 */
public class MapInstance extends HashMap<String, Object> implements Instance, Cloneable {

    private long timeObj = System.currentTimeMillis();
    private Object labelObj = null;
    
    public MapInstance () {
        // Empty body
    }
    public MapInstance (long time, Object lbl) {
        setTime(time);
        setLabel(lbl);
    }
    public MapInstance (long time, Object lbl, Map<? extends String, ? extends Object> map) {
        this (time, lbl);
        this.putAll(map);
    }
    public MapInstance (Map<? extends String, ? extends Object> map) {
        this.putAll(map);
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
        return new MapInstance(getTime(), getLabel(), this);
    }

    @Override
    public boolean code(Map<String, Object> map) {
        map.put(PARAM_TIME, getTime());
        map.put(PARAM_LABEL, getLabel());
        map.putAll(this);
        return true;
    }

    @Override
    public boolean code(List<Object> list) {
        list.add(getTime());
        list.add(getLabel());
        list.addAll(new TreeMap<String, Object>(this).values());
        return true;
    }

    @Override
    public boolean decode(Map<String, Object> map) {
        setTime((long) map.get(PARAM_TIME));
        setLabel(map.get(PARAM_LABEL));
        HashMap<String, Object> tmp = new HashMap<>(map);
        tmp.remove(PARAM_TIME);
        tmp.remove(PARAM_LABEL);
        this.putAll(tmp);
        return true;
    }

    @Override
    public boolean decode(List<Object> map) {
        return false;
    }
    
}
