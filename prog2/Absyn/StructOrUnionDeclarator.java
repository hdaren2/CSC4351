package Absyn;

public class StructOrUnionDeclarator extends Absyn {
    public StructDeclarationList declarations;

    public StructOrUnionDeclarator(StructDeclarationList declarations) {
        this.declarations = declarations;
    }
}
