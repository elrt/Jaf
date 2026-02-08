package jaf.syntax;

import jaf.lang.AstVisitor;

public final class FunctionDefinition implements Expression {
    
    private final String functionName;
    private final String parameterName;
    private final Expression body;
    
    public FunctionDefinition(String functionName, String parameterName, Expression body) {
        this.functionName = functionName;
        this.parameterName = parameterName;
        this.body = body;
    }
    
    public String getFunctionName() {
        return functionName;
    }
    
    public String getParameterName() {
        return parameterName;
    }
    
    public Expression getBody() {
        return body;
    }
    
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visit(this);
    }
    
    @Override
    public String toString() {
        return "func " + functionName + "(" + parameterName + ") " + body;
    }
}