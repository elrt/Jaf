package jaf.lang;

public interface Value extends Immutable {
    
    String getType();
    
    @Override
    String toString();
}