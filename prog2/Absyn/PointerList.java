package Absyn;

public class PointerList extends Absyn {
    public int count;

    public PointerList(int count) {
        this.count = count;
    }

    public void inc() {
        this.count++;
    }
}
