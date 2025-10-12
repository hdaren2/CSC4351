package Absyn;

public class Param extends Absyn {
    public Type type;
    public Name name;

    public Param(int pos, Type type, Name name) {
        this.pos = pos;
        this.type = type;
        this.name = name;
    }
}
