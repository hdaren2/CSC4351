package Absyn;
public class ArrayExpression extends Exp {
    public Exp name;
    public Exp index;
    public ArrayExpression(int p, Exp n, Exp i) {
        pos=p;
        name = n;
        index = i;
    }
}
