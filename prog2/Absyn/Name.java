package Absyn;

public class Name extends Absyn {
    public String name;

    public Name(String name, int pos) {
        this.name = name;
        this.pos = pos;
    }
}
