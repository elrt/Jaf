package jaf.lang;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public final class ArrayValue implements Value {
    
    private final List<IntValue> elements;
    
    public ArrayValue(List<IntValue> elements) {
        this.elements = new ArrayList<>(elements);
    }
    
    public ArrayValue(int size) {
        this.elements = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            elements.add(new IntValue(0));
        }
    }
    
    public int size() {
        return elements.size();
    }
    
    public IntValue get(int index) {
        checkBounds(index);
        return elements.get(index);
    }
    
    public void set(int index, IntValue value) {
        checkBounds(index);
        elements.set(index, value);
    }
    
    private void checkBounds(int index) {
        if (index < 0 || index >= elements.size()) {
            throw new JafRuntimeException(
                String.format("Array index out of bounds: %d (size: %d)", 
                    index, elements.size())
            );
        }
    }
    
    public List<IntValue> getElements() {
        return Collections.unmodifiableList(elements);
    }
    
    @Override
    public String getType() {
        return "array";
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < elements.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(elements.get(i));
        }
        sb.append("]");
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ArrayValue)) return false;
        ArrayValue other = (ArrayValue) obj;
        return elements.equals(other.elements);
    }
    
    @Override
    public int hashCode() {
        return elements.hashCode();
    }
}