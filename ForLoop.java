import java.util.*;

// SAMPLE: for ( int i=0 ; i<length ; ++i ) { int x , y , x ; int abc=0 ; a+=0 ; b-=1 ; ++g ; g-- ; --g ; g++ ; System.out.print ( 'hello' ) ; }

// SAMPLE2: for ( int i = 0 ; i < length ; ++i ) { int x , y , x ; int abc = 0 ; a += 0 ; b -= 1 ; ++g ; g-- ; --g ; g++ ; System.out.print ( "hello" ) ; }

// SAMPLE3: for ( int i = 0 ; i < length ; ++i ) { int x , y , x ; int var_name = 0 ; a += 0 ; b -= 1 ; ++g ; g-- ; --g ; g++ ; System.out.print ( "hello" ) ; }

public class ForLoop {

  // Symbols
  static Set<String> separatorSymbols = new HashSet<>(
      Arrays.asList("=", "(", ")", "{", "}", ",", ";"));
  // Boolean Symbols
  static Set<String> booleanSymbols = new HashSet<>(Arrays.asList("<", ">", "<=", "=>", "!=", "=="));
  // Operators
  static Set<String> expOperators = new HashSet<>(Arrays.asList("+=", "-=", "*=", "/=", "%="));

  public static void main(String[] args) {
    // Read syntax input
    Scanner scan = new Scanner(System.in);
    System.out.print("Enter syntax: ");
    String syntaxInput = scan.nextLine();

    // Split the input into an array and pass to tokenizer()
    String[] arrayInput = syntaxInput.trim().split("\\s+");
    String[] token = tokenizer(arrayInput);

    System.out.println("\n" + Arrays.toString(token));

    System.out.println("\nChecking sequence...");
    if (checkSequence(token)) {
      // Check Control Statement ()
      int startControl = -1;
      int endControl = -1;

      for (int i = 0; i < token.length; i++) {
        if (token[i].equals("(") && startControl == -1) {
          startControl = i;
        } else if (token[i].equals(")") && endControl == -1) {
          endControl = i;
        }
      }

      System.out.println("\nChecking control statements...");
      checkControl(token, startControl, endControl);

      // Check For Loop Statement {}
      int startStatement = 0, endStatement = 0;

      for (int i = 0; i < token.length; i++) {
        if (token[i].equals("{")) {
          startStatement = i;
        } else if (token[i].equals("}")) {
          endStatement = i;
        }
      }

      System.out.println("\nChecking statements...");
      checkStatement(token, startStatement, endStatement);
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

        for (int j = 0; i < equalArray.length; j++) {
          if (equalArray[j].matches("-?\\d+")) {
            tokenList.add("integer");
          } else if (equalArray[j].matches("[a-zA-Z0-9_]+")) {
            tokenList.add("varName");
          } else if (expOperators.contains(equalArray[j])) {
            tokenList.add(equalArray[j]);
          } else if ("=".equals(equalArray[j])) {
            tokenList.add("=");
          }
        }
      } else {
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
      System.out.println("Incorrect for () {} Sequence");
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

    if (controlContent.isEmpty()) {
      System.out.println("Control statement is empty");
    } else if (controlContent.size() < 3) {
      System.out.println("Control statement has less than 3 statements");
    } else if (controlContent.size() == 3) {
      if (controlContent.get(0).equals("varDeclare") &&
          controlContent.get(1).equals("condition") &&
          controlContent.get(2).equals("update")) {
        System.out.println("Correct control statements");
        validConditionMet = true;
      }
    } else if (controlContent.size() > 3) {
      System.out.println("Control statement has more than 3 statements");
    }

    if (!validConditionMet) {
      System.out.println("Illegal control statement");
    }

    return validConditionMet;
  }

  // checkStatement(): Validates if for loop statements are correct
  // It should accept varDeclare, update, print, exp
  public static boolean checkStatement(String[] token, int start, int end) {
    start += 1; // Start checking tokens after "{"

    ArrayList<String> line = new ArrayList<>();
    int i = start;
    boolean validConditionMet = false;

    while (i < end) {
      String currentToken = token[i];
      line.add(currentToken);

      if (currentToken.equals(";") && !line.isEmpty()) {
        if (line.get(0).equals("int")
            || (line.get(0).equals("varName") && line.size() >= 3 && line.get(1).equals("="))) {
          if (checkVarDeclare(token, start, i)) {
            validConditionMet = true;
          }
        } else if (line.get(0).equals("update") && line.get(line.size() - 1).contains(";")) {
          if (checkUpdate(token, start, i)) {
            validConditionMet = true;
          }
        } else if (line.get(0).equals("print")) {
          if (checkPrint(token, start, i)) {
            validConditionMet = true;
          }
        } else if (line.get(0).equals("varName") && expOperators.contains(line.get(1))) {
          if (checkExpression(token, start, i)) {
            validConditionMet = true;
          }
        }
        System.out.println(line);
        start = i + 1;
        line.clear();
      }
      i++;
    }

    if (!validConditionMet) {
      System.out.println("Illegal statement");
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
              System.out.println("Correct Identifier List Declaration");
              return true;
            } else {
              System.out.println("Incorrect Identifier List Declaration");
              return false;
            }
          } else {
            // Invalid ending, return false
            System.out.println("Incorrect Identifier List Declaration");
            return false;
          }
        } else {
          System.out.println("Incorrect Identifier List Declaration");
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
            System.out.println("Correct Variable Declaration");
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
            System.out.println("Correct Variable Declaration");
            return true;
          }
        }
      }
    }

    System.out.println("Incorrect variable declaration");
    return false;
  }

  // checkUpdate(): Validates if update has the correct syntax
  public static boolean checkUpdate(String[] token, int start, int end) {
    for (int i = start; i <= end - 1; i++) {
      if (token[i].equals("update")) {
        System.out.println("Correct Update");
        return true;
      }
    }
    System.out.println("Incorrect Update");
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
      System.out.println("Correct Condition");
      return true;
    } else {
      System.out.println("Inorrect Condition");
      return false;
    }

  }

  // checkExpression(): Validates if expression has the correct syntax
  // It accepts <varName> <expOperator> <digit>||<char> [;]
  public static boolean checkExpression(String[] token, int start, int end) {
    Set<String> expOp = new HashSet<>(Arrays.asList("+=", "-=", "*=", "/=", "%="));

    for (int i = start; i < end; i++) {
      if (token[i].equals("varName") && expOp.contains(token[i + 1]) &&
          (token[i + 2].equals("integer") || token[i + 2].equals("varName")) && token[i + 3].equals(";")) {
        System.out.println("Correct Expression");
        return true;
      }
    }
    System.out.println("Incorrect Expression");
    return false;
  }

  // checkPrint(): Validates if sysout print has the correct syntax
  // It accepts <print> ( <printContent>||<varName> ) ;
  public static boolean checkPrint(String[] token, int start, int end) {
    for (int i = start; i <= end - 1; i++) {
      if (token[i].equals("print") &&
          token[i + 1].equals("(") &&
          token[i + 2].equals("printContent") &&
          token[i + 3].equals(")") &&
          token[i + 4].equals(";")) {
        System.out.println("Correct Print Statement");
        return true;
      }
    }
    System.out.println("Incorrect Print Statement");
    return false;
  }
}
