package Absyn;

public class Comma extends Exp {
    public Exp left;
    public Exp right;

    public Comma(int p, Exp left, Exp right) {
        this.pos = p;
        this.left = left;
        this.right = right;
    }
}
