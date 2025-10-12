package Absyn;

import java.util.List;

public class Call extends Exp {
    public Exp function;
    public List<Exp> arguments;

    public Call(int pos, Exp function, List<Exp> arguments) {
        this.pos = pos;
        this.function = function;
        this.arguments = arguments;
    }
}
