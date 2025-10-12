package Absyn;

public class ParenExp extends Exp {
    public Exp expression;

    public ParenExp(int pos, Exp expression) {
        this.pos = pos;
        this.expression = expression;
    }
}
