package Absyn;
import Symbol.Symbol;
public class PlusPlusExpression extends Postfix {
    public PlusPlusExpression(int p, Exp src){
        pos=p; postfix_expression=src;
    }
}
