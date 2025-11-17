package Absyn;
import Symbol.Symbol;
public class Id extends Exp {
    public String id;
    public Id(int p, String i) {
        pos=p;
        id=i;
    }
}
