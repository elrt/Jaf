package jaf.lang;

public interface AstNode extends Immutable {
    
    <R> R accept(AstVisitor<R> visitor);
}