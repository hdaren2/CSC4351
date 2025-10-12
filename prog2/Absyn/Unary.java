package Absyn;

public class Unary extends Exp {
    public enum Op {
        LNOT, BNOT, PREINC, PREDEC, NEG, POS, ADDRESS, DEREF
    }

    public Op op;
    public Exp operand;

    public Unary(int pos, Op op, Exp operand) {
        this.pos = pos;
        this.op = op;
        this.operand = operand;
    }
}
