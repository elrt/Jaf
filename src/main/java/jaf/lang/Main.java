package jaf.lang;

import jaf.syntax.*;
import jaf.semantics.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }
        
        String command = args[0];
        
        switch (command) {
            case "run":
                if (args.length < 2) {
                    System.err.println("Error: specify file to run");
                    printUsage();
                    return;
                }
                runFile(args[1]);
                break;
                
            case "eval":
                if (args.length < 2) {
                    System.err.println("Error: specify expression to evaluate");
                    printUsage();
                    return;
                }
                evalExpression(String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length)));
                break;
                
            case "help":
                printHelp();
                break;
                
            default:
                if (command.endsWith(".jaf")) {
                    runFile(command);
                } else {
                    System.err.println("Unknown command: " + command);
                    printUsage();
                }
        }
    }
    
    private static void runFile(String filename) {
        try {
            String source = new String(Files.readAllBytes(Paths.get(filename)));
            System.out.println("Running file: " + filename);
            System.out.println("==================================" );
            
            executeProgram(source);
            
        } catch (IOException e) {
            System.err.println("File read error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Execution error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void evalExpression(String expression) {
        try {
            System.out.println("Evaluating: " + expression);
            System.out.println("==================================");
            
            executeProgram(expression);
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private static void executeProgram(String source) throws Exception {
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();
        
        TokenStream stream = new TokenStream(tokens);
        Parser parser = new Parser(stream);
        List<Expression> expressions = parser.parseProgram();
        
        Interpreter interpreter = new Interpreter();
        Value result = interpreter.executeProgram(expressions);
        
        System.out.println("\nResult: " + result);
    }
    
    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  java -jar jaf.jar run <file.jaf>  - run Jaf file");
        System.out.println("  java -jar jaf.jar eval <code>     - evaluate expression");
        System.out.println("  java -jar jaf.jar help            - show help");
        System.out.println("  java -jar jaf.jar <file.jaf>      - run file (short form)");
    }
    
    private static void printHelp() {
        System.out.println("Jaf - minimalistic interpreted language");
        System.out.println("==================================");
        System.out.println("\nLanguage syntax:");
        System.out.println("  Variables:    x = 42");
        System.out.println("  Arithmetic:   x + y, x - y, x * y, x / y");
        System.out.println("  Comparisons:  x == y, x != y, x < y, x <= y, x > y, x >= y");
        System.out.println("  Logic:        x && y, x || y");
        System.out.println("  Conditionals: if (condition) expression [else expression]");
        System.out.println("  Loops:        while (condition) expression");
        System.out.println("  Blocks:       { expression1; expression2; ... }");
        System.out.println("  Functions:    func name(parameter) body");
        System.out.println("  Arrays:       arr = [1, 2, 3], arr[0], arr[0] = 42, length(arr)");
        System.out.println("\nExample .jaf files are in examples/");
    }
}