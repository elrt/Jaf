package jaf.semantics;

import jaf.lang.AstVisitor;
import jaf.lang.Value;
import jaf.lang.JafRuntimeException;
import jaf.lang.IntValue;
import jaf.lang.BitValue;
import jaf.lang.BuiltinFunctionValue;
import jaf.lang.VoidValue;
import jaf.lang.FunctionValue;
import jaf.lang.ArrayValue;
import jaf.syntax.IntLiteral;
import jaf.syntax.BinaryOp;
import jaf.syntax.Variable;
import jaf.syntax.Assignment;
import jaf.syntax.Block;
import jaf.syntax.Expression;
import jaf.syntax.IfExpression;
import jaf.syntax.WhileExpression;
import jaf.syntax.FunctionDefinition;
import jaf.syntax.FunctionCall;
import jaf.syntax.ArrayLiteral;
import jaf.syntax.ArrayAccess;
import jaf.syntax.ArrayAssignment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class Interpreter implements AstVisitor<Value> {
    
    private Environment environment;
    
    private final Map<String, FunctionValue> functions;
    
    private static final int MAX_ITERATIONS = 1_000_000;
    private int iterationCount = 0;
    
    public Interpreter() {
        this(new MapEnvironment());
    }
    
    public Interpreter(Environment environment) {
        this.environment = environment;
        this.functions = new HashMap<>();
    }
    
    public Environment getEnvironment() {
        return environment;
    }
    
    public Value executeProgram(List<Expression> expressions) {
        Value lastValue = VoidValue.INSTANCE;
        
        for (Expression expr : expressions) {
            lastValue = expr.accept(this);
        }
        
        return lastValue;
    }
    
    private void resetIterationCount() {
        iterationCount = 0;
    }
    
    private void checkIterationLimit() {
        if (iterationCount >= MAX_ITERATIONS) {
            throw new JafRuntimeException(
                String.format("Iteration limit exceeded (%d). Possible infinite loop", MAX_ITERATIONS)
            );
        }
        iterationCount++;
    }
    
    private Value executeFunction(FunctionValue function, Value argument) {
        FunctionDefinition definition = function.getDefinition();
        
        Environment functionEnv = environment.createChild();
        
        functionEnv = functionEnv.put(definition.getParameterName(), argument);
        
        Environment oldEnv = this.environment;
        
        try {
            this.environment = functionEnv;
            
            return definition.getBody().accept(this);
            
        } finally {
            this.environment = oldEnv;
        }
    }
    
    private FunctionValue getFunction(String name) {
        FunctionValue function = functions.get(name);
        if (function == null) {
            throw new JafRuntimeException("Undefined function: '" + name + "'");
        }
        return function;
    }
    
    private Value builtinLength(Value argument) {
        if (!(argument instanceof ArrayValue)) {
            throw new JafRuntimeException("Function length expects array, got: " + argument.getType());
        }
        
        ArrayValue array = (ArrayValue) argument;
        return new IntValue(array.size());
    }
    
    @Override
    public Value visit(IntLiteral node) {
        return new IntValue(node.getValue());
    }
    
    @Override  
    public Value visit(BinaryOp node) {
        Value left = node.getLeft().accept(this);
        Value right = node.getRight().accept(this);
        
        switch (node.getOperator()) {
            case ADD:
                return add(left, right);
            case SUBTRACT:
                return subtract(left, right);
            case MULTIPLY:
                return multiply(left, right);
            case DIVIDE:
                return divide(left, right);
            case EQUAL:
                return equal(left, right);
            case NOT_EQUAL:
                return notEqual(left, right);
            case LESS:
                return less(left, right);
            case LESS_EQUAL:
                return lessEqual(left, right);
            case GREATER:
                return greater(left, right);
            case GREATER_EQUAL:
                return greaterEqual(left, right);
            case AND:
                return and(left, right);
            case OR:
                return or(left, right);
            default:
                throw new JafRuntimeException("Unknown operator: " + node.getOperator());
        }
    }
    
    @Override
    public Value visit(Variable node) {
        String name = node.getName();
        
        if (name.equals("length")) {
            return new BuiltinFunctionValue("length");
        }
        
        if (name.equals("print")) {
            return new BuiltinFunctionValue("print");
        }
        
        return environment.get(name);
    }
    
    @Override
    public Value visit(Assignment node) {
        Value value = node.getValue().accept(this);
        environment = environment.put(node.getVariableName(), value);
        return value;
    }
    
    @Override
    public Value visit(Block node) {
        Value lastValue = VoidValue.INSTANCE;
        
        for (Expression expr : node.getExpressions()) {
            lastValue = expr.accept(this);
        }
        
        return lastValue;
    }
    
    @Override
    public Value visit(IfExpression node) {
        Value conditionValue = node.getCondition().accept(this);
        
        boolean condition = toBoolean(conditionValue, "if condition");
        
        if (condition) {
            return node.getThenBranch().accept(this);
        } else if (node.getElseBranch() != null) {
            return node.getElseBranch().accept(this);
        } else {
            return VoidValue.INSTANCE;
        }
    }
    
    @Override
    public Value visit(WhileExpression node) {
        resetIterationCount();
        
        Value lastIterationValue = VoidValue.INSTANCE;
        
        while (true) {
            checkIterationLimit();
            
            Value conditionValue = node.getCondition().accept(this);
            boolean condition = toBoolean(conditionValue, "while condition");
            
            if (!condition) {
                break;
            }
            
            lastIterationValue = node.getBody().accept(this);
        }
        
        return lastIterationValue;
    }
    
    @Override
    public Value visit(FunctionDefinition node) {
        FunctionValue functionValue = new FunctionValue(node);
        
        functions.put(node.getFunctionName(), functionValue);
        
        environment = environment.put(node.getFunctionName(), functionValue);
        
        return functionValue;
    }
    
    @Override
    public Value visit(FunctionCall node) {
        String functionName = node.getFunctionName();
        
        if (functionName.equals("length")) {
            Value argument = node.getArgument().accept(this);
            
            return builtinLength(argument);
        }
        
        if (functionName.equals("print")) {
            Value argument = node.getArgument().accept(this);
            
            System.out.println(argument);
            
            return VoidValue.INSTANCE;
        }
        
        FunctionValue function = getFunction(functionName);
        
        Value argument = node.getArgument().accept(this);
        
        return executeFunction(function, argument);
    }
    
    @Override
    public Value visit(ArrayLiteral node) {
        List<IntValue> elements = new ArrayList<>();
        
        for (Expression elementExpr : node.getElements()) {
            Value elementValue = elementExpr.accept(this);
            
            if (!(elementValue instanceof IntValue)) {
                throw new JafRuntimeException(
                    "Array element must be integer, got: " + elementValue.getType()
                );
            }
            
            elements.add((IntValue) elementValue);
        }
        
        return new ArrayValue(elements);
    }
    
    @Override
    public Value visit(ArrayAccess node) {
        Value arrayValue = node.getArray().accept(this);
        
        if (!(arrayValue instanceof ArrayValue)) {
            throw new JafRuntimeException(
                "Expected array, got: " + arrayValue.getType()
            );
        }
        
        ArrayValue array = (ArrayValue) arrayValue;
        
        Value indexValue = node.getIndex().accept(this);
        int index = toInt(indexValue, "array access");
        
        return array.get(index);
    }
    
    @Override
    public Value visit(ArrayAssignment node) {
        Value arrayValue = node.getArray().accept(this);
        
        if (!(arrayValue instanceof ArrayValue)) {
            throw new JafRuntimeException(
                "Expected array, got: " + arrayValue.getType()
            );
        }
        
        ArrayValue array = (ArrayValue) arrayValue;
        
        Value indexValue = node.getIndex().accept(this);
        int index = toInt(indexValue, "array element assignment");
        
        Value value = node.getValue().accept(this);
        
        if (!(value instanceof IntValue)) {
            throw new JafRuntimeException(
                "Array element must be integer, got: " + value.getType()
            );
        }
        
        array.set(index, (IntValue) value);
        
        return value;
    }
    
    private Value add(Value left, Value right) {
        int leftInt = toInt(left, "addition");
        int rightInt = toInt(right, "addition");
        return new IntValue(leftInt + rightInt);
    }
    
    private Value subtract(Value left, Value right) {
        int leftInt = toInt(left, "subtraction");
        int rightInt = toInt(right, "subtraction");
        return new IntValue(leftInt - rightInt);
    }
    
    private Value multiply(Value left, Value right) {
        int leftInt = toInt(left, "multiplication");
        int rightInt = toInt(right, "multiplication");
        return new IntValue(leftInt * rightInt);
    }
    
    private Value divide(Value left, Value right) {
        int leftInt = toInt(left, "division");
        int rightInt = toInt(right, "division");
        
        if (rightInt == 0) {
            throw new JafRuntimeException("Division by zero");
        }
        
        return new IntValue(leftInt / rightInt);
    }
    
    private Value equal(Value left, Value right) {
        if (left.getType().equals(right.getType())) {
            if (left instanceof IntValue) {
                int leftInt = ((IntValue) left).getValue();
                int rightInt = ((IntValue) right).getValue();
                return new BitValue(leftInt == rightInt);
            }
            if (left instanceof BitValue) {
                boolean leftBool = ((BitValue) left).getValue();
                boolean rightBool = ((BitValue) right).getValue();
                return new BitValue(leftBool == rightBool);
            }
            if (left instanceof VoidValue) {
                return new BitValue(true);
            }
            if (left instanceof FunctionValue) {
                return new BitValue(left == right);
            }
            if (left instanceof ArrayValue) {
                return new BitValue(left.equals(right));
            }
        }
        
        try {
            int leftInt = toInt(left, "comparison");
            int rightInt = toInt(right, "comparison");
            return new BitValue(leftInt == rightInt);
        } catch (JafRuntimeException e) {
            return new BitValue(false);
        }
    }
    
    private Value notEqual(Value left, Value right) {
        BitValue eq = (BitValue) equal(left, right);
        return new BitValue(!eq.getValue());
    }
    
    private Value less(Value left, Value right) {
        int leftInt = toInt(left, "comparison");
        int rightInt = toInt(right, "comparison");
        return new BitValue(leftInt < rightInt);
    }
    
    private Value lessEqual(Value left, Value right) {
        int leftInt = toInt(left, "comparison");
        int rightInt = toInt(right, "comparison");
        return new BitValue(leftInt <= rightInt);
    }
    
    private Value greater(Value left, Value right) {
        int leftInt = toInt(left, "comparison");
        int rightInt = toInt(right, "comparison");
        return new BitValue(leftInt > rightInt);
    }
    
    private Value greaterEqual(Value left, Value right) {
        int leftInt = toInt(left, "comparison");
        int rightInt = toInt(right, "comparison");
        return new BitValue(leftInt >= rightInt);
    }
    
    private Value and(Value left, Value right) {
        boolean leftBool = toBoolean(left, "logical AND");
        boolean rightBool = toBoolean(right, "logical AND");
        return new BitValue(leftBool && rightBool);
    }
    
    private Value or(Value left, Value right) {
        boolean leftBool = toBoolean(left, "logical OR");
        boolean rightBool = toBoolean(right, "logical OR");
        return new BitValue(leftBool || rightBool);
    }
    
    private int toInt(Value value, String operation) {
        if (value instanceof IntValue) {
            return ((IntValue) value).getValue();
        }
        
        if (value instanceof BitValue) {
            return ((BitValue) value).getValue() ? 1 : 0;
        }
        
        if (value instanceof VoidValue) {
            throw new JafRuntimeException(
                String.format("Cannot use void in %s operation", operation)
            );
        }
        
        if (value instanceof FunctionValue) {
            throw new JafRuntimeException(
                String.format("Cannot use function in %s operation", operation)
            );
        }
        
        if (value instanceof ArrayValue) {
            throw new JafRuntimeException(
                String.format("Cannot use array in %s operation", operation)
            );
        }
        
        throw new JafRuntimeException(
            String.format("Unsupported type for arithmetic: %s", value.getType())
        );
    }
    
    private boolean toBoolean(Value value, String operation) {
        if (value instanceof BitValue) {
            return ((BitValue) value).getValue();
        }
        
        throw new JafRuntimeException(
            String.format("Cannot use type %s in %s operation", 
                value.getType(), operation)
        );
    }
}