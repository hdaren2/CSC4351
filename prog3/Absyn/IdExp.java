package Absyn;
import Symbol.Symbol;
public class IdExp extends Exp{
    Id id;
    public IdExp(int p, Id i) {
        pos=p;
        id=i;
    }
}
