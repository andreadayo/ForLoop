import java.util.Arrays;

public class ForLoop {
    public static void main(String[] args)
    {
        /*for (int i = 0; i < 5; i += 2) {
          System.out.println(i);
          System.out.println("Hello World");
          System.out.println("Hello World");
        }*/
        String a = "for ( int i=1 ; i < length ; ++i ) { a+=0 0 ; ++g ; g-- ; --g ; g++; System.out.print ( 'hello' ) }";
        String[] b= a.split(" "); 
        System.out.println(Arrays.toString(b));
        String[] test=tokenizer(a);
        System.out.println(Arrays.toString(test));
    }
    
    //natalia
    public static String[] tokenizer(String a){
      a.trim();
      a.replace(" ", "");
      String[] b= a.split(" "); 
      for(int i=0; i<b.length; i++){

        if(b[i].contains("=") && Character.isAlphabetic(b[i].charAt(b[i].indexOf("=")-1)) && Character.isDigit(b[i].charAt(b[i].length()-1))){
        b[i]="varDeclare";
        }
        else if(b[i].equals("System.out.print")||b[i].equals("System.out.println")){
          b[i]="print";
        }
        else if ((
        b[i].equals("for") ||
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
        }
        else if ((Character.toString(b[i].charAt(0)).equals("+") && Character.toString(b[i].charAt(1)).equals("+") && b[i].length() > 2 && Character.isLetter(b[i].charAt(2))) ||
        (Character.toString(b[i].charAt(0)).equals("-") && Character.toString(b[i].charAt(1)).equals("-") && b[i].length() > 2 && Character.isLetter(b[i].charAt(2))) ||
        (b[i].length() > 2 && Character.isLetter(b[i].charAt(0)) && Character.toString(b[i].charAt(b[i].length() - 2)).equals("-") && Character.toString(b[i].charAt(b[i].length() - 1)).equals("-")) ||
        (b[i].length() > 2 && Character.isLetter(b[i].charAt(0)) && Character.toString(b[i].charAt(b[i].length() - 2)).equals("+") && Character.toString(b[i].charAt(b[i].length() - 1)).equals("+"))) {
         b[i] = "update";
          }
      else if(b[i].contains("+")||
        b[i].contains("-")||
        b[i].contains("/")||
        b[i].contains("%")||
        b[i].contains("*")){
          if(b[i].length()>3){
          if((Character.isAlphabetic(b[i].charAt(b[i].indexOf("+")-1)))||
          (Character.isAlphabetic(b[i].charAt(b[i].indexOf("-")-1)))||
          (Character.isAlphabetic(b[i].charAt(b[i].indexOf("/")-1)))||
          (Character.isAlphabetic(b[i].charAt(b[i].indexOf("*")-1)))||
          (Character.isAlphabetic(b[i].charAt(b[i].indexOf("%")-1)))){
            if(((b[i].charAt(b[i].indexOf("+")+1))=='=')||
          ((b[i].charAt(b[i].indexOf("-")+1))=='=')||
          ((b[i].charAt(b[i].indexOf("/")+1))=='=')||
          ((b[i].charAt(b[i].indexOf("%")+1))=='=')||
          ((b[i].charAt(b[i].indexOf("*")+1))=='=')){
            if(((Character.isAlphabetic(b[i].charAt(b[i].indexOf("+")+2)))||
          (Character.isAlphabetic(b[i].charAt(b[i].indexOf("-")+2)))||
          (Character.isAlphabetic(b[i].charAt(b[i].indexOf("/")+2)))||
          (Character.isAlphabetic(b[i].charAt(b[i].indexOf("*")+2)))||
          (Character.isAlphabetic(b[i].charAt(b[i].indexOf("%")+2))))||
          ((Character.isDigit(b[i].charAt(b[i].indexOf("+")+2)))||
          (Character.isDigit(b[i].charAt(b[i].indexOf("-")+2)))||
          (Character.isDigit(b[i].charAt(b[i].indexOf("/")+2)))||
          (Character.isDigit(b[i].charAt(b[i].indexOf("*")+2)))||
          (Character.isDigit(b[i].charAt(b[i].indexOf("%")+2))))){
            b[i]="expr";
              }                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               

              }
            
            }
          }
        }
        else if((b[i].charAt(0)=='-' && (Character.isAlphabetic(b[i].charAt(1)))||Character.isDigit(b[i].charAt(0)))){
          b[i]="integer";
        }
        else if (b[i] instanceof String || b[i].length() == 1) {
          b[i] = "stringVar";
        }
        else{
          b[i]="unknown";
        }
      
      }
      return b;
    }
    //natalia
    public boolean print(String[] a){
      return true;
    }
    //natalia
    public boolean update(String[] a){
      return true;
    }

  }

