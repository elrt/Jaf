package jaf.lang;

public interface AstVisitor<R> {
    
    default R visit(AstNode node) {
        if (node instanceof jaf.syntax.IntLiteral) {
            return visit((jaf.syntax.IntLiteral) node);
        }
        if (node instanceof jaf.syntax.BinaryOp) {
            return visit((jaf.syntax.BinaryOp) node);
        }
        if (node instanceof jaf.syntax.Variable) {
            return visit((jaf.syntax.Variable) node);
        }
        if (node instanceof jaf.syntax.Assignment) {
            return visit((jaf.syntax.Assignment) node);
        }
        if (node instanceof jaf.syntax.Block) {
            return visit((jaf.syntax.Block) node);
        }
        if (node instanceof jaf.syntax.IfExpression) {
            return visit((jaf.syntax.IfExpression) node);
        }
        if (node instanceof jaf.syntax.WhileExpression) {
            return visit((jaf.syntax.WhileExpression) node);
        }
        if (node instanceof jaf.syntax.FunctionDefinition) {
            return visit((jaf.syntax.FunctionDefinition) node);
        }
        if (node instanceof jaf.syntax.FunctionCall) {
            return visit((jaf.syntax.FunctionCall) node);
        }
        if (node instanceof jaf.syntax.ArrayLiteral) {
            return visit((jaf.syntax.ArrayLiteral) node);
        }
        if (node instanceof jaf.syntax.ArrayAccess) {
            return visit((jaf.syntax.ArrayAccess) node);
        }
        if (node instanceof jaf.syntax.ArrayAssignment) {
            return visit((jaf.syntax.ArrayAssignment) node);
        }
        throw new JafVisitorException(getClass(), node.getClass());
    }
    
    default R visit(jaf.syntax.IntLiteral node) {
        throw new JafVisitorException(getClass(), node.getClass());
    }
    
    default R visit(jaf.syntax.BinaryOp node) {
        throw new JafVisitorException(getClass(), node.getClass());
    }
    
    default R visit(jaf.syntax.Variable node) {
        throw new JafVisitorException(getClass(), node.getClass());
    }
    
    default R visit(jaf.syntax.Assignment node) {
        throw new JafVisitorException(getClass(), node.getClass());
    }
    
    default R visit(jaf.syntax.Block node) {
        throw new JafVisitorException(getClass(), node.getClass());
    }
    
    default R visit(jaf.syntax.IfExpression node) {
        throw new JafVisitorException(getClass(), node.getClass());
    }
    
    default R visit(jaf.syntax.WhileExpression node) {
        throw new JafVisitorException(getClass(), node.getClass());
    }
    
    default R visit(jaf.syntax.FunctionDefinition node) {
        throw new JafVisitorException(getClass(), node.getClass());
    }
    
    default R visit(jaf.syntax.FunctionCall node) {
        throw new JafVisitorException(getClass(), node.getClass());
    }
    
    default R visit(jaf.syntax.ArrayLiteral node) {
        throw new JafVisitorException(getClass(), node.getClass());
    }
    
    default R visit(jaf.syntax.ArrayAccess node) {
        throw new JafVisitorException(getClass(), node.getClass());
    }
    
    default R visit(jaf.syntax.ArrayAssignment node) {
        throw new JafVisitorException(getClass(), node.getClass());
    }
}