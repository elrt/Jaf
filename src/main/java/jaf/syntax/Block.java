package jaf.syntax;

import jaf.lang.AstVisitor;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public final class Block implements Expression {
    
    private final List<Expression> expressions;
    
    public Block(List<Expression> expressions) {
        this.expressions = new ArrayList<>(expressions);
    }
    
    public List<Expression> getExpressions() {
        return Collections.unmodifiableList(expressions);
    }
    
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visit(this);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Block[");
        for (int i = 0; i < expressions.size(); i++) {
            if (i > 0) sb.append("; ");
            sb.append(expressions.get(i));
        }
        sb.append("]");
        return sb.toString();
    }
}