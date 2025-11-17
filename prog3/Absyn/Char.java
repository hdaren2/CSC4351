package Absyn;
import Symbol.Symbol;
public class Char extends Exp {
    public char val;
    public Char(int p, char v) {
        pos=p;
        val=v;
    }
}
