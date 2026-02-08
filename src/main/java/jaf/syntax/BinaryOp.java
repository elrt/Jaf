package jaf.syntax;

import jaf.lang.AstVisitor;

public final class BinaryOp implements Expression {
    
    public enum Operator {
        ADD("+"),
        SUBTRACT("-"),
        MULTIPLY("*"),
        DIVIDE("/"),
        EQUAL("=="),
        NOT_EQUAL("!="),
        LESS("<"),
        LESS_EQUAL("<="),
        GREATER(">"),
        GREATER_EQUAL(">="),
        AND("&&"),
        OR("||");
        
        private final String symbol;
        
        Operator(String symbol) {
            this.symbol = symbol;
        }
        
        public String getSymbol() {
            return symbol;
        }
    }
    
    private final Operator operator;
    private final Expression left;
    private final Expression right;
    
    public BinaryOp(Operator operator, Expression left, Expression right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }
    
    public Operator getOperator() {
        return operator;
    }
    
    public Expression getLeft() {
        return left;
    }
    
    public Expression getRight() {
        return right;
    }
    
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visit(this);
    }
    
    @Override
    public String toString() {
        return "BinaryOp(" + left + " " + operator.getSymbol() + " " + right + ")";
    }
}