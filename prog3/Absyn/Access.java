package Absyn;
import Symbol.Symbol;
public class Access extends Postfix {
    public int operator;
    public Id target;

    public Access(int p, Exp src, int op, Id trg){
        pos = p; operator = op; postfix_expression = src; target = trg;
        
    };
    public final static int PERIOD=0, ARROW=1;
}
