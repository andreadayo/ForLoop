import java.util.*;

public class ForLoopSyntaxChecker {

  // Symbols
  static Set<String> separatorSymbols = new HashSet<>(
      Arrays.asList("=", "(", ")", "{", "}", ",", ";"));
  // Boolean Symbols
  static Set<String> booleanSymbols = new HashSet<>(Arrays.asList("<", ">", "<=", "=>", "!=", "=="));
  // Operators
  static Set<String> expOperators = new HashSet<>(Arrays.asList("+=", "-=", "*=", "/=", "%="));
  // Error Counter
  static int errorCounter = 0;

  public static void main(String[] args) {
    // Read syntax input
    Scanner scan = new Scanner(System.in);
    System.out.print("Enter syntax: ");
    String syntaxInput = scan.nextLine();

    // Split the input into an array and pass to tokenizer()
    String[] arrayInput = syntaxInput.trim().split("\\s+");
    String[] token = tokenizer(arrayInput);

    // Count how many "for" exists and store its index
    int tokenCounter = 0;
    ArrayList<Integer> forIndex = new ArrayList<>();
    for (String word : token) {
      if (word.equals("for")) {
        forIndex.add(tokenCounter);
      }
      tokenCounter++;
    }

    // Ensure forIndex is not empty
    if (!forIndex.isEmpty()) {
      for (int i = forIndex.size() - 1; i >= 0; i--) {
        int forStartIndex = forIndex.get(i);
        int forEndIndex = -1; // Initialize the end index to -1

        // Find the corresponding closing "}" for the current "for"
        int openBraces = 0;
        for (int j = forStartIndex; j < token.length; j++) {
          if (token[j].equals("{")) {
            openBraces++;
          } else if (token[j].equals("}")) {
            openBraces--;
            if (openBraces == 0) {
              forEndIndex = j;
              break; // Found the matching closing brace
            }
          }
        }

        // Check the syntax of the "for" loop
        if (forEndIndex != -1) {
          System.out.println("\nChecking sequence...");

          if (checkSequence(token)) {
            // Check Control Statement ()
            int startControl = -1;
            int endControl = -1;

            for (int n = forStartIndex; n <= forEndIndex; n++) {
              if (token[n].equals("(") && startControl == -1) {
                startControl = n;
              } else if (token[n].equals(")") && endControl == -1) {
                endControl = n;
              }
            }

            System.out.println("\nChecking control statements...");
            checkControl(token, startControl, endControl);

            // Check For Loop Statement {}
            int startStatement = forStartIndex;
            int endStatement = forEndIndex;

            for (int j = forStartIndex; j <= forEndIndex; j++) {
              if (token[j].equals("{")) {
                startStatement = j;
              } else if (token[j].equals("}")) {
                endStatement = j;
              }
            }

            System.out.println("\nChecking statements...");
            checkStatement(token, startStatement, endStatement);
          }

          boolean isSyntaxValid = true;
          if (errorCounter > 0) {
            isSyntaxValid = false;
          }

          if (isSyntaxValid) {
            System.out.println("\n== Syntax of 'for' loop at index " + forStartIndex + " is VALID.");
          } else {
            System.err.println("\n== Syntax of 'for' loop at index " + forStartIndex + " is INVALID.");
            break;
          }
        } else {
          System.err.println(" Missing closing '}' for 'for' loop at index " + forStartIndex);
          break;
        }
      }
    }
  }

