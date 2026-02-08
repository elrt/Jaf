package jaf.syntax;

import jaf.lang.AstVisitor;

public final class IntLiteral implements Expression {
    
    private final int value;
    
    public IntLiteral(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visit(this);
    }
    
    @Override
    public String toString() {
        return "IntLiteral(" + value + ")";
    }
}