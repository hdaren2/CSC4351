package Absyn;

public class BaseType extends Type {
    public enum Kind {
        VOID, CHAR, SHORT, INT, LONG, FLOAT, DOUBLE
    }

    public Kind kind;

    public BaseType(Kind kind) {
        this.kind = kind;
    }
}