  // tokenizer(): Stores the array in a token with its corresponding label
  public static String[] tokenizer(String[] array) {
    ArrayList<String> tokenList = new ArrayList<>();

    for (int i = 0; i < array.length; i++) {
      if (array[i].equals("for")) {
        tokenList.add("for");
      } else if (array[i].equals("System.out.println") || array[i].equals("System.out.print")) {
        tokenList.add("print");
      } else if (array[i].equals("int")) {
        tokenList.add("int");
      } else if (((array[i].startsWith("++")
          || array[i].startsWith("--")) && array[i].length() > 2 && Character.isLetter(array[i].charAt(2))) ||
          (array[i].length() > 2 && Character.isLetter(array[i].charAt(0)) &&
              ((array[i].endsWith("--") || array[i].endsWith("++"))))) {
        tokenList.add("update");
      } else if (separatorSymbols.contains(array[i]) || booleanSymbols.contains(array[i])
          || expOperators.contains(array[i])) {
        tokenList.add(array[i]);
      } else if (array[i].matches("-?\\d+")) {
        tokenList.add("integer");
      } else if (array[i].matches("[a-zA-Z0-9_]+")) {
        tokenList.add("varName");
      } else if (array[i].matches(".*[\"'].*")) {
        tokenList.add("printContent");
      } else if (expOperators.stream().anyMatch(array[i]::contains) && array[i].length() >= 3) {
        String pattern = String.join("|", expOperators)
            .replaceAll("\\+", "\\[+\\]")
            .replaceAll("\\*", "\\[\\*\\]");
        String[] exprArray = array[i].split("(?=" + pattern + ")|(?<=" + pattern + ")");

        for (int j = 0; j < exprArray.length; j++) {
          if (exprArray[j].matches("-?\\d+")) {
            tokenList.add("integer");
          } else if (exprArray[j].matches("[a-zA-Z0-9_]+")) {
            tokenList.add("varName");
          } else if (expOperators.contains(exprArray[j])) {
            tokenList.add(exprArray[j]);
          }
        }
      } else if (booleanSymbols.stream().anyMatch(array[i]::contains) && array[i].length() >= 3) {
        String pattern = String.join("|", booleanSymbols);
        String[] boolArray = array[i].split("(?=" + pattern + ")|(?<=" + pattern + ")");

        for (int j = 0; j < boolArray.length; j++) {
          if (boolArray[j].matches("-?\\d+")) {
            tokenList.add("integer");
          } else if (boolArray[j].matches("[a-zA-Z0-9_]+")) {
            tokenList.add("varName");
          } else if (booleanSymbols.contains(boolArray[j])) {
            tokenList.add(boolArray[j]);
          }
        }
      } else if (array[i].contains("=") && array[i].length() >= 3) {
        String[] equalArray = array[i].split("(?<=\\=)|(?=\\=)");

        for (int j = 0; j < equalArray.length; j++) {
          if (equalArray[j].matches("-?\\d+")) {
            tokenList.add("integer");
          } else if (equalArray[j].matches("[a-zA-Z0-9_]+")) {
            tokenList.add("varName");
          } else if (equalArray[j].equals("=")) {
            tokenList.add("=");
          } else {
            tokenList.add(equalArray[j]);
          }
        }
      } else {
        errorCounter++;
        tokenList.add("illegal");
      }
    }

    return tokenList.toArray(new String[0]);

  }

