
package com.phm.core.data.converter;

import com.google.common.collect.EvictingQueue;
import com.phm.core.data.Instance;
import com.phm.core.data.InstanceList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author phm
 * @param <DataType>
 */
public class InstanceToListConverter<DataType extends Instance> 
        implements InstanceConverter<DataType, InstanceList<DataType>> {

    public static final int DEFAULT_BUFFER_SIZE = 30;
    
    protected int packedSize = DEFAULT_BUFFER_SIZE;
    protected final EvictingQueue<DataType> buffer;
    protected boolean cleanAfterPacked = false;
    
    public InstanceToListConverter (int ps, boolean clean) {
        setPackedSize(ps);
        buffer = EvictingQueue.create(packedSize);
        cleanAfterPacked = clean;
    }
    public InstanceToListConverter (int ps) {
        this (ps, false);
    }
    
    public void setPackedSize (int ps) {
        packedSize = ps;
    }
    public int getPackedSize () {
        return packedSize;
    }
    public void setCleanAfterPacked (boolean clean) {
        cleanAfterPacked = clean;
    }
    public boolean getCleanAfterPacked () {
        return cleanAfterPacked;
    }
    
    public List<DataType> getBufferedData () {
        return new LinkedList<>(buffer);
    }

    @Override
    public InstanceList<DataType> apply (DataType data) {
        synchronized (buffer) {
            buffer.add (data);
            InstanceList ent = new InstanceList(data.getTime(), data.getLabel());
            if (buffer.size() >= packedSize) {
                ent.addAll (buffer);
                if (cleanAfterPacked) {
                    buffer.clear();
                }
                return ent;
            }
            return null;
        }
    }
}
