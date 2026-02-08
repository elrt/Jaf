package jaf.lang;

public class JafVisitorException extends JafRuntimeException {
    
    public JafVisitorException(String message) {
        super(message);
    }
    
    public JafVisitorException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public JafVisitorException(Class<?> nodeType, Class<?> visitorType) {
        super(String.format(
            "Visitor of type %s does not support node of type %s",
            getVisitorName(visitorType),
            nodeType.getSimpleName()
        ));
    }
    
    private static String getVisitorName(Class<?> visitorType) {
        String name = visitorType.getSimpleName();
        if (name.isEmpty()) {
            name = visitorType.getName();
            int lastDot = name.lastIndexOf('.');
            if (lastDot != -1) {
                name = name.substring(lastDot + 1);
            }
        }
        return name;
    }
}