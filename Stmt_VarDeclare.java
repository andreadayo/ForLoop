import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Stmt_VarDeclare {
  public static void main(String[] args) {
    // Read syntax input
    // Scanner scan = new Scanner(System.in);
    // System.out.print("Enter syntax: ");
    // String syntaxInput = scan.nextLine();

    String syntaxInput = "for ( int i = -1 ; i < length ; ++i ) { int a = b ; int test = int ; test = 0 ; a += 0 ; ++g ; g-- ; --g ; g++ ; System.out.print ( 'hello' ) ; }";

    // Split the input into an array and pass to tokenizer()
    String[] arrayInput = syntaxInput.trim().split(" ");
    String[] token = tokenizer(arrayInput);
    System.out.println(Arrays.toString(token));

    // For Statement Checker
    int startStatement = 0, endStatement = 0;

    for (int i = 0; i < token.length; i++) {
      if (token[i].equals("{")) {
        startStatement = i;
      } else if (token[i].equals("}")) {
        endStatement = i;
      }
    }

    checkStatement(token, startStatement, endStatement);
    // System.out.println(checkStatement(token, startStatement, endStatement));
  }

  // tokenizer(): Stores the array in a token with its corresponding label
  public static String[] tokenizer(String[] array) {
    String[] token = new String[array.length];

    // Symbols
    Set<String> symbols = new HashSet<>(Arrays.asList("(", ")", "{", "}", ";", "<", ">", "<=", "=>", "!=", "=", "=="));
    // Operators
    Set<String> expOperators = new HashSet<>(Arrays.asList("+=", "-=", "*=", "/=", "%="));

    for (int i = 0; i < token.length; i++) {
      if (array[i].equals("for")) {
        token[i] = "for";
      } else if (array[i].equals("System.out.println") || array[i].equals("System.out.print")) {
        token[i] = "print";
      } else if (array[i].equals("int")) {
        token[i] = "int";
      } else if (symbols.contains(array[i]) || expOperators.contains(array[i])) {
        token[i] = array[i];
      } else if (array[i].matches("-?\\d+")) {
        token[i] = "integer";
      } else if (array[i].matches("[a-zA-Z0-9_]+")) {
        token[i] = "varName";
      } else if (array[i].matches(".*[\"'].*")) {
        token[i] = "printContent";
      } else if (array[i].startsWith("++")
          || array[i].startsWith("--") && array[i].length() > 2 && Character.isLetter(array[i].charAt(2)) ||
          (array[i].length() > 2 && Character.isLetter(array[i].charAt(0)) &&
              (array[i].endsWith("--") || array[i].endsWith("++")))) {
        token[i] = "update";
      } else {
        token[i] = "illegal";
      }
    }
    return token;
  }

  public static boolean checkStatement(String[] token, int start, int end) {
    start += 1;
    // Operators
    Set<String> expOperators = new HashSet<>(Arrays.asList("+=", "-=", "*=", "/=", "%="));

    ArrayList<String> line = new ArrayList<>();
    int i = start; // Initialize the iterator to the start index

    boolean validConditionMet = false;

    while (i < token.length) {
      String currentToken = token[i];
      line.add(currentToken); // Add the current token to the ArrayList

      if (currentToken.equals(";") && !line.isEmpty()) {
        if (line.get(0).equals("int") || (line.get(0).equals("varName") && line.get(1).equals("="))) {
          System.out.println("\nCalling checkVarDeclare with start: " + start + " and end: " + i);
          System.out.println("Passing: " + String.join(" ", line));
          System.out.println("Call varDeclare");
          if (checkVarDeclare(token, start, i)) {
            validConditionMet = true;
          }
        } else if (line.get(0).equals("update")) {
          System.out.println("\nCall update");
          validConditionMet = true;
        } else if (line.get(0).equals("print")) {
          System.out.println("\nCall print");
          validConditionMet = true;
        } else if (line.get(0).equals("varName") && expOperators.contains(line.get(1))) {
          System.out.println("\nCall expression");
          validConditionMet = true;
        }
        // Update the start index to the index following the semicolon
        start = i + 1;
        // Clear the ArrayList for the next line
        line.clear();

        if (!validConditionMet) {
          System.out.println("Illegal statement");
        }
        validConditionMet = false;
      }
      i++; // Move to the next token
    }
    return false;
  }

  public static boolean checkVarDeclare(String[] token, int start, int end) {
    if (token[start].equals("int")) {
      for (int i = start; i < end; i++) {
        if (token[i + 1].equals("varName") &&
            token[i + 2].equals("=") &&
            (token[i + 3].equals("varName")
                || token[i + 3].equals("integer"))
            &&
            token[i + 4].equals(";")) {
          System.out.println("Checking: " + Arrays.toString(Arrays.copyOfRange(token, start, end)));
          System.out.println("Correct variable declaration");
          return true;
        }
      }
    } else if (token[start].equals("varName")) {
      for (int i = start; i < end; i++) {
        if (token[i + 1].equals("=") &&
            (token[i + 2].equals("varName")
                || token[i + 2].equals("integer"))
            &&
            token[i + 3].equals(";")) {
          System.out.println("Checking: " + Arrays.toString(Arrays.copyOfRange(token, start, end)));
          System.out.println("Correct variable declaration");
          return true;
        }
      }
    }
    System.out.println("Incorrect variable declaration");
    return false;
  }
}