package jaf.syntax;

import jaf.lang.AstVisitor;

public final class WhileExpression implements Expression {
    
    private final Expression condition;
    private final Expression body;
    
    public WhileExpression(Expression condition, Expression body) {
        this.condition = condition;
        this.body = body;
    }
    
    public Expression getCondition() {
        return condition;
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
        return "While(" + condition + ") " + body;
    }
}