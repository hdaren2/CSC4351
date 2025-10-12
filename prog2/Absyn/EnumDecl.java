package Absyn;

public class EnumDecl extends Dec {
    public String name;
    public EnumeratorList enumerators;

    public EnumDecl(int pos, String name, EnumeratorList enumerators) {
        this.pos = pos;
        this.name = name;
        this.enumerators = enumerators;
    }
}
