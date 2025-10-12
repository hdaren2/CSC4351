package Absyn;

public class StructDeclaration extends Absyn {
    public Type type;
    public Name name;

    public StructDeclaration(Type type, Name name) {
        this.type = type;
        this.name = name;
    }
}
