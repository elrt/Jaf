package jaf.syntax;

import jaf.lang.AstVisitor;

public final class IfExpression implements Expression {
    
    private final Expression condition;
    private final Expression thenBranch;
    private final Expression elseBranch;
    
    public IfExpression(Expression condition, Expression thenBranch, Expression elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }
    
    public Expression getCondition() {
        return condition;
    }
    
    public Expression getThenBranch() {
        return thenBranch;
    }
    
    public Expression getElseBranch() {
        return elseBranch;
    }
    
    public boolean hasElseBranch() {
        return elseBranch != null;
    }
    
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visit(this);
    }
    
    @Override
    public String toString() {
        String result = "If(" + condition + ") " + thenBranch;
        if (elseBranch != null) {
            result += " else " + elseBranch;
        }
        return result;
    }
}