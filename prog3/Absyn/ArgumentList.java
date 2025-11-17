package Absyn;
import java.util.List;

public class ArgumentList extends Exp {
    public Exp args;
    public Exp arg;
    public int prefix;
    public ArgumentList(int p, Exp as, Exp a) {
        pos=p;
        args=as;
        arg=a;

    }
}
