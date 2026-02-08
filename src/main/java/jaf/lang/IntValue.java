package jaf.lang;

public final class IntValue implements Value {
    
    private final int value;
    
    public IntValue(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    @Override
    public String getType() {
        return "int";
    }
    
    @Override
    public String toString() {
        return Integer.toString(value);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof IntValue)) return false;
        IntValue other = (IntValue) obj;
        return value == other.value;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }
}