package Absyn;

public class Index extends Exp {
    public Exp array;
    public Exp index;

    public Index(int pos, Exp array, Exp index) {
        this.pos = pos;
        this.array = array;
        this.index = index;
    }
}
