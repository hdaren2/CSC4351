package Absyn;
import Symbol.Symbol;
public class ParenExpression extends Exp {
    public int op;
    public Exp exp;
    public ParenExpression(int p, Exp e) {
        pos=p;
        exp=e;
    }
}
