package jaf.syntax;

import jaf.lang.AstVisitor;

public final class Assignment implements Expression {
    
    private final String variableName;
    private final Expression value;
    
    public Assignment(String variableName, Expression value) {
        this.variableName = variableName;
        this.value = value;
    }
    
    public String getVariableName() {
        return variableName;
    }
    
    public Expression getValue() {
        return value;
    }
    
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visit(this);
    }
    
    @Override
    public String toString() {
        return "Assignment(" + variableName + " = " + value + ")";
    }
}