package Absyn;

public class Assign extends Exp {
    public enum Op {
        ASSIGN, PLUS_ASSIGN, MINUS_ASSIGN, STAR_ASSIGN, SLASH_ASSIGN, PERCENT_ASSIGN
    }

    public Op op;
    public Exp left;
    public Exp right;

    public Assign(int pos, Op op, Exp left, Exp right) {
        this.pos = pos;
        this.op = op;
        this.left = left;
        this.right = right;
    }
}
