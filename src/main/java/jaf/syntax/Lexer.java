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
            case '#':
                while (peek() != '\n' && !isAtEnd()) {
                    advance();
                }
                break;

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
                } else if (c == '.' && isDigit(peek())) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    throw new JafException(
                            String.format("Unexpected character '%c'", c),
                            line, column - 1
                    );
                }
                break;
        }
    }

    private void number() {
        while (isDigit(peek())) {
            advance();
        }

        if (peek() == '.' && isDigit(peekNext())) {
            advance();
            while (isDigit(peek())) {
                advance();
            }
        }

        if (peek() == 'e' || peek() == 'E') {
            advance();
            if (peek() == '+' || peek() == '-') {
                advance();
            }
            while (isDigit(peek())) {
                advance();
            }
        }

        String lexeme = source.substring(start, current);
        double value = Double.parseDouble(lexeme);
        addToken(new Token(Token.Type.NUMBER, lexeme, value, line, column - lexeme.length()));
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

    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
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
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
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