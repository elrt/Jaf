package jaf.syntax;

import jaf.lang.AstVisitor;

public final class FunctionCall implements Expression {
    
    private final String functionName;
    private final Expression argument;
    
    public FunctionCall(String functionName, Expression argument) {
        this.functionName = functionName;
        this.argument = argument;
    }
    
    public String getFunctionName() {
        return functionName;
    }
    
    public Expression getArgument() {
        return argument;
    }
    
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visit(this);
    }
    
    @Override
    public String toString() {
        return functionName + "(" + argument + ")";
    }
}