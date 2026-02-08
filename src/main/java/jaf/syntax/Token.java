package jaf.syntax;

import jaf.lang.Immutable;

public final class Token implements Immutable {
    
    public enum Type {
        INT_LITERAL,
        IDENTIFIER,
        IF,
        ELSE,
        WHILE,
        FUNC,
        PLUS,
        MINUS,
        MULTIPLY,
        DIVIDE,
        EQUAL,
        NOT_EQUAL,
        LESS,
        LESS_EQUAL,
        GREATER,
        GREATER_EQUAL,
        AND,
        OR,
        ASSIGN,
        LEFT_PAREN,
        RIGHT_PAREN,
        LEFT_BRACE,
        RIGHT_BRACE,
        LEFT_BRACKET,
        RIGHT_BRACKET,
        SEMICOLON,
        COMMA,
        EOF
    }
    
    private final Type type;
    private final String lexeme;
    private final Object literal;
    private final int line;
    private final int column;
    
    public Token(Type type, String lexeme, Object literal, int line, int column) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
        this.column = column;
    }
    
    public static Token intLiteral(int value, String lexeme, int line, int column) {
        return new Token(Type.INT_LITERAL, lexeme, value, line, column);
    }
    
    public static Token identifier(String name, int line, int column) {
        return new Token(Type.IDENTIFIER, name, name, line, column);
    }
    
    public static Token eof(int line, int column) {
        return new Token(Type.EOF, "", null, line, column);
    }
    
    public Type getType() {
        return type;
    }
    
    public String getLexeme() {
        return lexeme;
    }
    
    public Object getLiteral() {
        return literal;
    }
    
    public int getLine() {
        return line;
    }
    
    public int getColumn() {
        return column;
    }
    
    public int getIntValue() {
        if (type != Type.INT_LITERAL) {
            throw new IllegalStateException("Token is not INT_LITERAL: " + type);
        }
        return (Integer) literal;
    }
    
    public String getIdentifier() {
        if (type != Type.IDENTIFIER) {
            throw new IllegalStateException("Token is not IDENTIFIER: " + type);
        }
        return (String) literal;
    }
    
    public boolean isKeyword() {
        return type == Type.IF || type == Type.ELSE || type == Type.WHILE || type == Type.FUNC;
    }
    
    @Override
    public String toString() {
        String literalStr = literal != null ? ", value=" + literal : "";
        return String.format("Token[%s, lexeme='%s'%s, line=%d, col=%d]", 
            type, lexeme, literalStr, line, column);
    }
}