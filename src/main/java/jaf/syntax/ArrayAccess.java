package jaf.syntax;

import jaf.lang.AstVisitor;

public final class ArrayAccess implements Expression {
    
    private final Expression array;
    private final Expression index;
    
    public ArrayAccess(Expression array, Expression index) {
        this.array = array;
        this.index = index;
    }
    
    public Expression getArray() {
        return array;
    }
    
    public Expression getIndex() {
        return index;
    }
    
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visit(this);
    }
    
    @Override
    public String toString() {
        return array + "[" + index + "]";
    }
}