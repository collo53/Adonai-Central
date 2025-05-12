import javax.swing.*;
import java.awt.*;
public class demo {
    public static void main(String[] args) {

    String[][] MyNumber={ {"deco","ansu","pique","xavi"} ,{"messi","puyol","carlos"} };
    for (int i=0; i<MyNumber.length; ++i ) {
        System.out.println(i);
        for (int j=0;j<MyNumber[i].length ;++j ) {
            System.out.println(MyNumber[i][j]);
            
        }
        
    }
        
       
    }
}