package jaf.syntax;

import jaf.lang.AstVisitor;

public final class NumberLiteral implements Expression {

    private final double value;

    public NumberLiteral(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        if (value == (long) value) {
            return "NumberLiteral(" + (long) value + ")";
        }
        return "NumberLiteral(" + value + ")";
    }
}