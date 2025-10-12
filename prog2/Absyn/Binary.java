package Absyn;

public class Binary extends Exp {
    public enum Op {
        LOR, LAND, BOR, BXOR, BAND, EQ, NEQ, LT, LE, GT, GE, SHL, SHR, ADD, SUB, MUL, DIV, MOD
    }

    public Op op;
    public Exp left;
    public Exp right;

    public Binary(int pos, Op op, Exp left, Exp right) {
        this.pos = pos;
        this.op = op;
        this.left = left;
        this.right = right;
    }
}
