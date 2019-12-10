
package com.phm.biometry.heartrate.v3;

import com.phm.biometry.heartrate.HeartrateInfo;
import com.phm.core.data.Features;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author phm
 */
public class v3HeartrateInfo extends HeartrateInfo {
    
    public static final String PARAM_DATA = "heartinfo.data";
    public static final String PARAM_INTERPOLATE = "heartinfo.interpolate";
    public static final String PARAM_NORMALIZE = "heartinfo.normalize";
    public static final String PARAM_ICA = "heartinfo.ica";
    public static final String PARAM_DETRENDING = "heartinfo.detrending";
    public static final String PARAM_NOISEFILTER = "heartinfo.noiseFilter";
    public static final String PARAM_SMOOTH = "heartinfo.smooth";
    public static final String PARAM_WELCH = "heartinfo.welch";
    
    public List<Features> data = null;
    public List<RealVector> interpolate = null;
    public List<RealVector> normalize = null;
    public List<RealVector> ica = null;
    public List<RealVector> detrending = null;
    public List<RealVector> noiseFilter = null;
    public List<RealVector> smooth = null;
    public List<RealVector> welch = null;
    
    @Override
    public boolean code (Map<String, Object> map) {
        super.code (map);
        map.put(PARAM_DATA, data);
        map.put(PARAM_INTERPOLATE, interpolate);
        map.put(PARAM_NORMALIZE, normalize);
        map.put(PARAM_ICA, ica);
        map.put(PARAM_DETRENDING, detrending);
        map.put(PARAM_NOISEFILTER, noiseFilter);
        map.put(PARAM_SMOOTH, smooth);
        map.put(PARAM_WELCH, welch);
        return true;
    }

    @Override
    public boolean code (List<Object> list) {
        super.code(list);
        list.add(data);
        list.add(interpolate);
        list.add(normalize);
        list.add(ica);
        list.add(detrending);
        list.add(noiseFilter);
        list.add(smooth);
        list.add(welch);
        return true;
    }

    @Override
    public boolean decode (Map<String, Object> map) {
        super.decode(map);
        data = (List<Features>) map.get(PARAM_DATA);
        interpolate = (List<RealVector>) map.get(PARAM_INTERPOLATE);
        normalize = (List<RealVector>) map.get(PARAM_NORMALIZE);
        ica = (List<RealVector>) map.get(PARAM_ICA);
        detrending = (List<RealVector>) map.get(PARAM_DETRENDING);
        noiseFilter = (List<RealVector>) map.get(PARAM_NOISEFILTER);
        smooth = (List<RealVector>) map.get(PARAM_SMOOTH);
        welch = (List<RealVector>) map.get(PARAM_WELCH);
        return true;
    }

    @Override
    public boolean decode (List<Object> list) {
        super.decode(list);
        int num = list.size();
        data = (List<Features>) list.get(num);
        interpolate = (List<RealVector>) list.get(num + 1);
        normalize = (List<RealVector>) list.get(num + 2);
        ica = (List<RealVector>) list.get(num + 3);
        detrending = (List<RealVector>) list.get(num + 4);
        noiseFilter = (List<RealVector>) list.get(num + 5);
        smooth = (List<RealVector>) list.get(num + 6);
        welch = (List<RealVector>) list.get(num + 7);
        return true;
    }
}