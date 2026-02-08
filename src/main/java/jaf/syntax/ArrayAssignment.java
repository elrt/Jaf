package jaf.syntax;

import jaf.lang.AstVisitor;

public final class ArrayAssignment implements Expression {
    
    private final Expression array;
    private final Expression index;
    private final Expression value;
    
    public ArrayAssignment(Expression array, Expression index, Expression value) {
        this.array = array;
        this.index = index;
        this.value = value;
    }
    
    public Expression getArray() {
        return array;
    }
    
    public Expression getIndex() {
        return index;
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
        return array + "[" + index + "] = " + value;
    }
}