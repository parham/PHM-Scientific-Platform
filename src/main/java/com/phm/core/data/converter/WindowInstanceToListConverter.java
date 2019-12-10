
package com.phm.core.data.converter;

import com.phm.core.data.Instance;
import com.phm.core.data.InstanceList;

/**
 *
 * @author phm
 * @param <DataType>
 */
public class WindowInstanceToListConverter<DataType extends Instance>
       extends InstanceToListConverter<DataType> {

    protected int moveSize = 1;
    
    public WindowInstanceToListConverter(int ps, float move) {
        super(ps, false);
        setSegmentSize(move);
    }
    
    public boolean setSegmentSize (float move) {
        int tmp = (int) (getPackedSize() * move);
        if (tmp != 0 && tmp < getPackedSize()) {
            moveSize = tmp;
            return true;
        }
        return false;
    }
    
    public int getSegmentSize () {
        return moveSize;
    }
    
    @Override
    public InstanceList<DataType> apply (DataType data) {
        synchronized (buffer) {
            buffer.add (data);
            InstanceList ent = new InstanceList(data.getTime(), data.getLabel());
            if (buffer.size() >= packedSize) {
                ent.addAll (buffer);
                for (int index = 0; index < moveSize; index++) {
                    buffer.remove();
                }
                return ent;
            }
            return null;
        }
    }
}
