import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class OldForLoop {
  public static void main(String[] args) {
    /*
     * for (int i = 0; i < 5; i += 2) {
     * System.out.println(i);
     * System.out.println("Hello World");
     * System.out.println("Hello World");
     * }
     */
    /*
     * String a =
     * "for ( int i=1 ; i < length ; ++i ) { a+=0 0 ; ++g ; g-- ; --g ; g++ ; System.out.print ( 'hello' ) }"
     * ;
     * String[] b= a.split(" ");
     * System.out.println(Arrays.toString(b));
     * String[] test=tokenizer(a);
     * System.out.println(Arrays.toString(test));
     */

    String[] a = { "update", "'" };
    System.out.println(Arrays.toString(a));
    System.out.println("boolean: " + isCorrectUpdate(a, 0, 2));

  }

  // natalia
  public static String[] tokenizer(String a) {
    // Symbols
    Set<String> symbols = new HashSet<>(Arrays.asList("(", ")", "{", "}", ";", "<", ">", "<=", "=>", "!=", "=", "=="));
    // Operators
    Set<String> expOperators = new HashSet<>(Arrays.asList("+=", "-=", "*=", "/=", "%="));

    a.trim();
    a.replace(" ", "");
    String[] b = a.split(" ");
    for (int i = 0; i < b.length; i++) {

      if (b[i].contains("=") && Character.isAlphabetic(b[i].charAt(0))
          && b[i].substring(b[i].indexOf("=") + 1).matches("-?\\d+")) {
        b[i] = "varDeclare";
      } else if (b[i].equals("System.out.print") || b[i].equals("System.out.println")) {
        b[i] = "print";
      } else if ((b[i].equals("for") ||
          b[i].equals("int") ||
          b[i].equals("(") ||
          b[i].equals(")") ||
          b[i].equals("{") ||
          b[i].equals("}") ||
          b[i].equals("+") ||
          b[i].equals("-") ||
          b[i].equals("*") ||
          b[i].equals("/") ||
          b[i].equals("%") ||
          b[i].equals("<") ||
          b[i].equals(">") ||
          b[i].equals("<=") ||
          b[i].equals("=>") ||
          b[i].equals("!=") ||
          b[i].equals("=") ||
          b[i].equals("==") ||
          b[i].equals(";"))) {
        b[i] = b[i];
      } else if ((Character.toString(b[i].charAt(0)).equals("+")
          && Character.toString(b[i].charAt(1)).equals("+") && b[i].length() > 2 && Character.isLetter(b[i].charAt(2)))
          ||
          (Character.toString(b[i].charAt(0)).equals("-") && Character.toString(b[i].charAt(1)).equals("-")
              && b[i].length() > 2 && Character.isLetter(b[i].charAt(2)))
          ||
          (b[i].length() > 2 && Character.isLetter(b[i].charAt(0))
              && Character.toString(b[i].charAt(b[i].length() - 2)).equals("-")
              && Character.toString(b[i].charAt(b[i].length() - 1)).equals("-"))
          ||
          (b[i].length() > 2 && Character.isLetter(b[i].charAt(0))
              && Character.toString(b[i].charAt(b[i].length() - 2)).equals("+")
              && Character.toString(b[i].charAt(b[i].length() - 1)).equals("+"))) {
        b[i] = "update";
      } else if (expOperators.contains(b[i]) &&
          b[i].length() > 3 &&
          Character.isLetter(b[i].charAt(0)) &&
          ((Character.isAlphabetic(b[i].charAt(b[i].indexOf("+") + 2))) ||
              (Character.isAlphabetic(b[i].charAt(b[i].indexOf("-") + 2))) ||
              (Character.isAlphabetic(b[i].charAt(b[i].indexOf("/") + 2))) ||
              (Character.isAlphabetic(b[i].charAt(b[i].indexOf("*") + 2))) ||
              (Character.isAlphabetic(b[i].charAt(b[i].indexOf("%") + 2))))
          ||
          (b[i].substring(b[i].indexOf("+") + 2).matches("-?\\d+") ||
              b[i].substring(b[i].indexOf("-") + 2).matches("-?\\d+") ||
              b[i].substring(b[i].indexOf("/") + 2).matches("-?\\d+") ||
              b[i].substring(b[i].indexOf("*") + 2).matches("-?\\d+") ||
              b[i].substring(b[i].indexOf("%") + 2).matches("-?\\d+"))) {
        b[i] = "expr";
      } else if (b[i].matches("-?\\d+")) { // ((b[i].charAt(0)=='-' &&
                                           // (Character.isAlphabetic(b[i].charAt(1)))||(Character.isDigit(b[i].charAt(0))
                                           // && !()))){
        b[i] = "integer";
      } else if (b[i].matches("[a-zA-Z0-9_]+")) { // ((b[i] instanceof String && b[i])|| b[i].length() == 1) {
        b[i] = "stringVar";
      } else {
        b[i] = "unknown";
      }

    }
    return b;
  }

  // this is the final product
  public static boolean isCorrectPrint(String[] token, int start, int end) {
    for (int i = start; i <= end - 1; i++) {
      if (token[i].equals("print") &&
          token[i + 1].equals("(") &&
          token[i + 2].equals("printContent") &&
          token[i + 3].equals(")") &&
          token[i + 4].equals(";")) {
        return true;
      }
    }
    System.out.println("Incorrect System.out.print statement");
    return false;
  }

  // this is the final product
  public static boolean isCorrectUpdate(String[] token, int start, int end) {
    for (int i = start; i <= end - 1; i++) {
      if (token[i].equals("update") && token[i + 1].equals(";")) {
        return true;
      }
    }
    System.out.println("Incorrect update");
    return false;
  }
}
