package Absyn;

public class Var extends Exp {
    public Name name;

    public Var(int pos, Name name) {
        this.pos = pos;
        this.name = name;
    }
}