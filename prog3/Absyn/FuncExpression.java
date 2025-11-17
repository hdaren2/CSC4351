package Absyn;
public class FuncExpression extends Exp {
    public Exp name;
    public Exp args;
    public FuncExpression(int p, Exp n, Exp a) {
        pos=p;
        name = n;
        args = a;
    }
}
