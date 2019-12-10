
package com.phm.biometry.heartrate.v2;

import com.phm.biometry.heartrate.HeartrateInfo;
import com.phm.core.data.Features;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author phm
 */
public class v2HeartrateInfo extends HeartrateInfo {
    
    public static final String PARAM_DATA = "heartinfo.data";
    public static final String PARAM_INTERPOLATE = "heartinfo.interpolate";
    public static final String PARAM_NORMALIZE = "heartinfo.normalize";
    public static final String PARAM_ICA = "heartinfo.ica";
    public static final String PARAM_FFT = "heartinfo.fft";
    public static final String PARAM_PEAKPOINT = "heartinfo.peakpoint";
    public static final String PARAM_CONVERT = "heartinfo.convert";
    public static final String PARAM_CALCULATE = "heartinfo.calculate";
    public static final String PARAM_FINALPROCESS = "heartinfo.finalprocess";
    
    public List<Features> data = null;
    public List<Features> interpolate = null;
    public List<Features> normalize = null;
    public List<RealVector> ica = null;
    public List<RealVector> fft = null;
    public List<RealVector> peakpoints = null;
    public List<RealVector> convert = null;
    public List<RealVector> calculate = null;
    public List<RealVector> finalprocess = null;
    
    @Override
    public boolean code (Map<String, Object> map) {
        super.code (map);
        map.put(PARAM_DATA, data);
        map.put(PARAM_INTERPOLATE, interpolate);
        map.put(PARAM_NORMALIZE, normalize);
        map.put(PARAM_ICA, ica);
        map.put(PARAM_FFT, fft);
        map.put(PARAM_PEAKPOINT, peakpoints);
        map.put(PARAM_CONVERT, convert);
        map.put(PARAM_CALCULATE, calculate);
        map.put(PARAM_FINALPROCESS, finalprocess);
        return true;
    }

    @Override
    public boolean code (List<Object> list) {
        super.code(list);
        list.add(data);
        list.add(interpolate);
        list.add(normalize);
        list.add(ica);
        list.add(fft);
        list.add(peakpoints);
        list.add(convert);
        list.add(calculate);
        list.add(finalprocess);
        return true;
    }

    @Override
    public boolean decode (Map<String, Object> map) {
        super.decode(map);
        data = (List<Features>) map.get(PARAM_DATA);
        interpolate = (List<Features>) map.get(PARAM_INTERPOLATE);
        normalize = (List<Features>) map.get(PARAM_NORMALIZE);
        ica = (List<RealVector>) map.get(PARAM_ICA);
        fft = (List<RealVector>) map.get(PARAM_FFT);
        peakpoints = (List<RealVector>) map.get(PARAM_PEAKPOINT);
        convert = (List<RealVector>) map.get(PARAM_CONVERT);
        calculate = (List<RealVector>) map.get(PARAM_CALCULATE);
        finalprocess = (List<RealVector>) map.get(PARAM_FINALPROCESS);
        return true;
    }

    @Override
    public boolean decode (List<Object> list) {
        super.decode(list);
        int num = list.size();
        data = (List<Features>) list.get(num);
        interpolate = (List<Features>) list.get(num + 1);
        normalize = (List<Features>) list.get(num + 2);
        ica = (List<RealVector>) list.get(num + 3);
        fft = (List<RealVector>) list.get(num + 4);
        peakpoints = (List<RealVector>) list.get(num + 5);
        convert = (List<RealVector>) list.get(num + 6);
        calculate = (List<RealVector>) list.get(num + 7);
        finalprocess = (List<RealVector>) list.get(num + 8);
        return true;
    }
}
