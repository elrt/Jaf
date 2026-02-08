package jaf.syntax;

import jaf.lang.JafException;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    
    private final TokenStream tokens;
    
    public Parser(TokenStream tokens) {
        this.tokens = tokens;
    }
    
    public Expression parse() throws JafException {
        return expression();
    }
    
    public List<Expression> parseProgram() throws JafException {
        List<Expression> expressions = new ArrayList<>();
        
        while (!tokens.isAtEnd()) {
            expressions.add(expression());
            
            if (tokens.match(Token.Type.SEMICOLON)) {
            }
        }
        
        return expressions;
    }
    
    private Expression expression() throws JafException {
        return assignment();
    }
    
    private Expression assignment() throws JafException {
        Expression expr = logicalOr();
        
        if (tokens.match(Token.Type.ASSIGN)) {
            if (expr instanceof Variable) {
                Variable variable = (Variable) expr;
                Expression value = assignment();
                return new Assignment(variable.getName(), value);
            } else if (expr instanceof ArrayAccess) {
                ArrayAccess arrayAccess = (ArrayAccess) expr;
                Expression value = assignment();
                return new ArrayAssignment(arrayAccess.getArray(), arrayAccess.getIndex(), value);
            } else {
                Token equals = tokens.getTokens().get(tokens.getPosition() - 1);
                throw new JafException(
                    "Left side of assignment must be variable or array access",
                    equals.getLine(), equals.getColumn()
                );
            }
        }
        
        return expr;
    }
    
    private Expression logicalOr() throws JafException {
        Expression expr = logicalAnd();
        
        while (tokens.match(Token.Type.OR)) {
            Expression right = logicalAnd();
            expr = new BinaryOp(BinaryOp.Operator.OR, expr, right);
        }
        
        return expr;
    }
    
    private Expression logicalAnd() throws JafException {
        Expression expr = equality();
        
        while (tokens.match(Token.Type.AND)) {
            Expression right = equality();
            expr = new BinaryOp(BinaryOp.Operator.AND, expr, right);
        }
        
        return expr;
    }
    
    private Expression equality() throws JafException {
        Expression expr = comparison();
        
        while (tokens.match(Token.Type.EQUAL) || tokens.match(Token.Type.NOT_EQUAL)) {
            Token operator = tokens.getTokens().get(tokens.getPosition() - 1);
            Expression right = comparison();
            
            BinaryOp.Operator op = operator.getType() == Token.Type.EQUAL 
                ? BinaryOp.Operator.EQUAL 
                : BinaryOp.Operator.NOT_EQUAL;
            
            expr = new BinaryOp(op, expr, right);
        }
        
        return expr;
    }
    
    private Expression comparison() throws JafException {
        Expression expr = addition();
        
        while (tokens.match(Token.Type.LESS) || tokens.match(Token.Type.LESS_EQUAL) ||
               tokens.match(Token.Type.GREATER) || tokens.match(Token.Type.GREATER_EQUAL)) {
            
            Token operator = tokens.getTokens().get(tokens.getPosition() - 1);
            Expression right = addition();
            
            BinaryOp.Operator op;
            switch (operator.getType()) {
                case LESS: op = BinaryOp.Operator.LESS; break;
                case LESS_EQUAL: op = BinaryOp.Operator.LESS_EQUAL; break;
                case GREATER: op = BinaryOp.Operator.GREATER; break;
                case GREATER_EQUAL: op = BinaryOp.Operator.GREATER_EQUAL; break;
                default: throw new JafException("Unknown comparison operator", 
                    operator.getLine(), operator.getColumn());
            }
            
            expr = new BinaryOp(op, expr, right);
        }
        
        return expr;
    }
    
    private Expression addition() throws JafException {
        Expression expr = term();
        
        while (tokens.match(Token.Type.PLUS) || tokens.match(Token.Type.MINUS)) {
            Token operator = tokens.getTokens().get(tokens.getPosition() - 1);
            Expression right = term();
            
            BinaryOp.Operator op = operator.getType() == Token.Type.PLUS 
                ? BinaryOp.Operator.ADD 
                : BinaryOp.Operator.SUBTRACT;
            
            expr = new BinaryOp(op, expr, right);
        }
        
        return expr;
    }
    
    private Expression term() throws JafException {
        Expression expr = factor();
        
        while (tokens.match(Token.Type.MULTIPLY) || tokens.match(Token.Type.DIVIDE)) {
            Token operator = tokens.getTokens().get(tokens.getPosition() - 1);
            Expression right = factor();
            
            BinaryOp.Operator op = operator.getType() == Token.Type.MULTIPLY 
                ? BinaryOp.Operator.MULTIPLY 
                : BinaryOp.Operator.DIVIDE;
            
            expr = new BinaryOp(op, expr, right);
        }
        
        return expr;
    }
    
    private Expression factor() throws JafException {
        return access();
    }
    
    private Expression access() throws JafException {
        Expression expr = primary();
        
        while (tokens.match(Token.Type.LEFT_BRACKET)) {
            Expression index = expression();
            tokens.consume(Token.Type.RIGHT_BRACKET, "Expected ']' after array index");
            expr = new ArrayAccess(expr, index);
        }
        
        if (expr instanceof Variable && tokens.match(Token.Type.LEFT_PAREN)) {
            String functionName = ((Variable) expr).getName();
            Expression argument = expression();
            tokens.consume(Token.Type.RIGHT_PAREN, "Expected ')' after function argument");
            return new FunctionCall(functionName, argument);
        }
        
        return expr;
    }
    
    private Expression unary() throws JafException {
        if (tokens.match(Token.Type.MINUS)) {
            Expression right = unary();
            return new BinaryOp(BinaryOp.Operator.SUBTRACT, new IntLiteral(0), right);
        }
        
        return access();
    }
    
    private Expression primary() throws JafException {
        if (tokens.match(Token.Type.LEFT_BRACKET)) {
            return arrayLiteral();
        }
        
        if (tokens.match(Token.Type.FUNC)) {
            return funcDefinition();
        }
        
        if (tokens.match(Token.Type.WHILE)) {
            return whileExpression();
        }
        
        if (tokens.match(Token.Type.IF)) {
            return ifExpression();
        }
        
        if (tokens.match(Token.Type.LEFT_BRACE)) {
            return block();
        }
        
        if (tokens.match(Token.Type.INT_LITERAL)) {
            Token token = tokens.getTokens().get(tokens.getPosition() - 1);
            return new IntLiteral(token.getIntValue());
        }
        
        if (tokens.match(Token.Type.IDENTIFIER)) {
            Token token = tokens.getTokens().get(tokens.getPosition() - 1);
            return new Variable(token.getIdentifier());
        }
        
        if (tokens.match(Token.Type.LEFT_PAREN)) {
            Expression expr = expression();
            tokens.consume(Token.Type.RIGHT_PAREN, "Expected closing parenthesis ')'");
            return expr;
        }
        
        Token token = tokens.peek();
        throw new JafException(
            String.format("Expected number, variable, array, func, while, if, block or parentheses, got %s", token.getType()),
            token.getLine(), token.getColumn()
        );
    }
    
    private Expression arrayLiteral() throws JafException {
        List<Expression> elements = new ArrayList<>();
        
        if (!tokens.check(Token.Type.RIGHT_BRACKET)) {
            do {
                elements.add(expression());
            } while (tokens.match(Token.Type.COMMA));
        }
        
        tokens.consume(Token.Type.RIGHT_BRACKET, "Expected ']' at end of array literal");
        
        return new ArrayLiteral(elements);
    }
    
    private Expression funcDefinition() throws JafException {
        Token nameToken = tokens.consume(Token.Type.IDENTIFIER, "Expected function name after 'func'");
        String functionName = nameToken.getIdentifier();
        
        tokens.consume(Token.Type.LEFT_PAREN, "Expected '(' after function name");
        
        Token paramToken = tokens.consume(Token.Type.IDENTIFIER, "Expected parameter name");
        String parameterName = paramToken.getIdentifier();
        
        tokens.consume(Token.Type.RIGHT_PAREN, "Expected ')' after function parameter");
        
        Expression body = expression();
        
        return new FunctionDefinition(functionName, parameterName, body);
    }
    
    private Expression whileExpression() throws JafException {
        tokens.consume(Token.Type.LEFT_PAREN, "Expected '(' after 'while'");
        Expression condition = expression();
        tokens.consume(Token.Type.RIGHT_PAREN, "Expected ')' after while condition");
        Expression body = expression();
        return new WhileExpression(condition, body);
    }
    
    private Expression ifExpression() throws JafException {
        tokens.consume(Token.Type.LEFT_PAREN, "Expected '(' after 'if'");
        Expression condition = expression();
        tokens.consume(Token.Type.RIGHT_PAREN, "Expected ')' after if condition");
        
        Expression thenBranch = expression();
        
        Expression elseBranch = null;
        if (tokens.match(Token.Type.ELSE)) {
            elseBranch = expression();
        }
        
        return new IfExpression(condition, thenBranch, elseBranch);
    }
    
    private Expression block() throws JafException {
        List<Expression> expressions = new ArrayList<>();
        
        while (!tokens.check(Token.Type.RIGHT_BRACE) && !tokens.isAtEnd()) {
            expressions.add(expression());
            
            if (tokens.match(Token.Type.SEMICOLON)) {
            }
        }
        
        tokens.consume(Token.Type.RIGHT_BRACE, "Expected '}' at end of block");
        
        if (expressions.isEmpty()) {
            return new Block(expressions);
        }
        
        return new Block(expressions);
    }
}