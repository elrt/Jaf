package jaf.lang;

public final class VoidValue implements Value {
    
    public static final VoidValue INSTANCE = new VoidValue();
    
    private VoidValue() {}
    
    @Override
    public String getType() {
        return "void";
    }
    
    @Override
    public String toString() {
        return "void";
    }
    
    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }
    
    @Override
    public int hashCode() {
        return 0;
    }
}