package jaf.syntax;

import jaf.lang.JafException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {
    
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private final Map<String, Token.Type> keywords;
    private int start = 0;
    private int current = 0;
    private int line = 1;
    private int column = 1;
    
    public Lexer(String source) {
        this.source = source;
        
        keywords = new HashMap<>();
        keywords.put("if", Token.Type.IF);
        keywords.put("else", Token.Type.ELSE);
        keywords.put("while", Token.Type.WHILE);
        keywords.put("func", Token.Type.FUNC);
    }
    
    public List<Token> scanTokens() throws JafException {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }
        tokens.add(Token.eof(line, column));
        return tokens;
    }
    
    private void scanToken() throws JafException {
        char c = advance();
        
        switch (c) {
            case '+': addToken(Token.Type.PLUS); break;
            case '-': addToken(Token.Type.MINUS); break;
            case '*': addToken(Token.Type.MULTIPLY); break;
            case '/': addToken(Token.Type.DIVIDE); break;
            case '(': addToken(Token.Type.LEFT_PAREN); break;
            case ')': addToken(Token.Type.RIGHT_PAREN); break;
            case '{': addToken(Token.Type.LEFT_BRACE); break;
            case '}': addToken(Token.Type.RIGHT_BRACE); break;
            case '[': addToken(Token.Type.LEFT_BRACKET); break;
            case ']': addToken(Token.Type.RIGHT_BRACKET); break;
            case ';': addToken(Token.Type.SEMICOLON); break;
            case ',': addToken(Token.Type.COMMA); break;
            case '<': addToken(match('=') ? Token.Type.LESS_EQUAL : Token.Type.LESS); break;
            case '>': addToken(match('=') ? Token.Type.GREATER_EQUAL : Token.Type.GREATER); break;
            case '=': 
                if (match('=')) {
                    addToken(Token.Type.EQUAL);
                } else {
                    addToken(Token.Type.ASSIGN);
                }
                break;
            case '!':
                if (match('=')) {
                    addToken(Token.Type.NOT_EQUAL);
                } else {
                    addToken(Token.Type.MINUS);
                }
                break;
            case '&':
                if (match('&')) {
                    addToken(Token.Type.AND);
                } else {
                    throw new JafException("Expected '&&'", line, column - 1);
                }
                break;
            case '|':
                if (match('|')) {
                    addToken(Token.Type.OR);
                } else {
                    throw new JafException("Expected '||'", line, column - 1);
                }
                break;
            
            case ' ':
            case '\t':
            case '\r':
                break;
                
            case '\n':
                line++;
                column = 1;
                break;
                
            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    throw new JafException(
                        String.format("Unexpeted character '%c'", c),
                        line, column - 1
                    );
                }
                break;
        }
    }
    
    private void number() {
        boolean isNegative = false;
        if (peekPrevious() == '-') {
            if (tokens.isEmpty() || 
                tokens.get(tokens.size() - 1).getType() == Token.Type.LEFT_PAREN ||
                tokens.get(tokens.size() - 1).getType() == Token.Type.ASSIGN ||
                tokens.get(tokens.size() - 1).getType() == Token.Type.PLUS ||
                tokens.get(tokens.size() - 1).getType() == Token.Type.MINUS ||
                tokens.get(tokens.size() - 1).getType() == Token.Type.MULTIPLY ||
                tokens.get(tokens.size() - 1).getType() == Token.Type.DIVIDE ||
                tokens.get(tokens.size() - 1).getType() == Token.Type.EQUAL ||
                tokens.get(tokens.size() - 1).getType() == Token.Type.NOT_EQUAL ||
                tokens.get(tokens.size() - 1).getType() == Token.Type.LESS ||
                tokens.get(tokens.size() - 1).getType() == Token.Type.LESS_EQUAL ||
                tokens.get(tokens.size() - 1).getType() == Token.Type.GREATER ||
                tokens.get(tokens.size() - 1).getType() == Token.Type.GREATER_EQUAL ||
                tokens.get(tokens.size() - 1).getType() == Token.Type.AND ||
                tokens.get(tokens.size() - 1).getType() == Token.Type.OR) {
                isNegative = true;
            }
        }
        
        while (isDigit(peek())) {
            advance();
        }
        
        String lexeme = source.substring(start, current);
        try {
            int value = Integer.parseInt(lexeme);
            addToken(Token.intLiteral(value, lexeme, line, column - lexeme.length()));
        } catch (NumberFormatException e) {
            throw new jaf.lang.JafRuntimeException("Invalid numeric literal: " + lexeme);
        }
    }
    
    private void identifier() {
        while (isAlphaNumeric(peek())) {
            advance();
        }
        
        String lexeme = source.substring(start, current);
        
        Token.Type type = keywords.get(lexeme);
        if (type == null) {
            type = Token.Type.IDENTIFIER;
        }
        
        addToken(new Token(type, lexeme, lexeme, line, column - lexeme.length()));
    }
    
    private char advance() {
        column++;
        return source.charAt(current++);
    }
    
    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }
    
    private char peekPrevious() {
        if (current == 0) return '\0';
        return source.charAt(current - 1);
    }
    
    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;
        current++;
        column++;
        return true;
    }
    
    private boolean isAtEnd() {
        return current >= source.length();
    }
    
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }
    
    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
               (c >= 'A' && c <= 'Z') ||
               c == '_';
    }
    
    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }
    
    private void addToken(Token.Type type) {
        String lexeme = source.substring(start, current);
        tokens.add(new Token(type, lexeme, null, line, column - lexeme.length()));
    }
    
    private void addToken(Token token) {
        tokens.add(token);
    }
}