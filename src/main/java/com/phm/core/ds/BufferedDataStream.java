
package com.phm.core.ds;

import com.phm.core.terminal.Source;
import com.phm.core.data.Instance;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

/**
 *
 * @author phm
 * @param <DataType>
 */
public class BufferedDataStream<DataType extends Instance> 
    extends ExtendableDataStream<DataType> 
    implements Queue<DataType> {

    protected Queue<DataType> buffer;
    
    public BufferedDataStream (String sn, Source src, Queue<DataType> buf) {
        super(sn, src);
        buffer = buf;
    }

    public Queue<DataType> getData () {
        return buffer;
    }
    @Override
    public boolean add (DataType dt) {
        return buffer.add(dt);
    }
    @Override
    public boolean addAll(Collection<? extends DataType> ds) {
        return buffer.addAll(ds);
    }
    @Override
    public DataType next() {
        return buffer.poll();
    }
    @Override
    public boolean hasMore() {
        return buffer.size() > 0;
    }
    @Override
    public boolean offer(DataType e) {
        return buffer.offer(e);
    }
    @Override
    public DataType remove() {
        return buffer.remove();
    }
    @Override
    public DataType poll() {
        return buffer.poll();
    }
    @Override
    public DataType element() {
        return buffer.element();
    }
    @Override
    public DataType peek() {
        return buffer.peek();
    }
    @Override
    public int size() {
        return buffer.size();
    }
    @Override
    public boolean isEmpty() {
        return buffer.isEmpty();
    }
    @Override
    public boolean contains(Object o) {
        return buffer.contains(o);
    }
    @Override
    public Iterator<DataType> iterator() {
        return buffer.iterator();
    }
    @Override
    public Object[] toArray() {
        return buffer.toArray();
    }
    @Override
    public <T> T[] toArray(T[] a) {
        return buffer.toArray(a);
    }
    @Override
    public boolean remove(Object o) {
        return buffer.remove(o);
    }
    @Override
    public boolean containsAll(Collection<?> c) {
        return buffer.containsAll(c);
    }
    @Override
    public boolean removeAll(Collection<?> c) {
        return buffer.removeAll(c);
    }
    @Override
    public boolean retainAll(Collection<?> c) {
        return buffer.retainAll(c);
    }
    @Override
    public void clear() {
        buffer.clear();
    }
}
