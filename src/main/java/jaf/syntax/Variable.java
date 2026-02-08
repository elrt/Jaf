package jaf.syntax;

import jaf.lang.AstVisitor;

public final class Variable implements Expression {
    
    private final String name;
    
    public Variable(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visit(this);
    }
    
    @Override
    public String toString() {
        return "Variable(" + name + ")";
    }
}