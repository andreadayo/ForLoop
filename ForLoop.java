import java.util.Arrays;

public class ForLoop {
    public static void main(String[] args)
    {
        /*for (int i = 0; i < 5; i += 2) {
          System.out.println(i);
          System.out.println("Hello World");
          System.out.println("Hello World");
        }*/
        String a = "for ( int i=1 ; i<length ; ++i ) {    }";
        String[] test=tokenizer(a);
        System.out.println(Arrays.toString(test));
    }
    
    //natalia
    public static String[] tokenizer(String a){
      a.trim();
      String[] b= a.split(" "); 
      for(int i=0; i<b.length; i++){
        if((b[i].charAt(0)=='+' && b[i].charAt(1)=='+' && b[i].charAt(2).isChar)||() ){//gonna continue later

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

