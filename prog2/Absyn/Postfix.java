package Absyn;

public class Postfix extends Exp {
    public enum Op {
        POSTINC, POSTDEC
    }

    public Op op;
    public Exp operand;

    public Postfix(int pos, Op op, Exp operand) {
        this.pos = pos;
        this.op = op;
        this.operand = operand;
    }
}
