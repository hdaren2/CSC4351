import Parse.*;
import ErrorMsg.ErrorMsg;
import java.io.FileInputStream;

public class debug_lexer {
    public static void main(String[] args) throws Exception {
        ErrorMsg errorMsg = new ErrorMsg("test.c");
        FileInputStream inp = new FileInputStream("test.c");
        Yylex lexer = new Yylex(inp, errorMsg);
        
        java_cup.runtime.Symbol token;
        while ((token = lexer.nextToken()).sym != 0) { // EOF is 0
            System.out.println("Token: " + token.sym + " = " + token.value);
        }
    }
}
