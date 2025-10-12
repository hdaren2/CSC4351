package Absyn;

public class Cast extends Exp {
    public Type type;
    public Exp expression;

    public Cast(int p, Type type, Exp expression) {
        this.pos = p;
        this.type = type;
        this.expression = expression;
    }
}
