package Absyn;
import Symbol.Symbol;
public class Int extends Exp {
    public int val;
    public Int(int p, int v) {
        pos=p;
        val=v;
    }
}
