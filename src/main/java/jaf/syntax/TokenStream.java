package jaf.syntax;

import jaf.lang.Immutable;
import jaf.lang.JafException;
import java.util.List;

public final class TokenStream implements Immutable {
    
    private final List<Token> tokens;
    private int position = 0;
    
    public TokenStream(List<Token> tokens) {
        this.tokens = tokens;
    }
    
    public Token peek() {
        if (isAtEnd()) {
            return tokens.get(tokens.size() - 1);
        }
        return tokens.get(position);
    }
    
    public Token next() {
        if (!isAtEnd()) {
            position++;
        }
        return tokens.get(position - 1);
    }
    
    public boolean isAtEnd() {
        return position >= tokens.size() - 1;
    }
    
    public boolean check(Token.Type type) {
        if (isAtEnd()) return false;
        return peek().getType() == type;
    }
    
    public boolean match(Token.Type type) {
        if (check(type)) {
            next();
            return true;
        }
        return false;
    }
    
    public Token consume(Token.Type type, String errorMessage) throws JafException {
        if (check(type)) {
            return next();
        }
        Token actual = peek();
        throw new JafException(
            String.format("%s. Expected %s, got %s",
                errorMessage, type, actual.getType()),
            actual.getLine(), actual.getColumn()
        );
    }
    
    public int getPosition() {
        return position;
    }
    
    public List<Token> getTokens() {
        return tokens;
    }
}