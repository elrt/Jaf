package jaf.lang;

public final class BitValue implements Value {
    
    private final boolean value;
    
    public BitValue(boolean value) {
        this.value = value;
    }
    
    public boolean getValue() {
        return value;
    }
    
    @Override
    public String getType() {
        return "bit";
    }
    
    @Override
    public String toString() {
        return value ? "1" : "0";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof BitValue)) return false;
        BitValue other = (BitValue) obj;
        return value == other.value;
    }
    
    @Override
    public int hashCode() {
        return Boolean.hashCode(value);
    }
}