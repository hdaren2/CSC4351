package Absyn;

public class CondExp extends Exp {
    public Exp condition;
    public Exp trueExp;
    public Exp falseExp;

    public CondExp(int pos, Exp condition, Exp trueExp, Exp falseExp) {
        this.pos = pos;
        this.condition = condition;
        this.trueExp = trueExp;
        this.falseExp = falseExp;
    }
}
