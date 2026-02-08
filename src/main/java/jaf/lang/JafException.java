package jaf.lang;

public class JafException extends Exception {
    
    public JafException(String message) {
        super(message);
    }
    
    public JafException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public JafException(String message, int line, int column) {
        super(String.format("Error at line %d, column %d: %s", line, column, message));
    }
}