package jaf.semantics;

import jaf.lang.Value;
import jaf.lang.Immutable;
import jaf.lang.JafRuntimeException;

public interface Environment extends Immutable {
    
    Value get(String name) throws JafRuntimeException;
    
    Environment put(String name, Value value);
    
    boolean containsLocal(String name);
    
    boolean contains(String name);
    
    Environment createChild();
    
    Environment getParent();
}