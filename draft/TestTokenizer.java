import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

// SAMPLE: for ( int i = 1 ; i < length ; ++i ) { a += 0 ; ++g ; g-- ; --g ; g++ ; System.out.print ( 'hello' ) }

// OUTPUT: [for, (, int, varName, =, integer, ;, varName, <, varName, ;, update, ), {, varName, +=, integer, ;, update, ;, update, ;, update, ;, update, ;, print, (, printContent, ), }]

public class TestTokenizer {
  public static void main(String[] args) {
    // Read syntax input
    Scanner scan = new Scanner(System.in);
    System.out.print("Enter syntax: ");
    String syntaxInput = scan.nextLine();

    // Split the input into an array and pass to tokenizer()
    String[] arrayInput = syntaxInput.trim().split(" ");
    String[] token = tokenizer(arrayInput);
    System.out.println("\n" + Arrays.toString(token));
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
      }else if (((array[i].startsWith("++")
          || array[i].startsWith("--")) && array[i].length() > 2 && Character.isLetter(array[i].charAt(2))) ||
          (array[i].length() > 2 && Character.isLetter(array[i].charAt(0)) &&
              ((array[i].endsWith("--") || array[i].endsWith("++"))))) {
        token[i] = "update";
      }else if(expOperators.contains(array[i])&&
      array[i].length()>3 &&
      Character.isAlphabetic(array[i].charAt(0)) &&
      (((Character.isAlphabetic(array[i].charAt(array[i].indexOf("+")+2)))||
      (Character.isAlphabetic(array[i].charAt(array[i].indexOf("-")+2)))||
      (Character.isAlphabetic(array[i].charAt(array[i].indexOf("/")+2)))||
      (Character.isAlphabetic(array[i].charAt(array[i].indexOf("*")+2)))||
      (Character.isAlphabetic(array[i].charAt(array[i].indexOf("%")+2))))||
      (array[i].substring(array[i].indexOf("+")+2).matches("-?\\d+")||
      array[i].substring(array[i].indexOf("-")+2).matches("-?\\d+")||
      array[i].substring(array[i].indexOf("/")+2).matches("-?\\d+")||
      array[i].substring(array[i].indexOf("*")+2).matches("-?\\d+")||
      array[i].substring(array[i].indexOf("%")+2).matches("-?\\d+"))) 
      ){    
      token[i]="expr"; 
      }else if(array[i].contains("=") && 
      !expOperators.contains(array[i]) && 
      Character.isAlphabetic(array[i].charAt(0)) && 
      array[i].substring(array[i].indexOf("=")+1).matches("-?\\d+")
      ){
      token[i]="varDeclare";
      }else if (symbols.contains(array[i]) || expOperators.contains(array[i])) {
        token[i] = array[i];
      } else if (array[i].matches("-?\\d+")) {
        token[i] = "integer";
      } else if (array[i].matches("[a-zA-Z0-9_]+")) {
        token[i] = "varName";
      } else if (array[i].matches(".*[\"'].*")) {
        token[i] = "printContent";
      } else {
        token[i] = "illegal";
      }
    }
    return token;
  }

  //check sequence
  public static boolean sequenceCorrect(String[] token) {
    boolean beginningCorrect = false, middleCorrect = true, endCorrect = false, isBalanced = true;
    boolean firstBraceCheckDone = false, openBraceCheckDone = false;
    Stack pairStack = new Stack<String>();

    //check for parenthesis balance
    for (int i = 0; i<token.length; i++) {
      if (token[i].equals("(")) {
        pairStack.push(token[i]);
      }

      if (token[i].equals(")")) {
        if (pairStack.empty()) {
          isBalanced = false;
        } else {
          pairStack.pop();
        }
      }
    }

    //check for any leftovers
    if (!pairStack.empty()) {
      isBalanced = false;
    }

    //check for curly brace balance
    for (int i = 0; i<token.length; i++) {
      if (token[i].equals("{")) {
        pairStack.push(token[i]);
      }

      if (token[i].equals("}")) {
        if (pairStack.empty()) {
          isBalanced = false;
            } else {
              pairStack.pop();
            }
          }
    }

    //check for any leftovers again
    if (!pairStack.empty()) {
      isBalanced = false;
    }

    //checking the actual sequence
    //check beginning
    if (token[0].equals("for") && token[1].equals("(")) {
      beginningCorrect = true;
    }

    //check middle
    for (int i=0; i<token.length; ++i) {
      if (token[i].equals("(")) {
        pairStack.push(token[i]);
      }

      if (token[i].equals(")")) {
        if (!pairStack.isEmpty()) {
          pairStack.pop();
        }
      }

      //checks "){"
      if (i > 0 && pairStack.isEmpty() && !openBraceCheckDone) {
        if (i < token.length-1) {
          if (!token[i+1].equals("{")) {
            middleCorrect = false;
          }
        } else {
          middleCorrect = false;
        }
        
        openBraceCheckDone = true;
      }

      //makes sure { is ONLY after ) and only triggers at the first )
      if (token[i].equals("{") && !firstBraceCheckDone) {
        if (!pairStack.isEmpty()) {
          middleCorrect = false;
        }
        firstBraceCheckDone = true;
      }

    }

    //check end
    if (token[token.length-1].equals("}")) {
      endCorrect = true;
    }

    if (isBalanced && beginningCorrect && middleCorrect && endCorrect) {
      System.out.println("Correct Sequence");
        return true;
        } else {
            System.out.println("Incorrect Sequence");
            return false;
        }
  }

  //check condition
  public static boolean conditionCorrect(String[] token) {
    int startIndex = 0;
    Set<String> comparisonSymbols = new HashSet<>(Arrays.asList("==", "!=", "<", "<=", ">", ">="));

    //finds condition
    for (int i = 0; i < token.length; i++) {
      if (token[i].equals(";")) {
        startIndex = i+1;
        break;
      }
    }

    //check condition <varName>||<integer> <comparisonSymbol> <varName>||<integer>
    //make sure that the left side is a variable or an integer
    if ((token[startIndex].equals("varName") || token[startIndex].equals("integer")) &&
      //make sure that it's followed by a comparison symbol
      (comparisonSymbols.contains(token[startIndex+1])) &&
        //make sure that the right side is either a variable or an integer
      (token[startIndex+2].equals("varName") || token[startIndex+2].equals("integer"))) {
        System.out.println("Correct Condition");
        return true;
    } else {
      System.out.println("Inorrect Condition");
      return false;
    }
    
  }

  //check expressions
  public static boolean exprCorrect(String[] token) {
    Set<String> expOp = new HashSet<>(Arrays.asList("+=", "-=", "*=", "/=", "%="));
    
    for (int i = 1; i < token.length; i++) {
      if (expOp.contains(token[i])) {
        if (token[i-1].equals("varName") && 
          (token[i+1].equals("integer") || token[i+1].equals("varName")) && token[i+2].equals(";")) {
            System.out.println("Correct Expression/s");
            return true;
          } else {
            System.out.println("Incorrect Expression/s");
            return false;
          }
      } else if (token[i].equals("expr")) {
        if (!token[i+1].equals(";")) {
          System.out.println("Incorrect Expression/s");
            return false;
        } else {
          System.out.println("Correct Expression/s");
        }
      }
    }
    return true;
  }
}