  // checkSequence(): Validate if separators () or {} are balanced & if it follows
  // the proper for loop syntax
  public static boolean checkSequence(String[] token) {
    boolean beginningCorrect = false, middleCorrect = true, endCorrect = false, isBalanced = true;
    boolean firstBraceCheckDone = false, openBraceCheckDone = false;
    Stack<String> pairStack = new Stack<String>();

    // check for parenthesis balance
    for (int i = 0; i < token.length; i++) {
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

    // check for any leftovers
    if (!pairStack.empty()) {
      isBalanced = false;
    }

    // check for curly brace balance
    for (int i = 0; i < token.length; i++) {
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

    // check for any leftovers again
    if (!pairStack.empty()) {
      isBalanced = false;
    }

    // checking the actual sequence
    // check beginning
    if (token[0].equals("for") && token[1].equals("(")) {
      beginningCorrect = true;
    }

    // check middle
    for (int i = 0; i < token.length; ++i) {
      if (token[i].equals("(")) {
        pairStack.push(token[i]);
      }

      if (token[i].equals(")")) {
        if (!pairStack.isEmpty()) {
          pairStack.pop();
        }
      }

      // checks "){"
      if (i > 0 && pairStack.isEmpty() && !openBraceCheckDone) {
        if (i < token.length - 1) {
          if (!token[i + 1].equals("{")) {
            middleCorrect = false;
          }
        } else {
          middleCorrect = false;
        }

        openBraceCheckDone = true;
      }

      // makes sure { is ONLY after ) and only triggers at the first )
      if (token[i].equals("{") && !firstBraceCheckDone) {
        if (!pairStack.isEmpty()) {
          middleCorrect = false;
        }
        firstBraceCheckDone = true;
      }
    }

    // check end
    if (token[token.length - 1].equals("}")) {
      endCorrect = true;
    }

    if (isBalanced && beginningCorrect && middleCorrect && endCorrect) {
      System.out.println("Correct for () {} Sequence");
      return true;
    } else {
      System.err.println("Incorrect for () {} Sequence");
      errorCounter++;
      return false;
    }
  }

  // checkControl(): Validates if the control statements inside for() are correct
  public static boolean checkControl(String[] token, int start, int end) {
    start++; // Start checking tokens after "("

    boolean validConditionMet = false;
    ArrayList<String> line = new ArrayList<>();
    ArrayList<String> controlContent = new ArrayList<>();

    for (int i = start; i <= end; i++) {
      String currentToken = token[i];
      line.add(currentToken);

      if (i == end || currentToken.equals(";")) {
        String content = "";
        if (line.get(0).equals("int")
            || (line.get(0).equals("varName") && line.size() >= 3 && line.get(1).equals("="))) {
          if (checkVarDeclare(token, start, i)) {
            content = "varDeclare";
          }
        } else if (line.get(0).equals("update")) {
          if (checkUpdate(token, start, i)) {
            content = "update";
          }
        } else if (line.get(0).equals("varName") && expOperators.contains(line.get(1))) {
          if (checkExpression(token, start, i)) {
            content = "expr";
          }
        } else if (line.get(0).equals("varName") && booleanSymbols.contains(line.get(1))) {
          if (checkCondition(token, start, i)) {
            content = "condition";
          }
        }

        if (!content.isEmpty()) {
          controlContent.add(content);
        }

        start = i + 1;
        line.clear();
      }
    }

    if (controlContent.size() == 3) {
      int controlCounter = 0;
      if (controlContent.get(0).equals("varDeclare")) {
        controlCounter++;
        System.out.println("Correct for loop initialization");
      } else {
        System.err.println("Incorrect for loop initialization");
      }
      if (controlContent.get(1).equals("condition")) {
        controlCounter++;
        System.out.println("Correct for loop condition");
      } else {
        System.err.println("Incorrect for loop condition");
      }
      if (controlContent.get(2).equals("update") || controlContent.get(2).equals("expr")) {
        controlCounter++;
        System.out.println("Correct for loop update");
      } else {
        System.err.println("Incorrect for loop update");
      }
      if (controlCounter == 3) {
        validConditionMet = true;
        System.out.println("Correct control statements");
      }
    }
    if (controlContent.isEmpty()) {
      errorCounter++;
      System.err.println("Control statement is empty");
    } else if (controlContent.size() < 3) {
      errorCounter++;
      System.err.println("Control statement has less than 3 correct statements");
    } else if (controlContent.size() > 3) {
      errorCounter++;
      System.err.println("Control statement has more than 3 correct statements");
    }

    if (!validConditionMet) {
      errorCounter++;
      System.err.println("Illegal control statement");
    }

    return validConditionMet;
  }

  // checkStatement(): Validates if for loop statements are correct
  // It should accept varDeclare, update, print, exp
  public static boolean checkStatement(String[] token, int start, int end) {
    start += 1; // Start checking tokens after "{"

    ArrayList<String> line = new ArrayList<>();
    int i = start;
    boolean validConditionMet = true;

    while (i < end) {
      String currentToken = token[i];
      line.add(currentToken);

      if (currentToken.equals(";") || currentToken.equals("}") || i == end - 1) {
        if (!line.isEmpty()) {
          if (line.get(0).equals("int")
              || (line.get(0).equals("varName") && line.size() >= 3 && line.get(1).equals("="))) {
            if (checkVarDeclare(token, start, i)) {
              System.out.println("Correct variable declaration");
            } else {
              System.err.println("Incorrect variable declaration");
            }
          } else if ((line.get(0).equals("update") || line.get(0).equals("expr"))
              && line.get(line.size() - 1).contains(";")) {
            if (checkUpdate(token, start, i)) {
              System.out.println("Correct update");
            } else {
              System.err.println("Incorrect update");
            }
          } else if (line.get(0).equals("print")) {
            if (checkPrint(token, start, i)) {
              System.out.println("Correct print statement");
            } else {
              System.err.println("Incorrect print statement");
            }
          } else if (line.get(0).equals("varName") && expOperators.contains(line.get(1))
              && line.get(line.size() - 1).contains(";")) {
            if (checkExpression(token, start, i)) {
              System.out.println("Correct expression");
            } else {
              System.err.println("Incorrect expression");
            }
          } else if (line.get(0).equals("}")) {
            System.out.println("Nested for loop found");
          } else {
            System.err.println("Statement not found");
            validConditionMet = false;
            errorCounter++;
          }
          start = i + 1;
          line.clear();
        }
      }
      i++;
    }

    if (!validConditionMet) {
      errorCounter++;
      System.err.println("Illegal statement");
    }

    return validConditionMet;
  }

  // checkVarDeclare(): Validates if the variable declaration is correct
  // It accepts [int] <varName> = <varName>||<integer> ;
  public static boolean checkVarDeclare(String[] token, int start, int end) {
    // Identifier List
    if (end - start > 4 && token[start].equals("int")) {
      int i = start + 1;
      while (i < end) {
        if (token[i].equals("varName")) {
          i++;

          if (i < end + 1) {
            if (token[i].equals(",")) {
              i++;
            } else if (token[i].equals(";")) {
              // System.out.println("Correct Identifier List Declaration");
              return true;
            } else {
              // System.err.println("Incorrect Identifier List Declaration");
              errorCounter++;
              return false;
            }
          } else {
            // Invalid ending, return false
            // System.err.println("Incorrect Identifier List Declaration");
            errorCounter++;
            return false;
          }
        } else {
          System.err.println("Incorrect Identifier List Declaration");
          errorCounter++;
          return false;
        }
      }
    }

    // Variable Declaration
    else {
      if (token[start].equals("int")) {
        for (int i = start; i < end; i++) {
          if (token[i + 1].equals("varName") &&
              token[i + 2].equals("=") &&
              (token[i + 3].equals("varName")
                  || token[i + 3].equals("integer"))
              &&
              token[i + 4].equals(";")) {
            // System.out.println("Correct Variable Declaration with Int Data Type");
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
            // System.out.println("Correct Variable Declaration");
            return true;
          }
        }
      }
    }

    // System.err.println("Incorrect variable declaration");
    errorCounter++;
    return false;
  }

  // checkUpdate(): Validates if update has the correct syntax
  public static boolean checkUpdate(String[] token, int start, int end) {
    for (int i = start; i <= end - 1; i++) {
      if (token[i].equals("update")) {
        // System.out.println("Correct Update");
        return true;
      }
    }
    // System.err.println("Incorrect Update");
    errorCounter++;
    return false;
  }

  // checkCondition(): Validates if condition has the correct syntax
  // It accepts <varName> <comparisonSymbol> <varName>||<integer>
  public static boolean checkCondition(String[] token, int start, int end) {
    int startIndex = start;

    // finds condition
    for (int i = 0; i < token.length; i++) {
      if (token[i].equals(";")) {
        startIndex = i + 1;
        break;
      }
    }
    // make sure that the left side is a variable or an integer
    if ((token[startIndex].equals("varName")) &&
    // make sure that it's followed by a comparison symbol
        (booleanSymbols.contains(token[startIndex + 1])) &&
        // make sure that the right side is either a variable or an integer
        (token[startIndex + 2].equals("varName") || token[startIndex + 2].equals("integer"))) {
      // System.out.println("Correct Condition");
      return true;
    } else {
      // System.err.println("Incorrect Condition");
      return false;
    }

  }

  // checkExpression(): Validates if expression has the correct syntax
  // It accepts <varName> <expOperator> <digit>||<char> [;]
  public static boolean checkExpression(String[] token, int start, int end) {
    Set<String> expOp = new HashSet<>(Arrays.asList("+=", "-=", "*=", "/=", "%="));

    for (int i = start; i < end; i++) {
      if (token[i].equals("varName") && expOp.contains(token[i + 1]) &&
          (token[i + 2].equals("integer") || token[i + 2].equals("varName"))) {
        // System.out.println("Correct Expression"); // && token[i + 3].equals(";")
        return true;
      }
    }
    // System.err.println("Incorrect Expression");
    errorCounter++;
    return false;
  }

  // checkPrint(): Validates if sysout print has the correct syntax
  // It accepts <print> ( <printContent>||<varName> ) ;
  public static boolean checkPrint(String[] token, int start, int end) {
    for (int i = start; i <= end - 1; i++) {
      if (token[i].equals("print") &&
          token[i + 1].equals("(") &&
          (token[i + 2].equals("printContent") || token[i + 2].equals("varName")) &&
          token[i + 3].equals(")") &&
          token[i + 4].equals(";")) {
        // System.out.println("Correct Print Statement");
        return true;
      }
    }
    // System.err.println("Incorrect Print Statement");
    errorCounter++;
    return false;
  }
}