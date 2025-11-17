package Absyn;
import Symbol.Symbol;
public class UnaryExpression extends Exp {
    Exp exp;
    int prefix;

    public UnaryExpression(int p, int pre, Exp e) {
        pos=p;
        exp=e;
        prefix=pre;
    }

    public UnaryExpression(int p, Exp e) {
        pos=p;
        exp=e;
        prefix= -1;
    }

    public final static int DECREMENT=0, INCREMENT=1, AND=2, TIMES=3, PLUS=4, MINUS=5, TILDE=6, NOT=7;
}
