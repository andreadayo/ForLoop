import java.util.Arrays;

public class ForLoop {
    public static void main(String[] args)
    {
        /*for (int i = 0; i < 5; i += 2) {
          System.out.println(i);
          System.out.println("Hello World");
          System.out.println("Hello World");
        }*/
        String a = "for ( int i=1 ; i<length ; ++i ) { }";
        String[] test=tokenizer(a);
        System.out.println(Arrays.toString(test));
    }
    
    //natalia
    public static String[] tokenizer(String a){
      a.trim();
      String[] b= a.split(" "); 
      for(int i=0; i<b.length; i++){
        if(b[i].contains("+")||b[i].contains("-")||b[i].contains("/")||b[i].contains("%")||b[i].contains("*")){
          if(){

          }

        }
        else if(b[i]=="System.out.print"||b[i]=="System.out.println"){
          b[i]="print";
        }
        else if((b[i].charAt(0)=='+' && b[i].charAt(1)=='+' && Character.isAlphabetic(b[i].charAt(2)))||
        (b[i].charAt(0)=='-' && b[i].charAt(1)=='-' && Character.isAlphabetic(b[i].charAt(2)))||
        (Character.isAlphabetic(b[i].charAt(0)) && b[i].charAt(1)=='-' && b[i].charAt(2)=='-')||
        (Character.isAlphabetic(b[i].charAt(0)) && b[i].charAt(1)=='+' && b[i].charAt(2)=='+')){
          b[i]="update";
        }
        else if((b[i].charAt(0)=='-' && Character.isDigit(b[i].charAt(1)))||Character.isDigit(b[i].charAt(0))){
          b[i]="int";
        }
        else if(Character.isAlphabetic(b[i].charAt(0))|| b[i] instanceof String){
          b[i]="stringVar";
        }
      }
      return b; //this is just for refresher
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

