package jaf.syntax;

import jaf.lang.AstVisitor;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public final class ArrayLiteral implements Expression {
    
    private final List<Expression> elements;
    
    public ArrayLiteral(List<Expression> elements) {
        this.elements = new ArrayList<>(elements);
    }
    
    public List<Expression> getElements() {
        return Collections.unmodifiableList(elements);
    }
    
    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visit(this);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < elements.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(elements.get(i));
        }
        sb.append("]");
        return sb.toString();
    }
}