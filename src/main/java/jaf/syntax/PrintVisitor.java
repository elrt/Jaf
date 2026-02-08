package jaf.syntax;

import jaf.lang.AstVisitor;

public class PrintVisitor implements AstVisitor<String> {
    
    @Override
    public String visit(IntLiteral node) {
        return Integer.toString(node.getValue());
    }
    
    @Override
    public String visit(BinaryOp node) {
        String leftStr = node.getLeft().accept(this);
        String rightStr = node.getRight().accept(this);
        return "(" + leftStr + " " + node.getOperator().getSymbol() + " " + rightStr + ")";
    }
}