package jaf.syntax;

import jaf.lang.AstVisitor;

public final class StringLiteral implements Expression {

    private final String value;

    public StringLiteral(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "\"" + value + "\"";
    }
}