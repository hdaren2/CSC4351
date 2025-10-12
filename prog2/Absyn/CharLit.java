package Absyn;

public class CharLit extends Exp {
    public char value;

    public CharLit(int pos, char value) {
        this.pos = pos;
        this.value = value;
    }
}
