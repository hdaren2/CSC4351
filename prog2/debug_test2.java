import Parse.*;
import ErrorMsg.ErrorMsg;
import java.io.FileInputStream;

public class debug_test2 {
    public static void main(String[] args) throws Exception {
        ErrorMsg errorMsg = new ErrorMsg("test2.c");
        FileInputStream inp = new FileInputStream("test2.c");
        Yylex lexer = new Yylex(inp, errorMsg);
        
        java_cup.runtime.Symbol token;
        int count = 0;
        while ((token = lexer.nextToken()).sym != 0) { // EOF is 0
            System.out.println("Token " + count + ": " + token.sym + " = " + token.value);
            count++;
        }
    }
}
