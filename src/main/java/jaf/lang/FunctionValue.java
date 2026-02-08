package jaf.lang;

import jaf.syntax.FunctionDefinition;

public final class FunctionValue implements Value {
    
    private final FunctionDefinition definition;
    
    public FunctionValue(FunctionDefinition definition) {
        this.definition = definition;
    }
    
    public FunctionDefinition getDefinition() {
        return definition;
    }
    
    @Override
    public String getType() {
        return "function";
    }
    
    @Override
    public String toString() {
        return "function:" + definition.getFunctionName();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof FunctionValue)) return false;
        FunctionValue other = (FunctionValue) obj;
        return definition.equals(other.definition);
    }
    
    @Override
    public int hashCode() {
        return definition.hashCode();
    }
}