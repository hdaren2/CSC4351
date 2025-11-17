package Absyn;
import Symbol.Symbol;
public class StringLit extends Exp{
    public String val;
    public StringLit(int p, String v) {
        pos=p;
        val=v;
    }
}
