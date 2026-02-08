package jaf.semantics;

import jaf.lang.Value;
import jaf.lang.JafRuntimeException;
import java.util.HashMap;
import java.util.Map;

public class MapEnvironment implements Environment {
    
    private final Map<String, Value> variables;
    private final Environment parent;
    
    public MapEnvironment() {
        this(new HashMap<>(), null);
    }
    
    private MapEnvironment(Map<String, Value> variables, Environment parent) {
        this.variables = new HashMap<>(variables);
        this.parent = parent;
    }
    
    @Override
    public Value get(String name) throws JafRuntimeException {
        if (variables.containsKey(name)) {
            return variables.get(name);
        }
        if (parent != null) {
            return parent.get(name);
        }
        throw new JafRuntimeException("Undefined variable: '" + name + "'");
    }
    
    @Override
    public Environment put(String name, Value value) {
        Map<String, Value> newVars = new HashMap<>(variables);
        newVars.put(name, value);
        return new MapEnvironment(newVars, parent);
    }
    
    @Override
    public boolean containsLocal(String name) {
        return variables.containsKey(name);
    }
    
    @Override
    public boolean contains(String name) {
        if (variables.containsKey(name)) {
            return true;
        }
        return parent != null && parent.contains(name);
    }
    
    @Override
    public Environment createChild() {
        return new MapEnvironment(new HashMap<>(), this);
    }
    
    @Override
    public Environment getParent() {
        return parent;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Environment{");
        sb.append("variables=").append(variables.keySet());
        if (parent != null) {
            sb.append(", parent=").append(parent);
        }
        sb.append("}");
        return sb.toString();
    }
}