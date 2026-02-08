package jaf.syntax;

import jaf.lang.AstVisitor;

public interface SyntaxVisitor<R> extends AstVisitor<R> {
    
    R visitIntLiteral(IntLiteral node);
    R visitBinaryOp(BinaryOp node);
}