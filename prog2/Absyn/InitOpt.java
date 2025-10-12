package Absyn;

public class InitOpt extends Absyn {
    public Initializer initializer;
    public Exp expression;

    public InitOpt(Initializer initializer) {
        this.initializer = initializer;
        this.expression = initializer != null ? initializer.expression : null;
    }
}
