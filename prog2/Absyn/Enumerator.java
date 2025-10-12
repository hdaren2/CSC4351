package Absyn;

public class Enumerator extends Absyn {
    public String name;
    public Exp value;

    public Enumerator(String name, Exp value) {
        this.name = name;
        this.value = value;
    }
}
