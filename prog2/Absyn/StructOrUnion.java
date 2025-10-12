package Absyn;

public class StructOrUnion extends Absyn {
    public enum Kind {
        STRUCT, UNION
    }

    public Kind kind;

    public StructOrUnion(Kind kind) {
        this.kind = kind;
    }
}
