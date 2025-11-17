package Absyn;
import Symbol.Symbol;
public class MinusMinusExpression extends Postfix {
    public MinusMinusExpression(int p, Exp src){
        pos=p; postfix_expression=src;
    }
}
