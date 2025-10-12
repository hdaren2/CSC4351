package Absyn;

public class Initializer extends Absyn {
    public Exp expression;
    public InitializerList list;

    public Initializer(Exp expression) {
        this.expression = expression;
        this.list = null;
    }

    public Initializer(InitializerList list) {
        this.expression = null;
        this.list = list;
    }
}
