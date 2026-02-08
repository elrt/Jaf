package jaf.lang;

public final class BuiltinFunctionValue implements Value {
    
    private final String name;
    
    public BuiltinFunctionValue(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String getType() {
        return "builtin";
    }
    
    @Override
    public String toString() {
        return "builtin:" + name;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof BuiltinFunctionValue)) return false;
        BuiltinFunctionValue other = (BuiltinFunctionValue) obj;
        return name.equals(other.name);
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}