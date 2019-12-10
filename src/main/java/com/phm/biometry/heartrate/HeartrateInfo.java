
package com.phm.biometry.heartrate;

import com.phm.core.data.Instance;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author PHM
 */
public class HeartrateInfo implements Instance {
    
    public static final String PARAM_START = "heartinfo.start";
    public static final String PARAM_END = "heartinfo.end";
    public static final String PARAM_HEARTRATE = "heartinfo.hr";
    public static final String PARAM_CONFIDENT = "heartinfo.confident";
    public static final String PARAM_POSSIBLE_HEARTRATE = "heartinfo.possible.hr";
    public static final String PARAM_VALID = "heartinfo.valid";
    
    protected double start = 0;
    protected double end = 0;
    protected double heartrate = 0.0f;
    protected double confidence = 1.0;
    protected ArrayList<Double> possibleHeartrates;
    protected boolean validObj = false;
    protected Object labelObj;
    
    public HeartrateInfo () {
        // Empty Body
    }
    public HeartrateInfo (double s, double e, double hr) {
        this (s, e, hr, new ArrayList<>(), false);
    }
    public HeartrateInfo (double s, double e, double hr, List<Double> phr, boolean valid) {
        start = s;
        end = e;
        heartrate = hr;
        possibleHeartrates = new ArrayList<>(phr);
        validObj = valid;
    }
    
    public void setConfidence (double v) {
        confidence = v;
    }
    public double getConfidence () {
        return confidence;
    }
    public void setStartTime (double st) {
        start = st;
    }
    public double getStartTime () {
        return start;
    }
    public void setEndTime (double et) {
        end = et;
    }
    public double getEndTime () {
        return end;
    }
    public void setHeartrate (double hr) {
        heartrate = hr;
    }
    public double getHeartrate () {
        return heartrate;
    }
    public void setPossibleHeartrate (List<Double> phr) {
        possibleHeartrates = new ArrayList<>(phr);
    }
    public List<Double> getPossibleHeartrate () {
        return possibleHeartrates;
    }
    public void setValid (boolean v) {
        validObj = v;
    }
    public boolean isValid () {
        return validObj;
    }

    @Override
    public String toString () {
        return "[" + start + "," + end  + "] " + " --> " + heartrate;
    }
    @Override
    public HeartrateInfo clone () {
        return new HeartrateInfo(start, end, heartrate, possibleHeartrates, validObj);
    }

    @Override
    public boolean code (Map<String, Object> map) {
        map.put(PARAM_START, start);
        map.put(PARAM_END, end);
        map.put(PARAM_HEARTRATE, heartrate);
        map.put(PARAM_CONFIDENT, confidence);
        map.put(PARAM_POSSIBLE_HEARTRATE, possibleHeartrates);
        map.put(PARAM_VALID, validObj);
        return true;
    }

    @Override
    public boolean code (List<Object> list) {
        list.add((double) start);
        list.add((double) end);
        list.add((double) heartrate);
        list.add((double) confidence);
        list.add(possibleHeartrates);
        list.add(validObj);
        return true;
    }

    @Override
    public boolean decode (Map<String, Object> map) {
        start = (Double) map.get(PARAM_START);
        end = (Double) map.get(PARAM_END);
        heartrate = (Double) map.get(PARAM_HEARTRATE);
        confidence = (Double) map.get(PARAM_CONFIDENT);
        possibleHeartrates = new ArrayList<>((ArrayList<Double>) map.get(PARAM_POSSIBLE_HEARTRATE));
        validObj = (Boolean) map.get(PARAM_VALID);
        return true;
    }

    @Override
    public boolean decode (List<Object> list) {
        start = (Double) list.get(0);
        end = (Double) list.get(1);
        heartrate = (Double) list.get(2);
        confidence = (Double) list.get(3);
        possibleHeartrates = new ArrayList<>((ArrayList<Double>) list.get(4));
        validObj = (Boolean) list.get(5);
        return true;
    }

    @Override
    public void setTime(long time) {
        setStartTime (time);
    }

    @Override
    public long getTime() {
        return (long) getStartTime();
    }

    @Override
    public void setLabel(Object lbl) {
        labelObj = Objects.requireNonNull(lbl);
    }

    @Override
    public Object getLabel() {
        return labelObj;
    }

    @Override
    public Instance copy() {
        return this.clone();
    }
}
