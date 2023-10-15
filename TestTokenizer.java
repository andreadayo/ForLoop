import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

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
      Character.isLetter(array[i].charAt(0)) &&
      ((Character.isAlphabetic(array[i].charAt(array[i].indexOf("+")+2)))||
      (Character.isAlphabetic(array[i].charAt(array[i].indexOf("-")+2)))||
      (Character.isAlphabetic(array[i].charAt(array[i].indexOf("/")+2)))||
      (Character.isAlphabetic(array[i].charAt(array[i].indexOf("*")+2)))||
      (Character.isAlphabetic(array[i].charAt(array[i].indexOf("%")+2))))||
      (array[i].substring(array[i].indexOf("+")+2).matches("-?\\d+")||
      array[i].substring(array[i].indexOf("-")+2).matches("-?\\d+")||
      array[i].substring(array[i].indexOf("/")+2).matches("-?\\d+")||
      array[i].substring(array[i].indexOf("*")+2).matches("-?\\d+")||
      array[i].substring(array[i].indexOf("%")+2).matches("-?\\d+")) 
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

}