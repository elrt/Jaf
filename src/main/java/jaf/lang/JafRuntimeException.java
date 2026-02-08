package jaf.lang;

public class JafRuntimeException extends RuntimeException {
    
    public JafRuntimeException(String message) {
        super(message);
    }
    
    public JafRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}