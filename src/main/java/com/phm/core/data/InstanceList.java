
package com.phm.core.data;

import com.phm.annotations.ImplementationDetails;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

@ImplementationDetails (
    className = "InstanceList",
    date = "10/20/2015",
    lastModification = "10/20/2015",
    version = "1.0.0",
    description = "entity list ..."
)
public class InstanceList<DataType extends Instance> implements List<DataType>, Instance {
    
    protected LinkedList<DataType> buffer = new LinkedList<>();
    private long timeObj = 0;
    private Object labelObj;
    
    public InstanceList () {
        // Empty body
    }
    public InstanceList (Collection<? extends DataType> cs) {
        buffer.addAll(cs);
    }
    public InstanceList (long time, Object lbl, Collection<? extends DataType> cs) {
        this (time, lbl);
        buffer.addAll(cs);
    }
    public InstanceList (long time, Object lbl) {
        timeObj = time;
        labelObj = lbl;
    }
    public InstanceList (long time) {
        timeObj = time;
    }
    public InstanceList (Object lbl) {
        labelObj = lbl;
    }
    
    public void setData (Collection<? extends DataType> cs) {
        buffer.clear();
        buffer.addAll(buffer);
    }
    public List<DataType> getData () {
        return buffer;
    }

    @Override
    public Instance copy() {
        InstanceList ent = new InstanceList (getTime(), getLabel());
        this.stream().forEach((x) -> {
            ent.add(x.copy());
        });
        return ent;
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
        return buffer.contains (o);
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
    public boolean add(DataType e) {
        return buffer.add (e);
    }

    @Override
    public boolean remove(Object o) {
        return buffer.remove (o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return buffer.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends DataType> c) {
        return buffer.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends DataType> c) {
        return buffer.addAll(index, c);
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
        buffer.clear ();
    }

    @Override
    public DataType get(int index) {
        return buffer.get(index);
    }

    @Override
    public DataType set(int index, DataType element) {
        return buffer.set(index, element);
    }

    @Override
    public void add(int index, DataType element) {
        buffer.add(index, element);
    }

    @Override
    public DataType remove (int index) {
        return buffer.remove (index);
    }

    @Override
    public int indexOf(Object o) {
        return buffer.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return buffer.lastIndexOf(o);
    }

    @Override
    public ListIterator<DataType> listIterator() {
        return buffer.listIterator();
    }

    @Override
    public ListIterator<DataType> listIterator(int index) {
        return buffer.listIterator(index);
    }

    @Override
    public List<DataType> subList(int fromIndex, int toIndex) {
        return buffer.subList(fromIndex, toIndex);
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
    public boolean code(Map<String, Object> map) {
        for (Instance x : buffer) {
            x.code(map);
        }
        return true;
    }

    @Override
    public boolean decode(Map<String, Object> map) {
        for (Instance x : buffer) {
            x.decode(map);
        }
        return true;        
    }

    @Override
    public boolean code(List<Object> list) {
        return false;
    }

    @Override
    public boolean decode(List<Object> map) {
        return false;
    }
}
