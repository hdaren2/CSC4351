package Absyn;

public class TypeDecl extends Dec {
    public Type type;
    public Name name;

    public TypeDecl(int pos, Type type, Name name) {
        this.pos = pos;
        this.type = type;
        this.name = name;
    }
}
