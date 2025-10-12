package Absyn;

import java.util.List;

public class StructDeclarationList extends Absyn {
    public List<StructDeclaration> declarations;

    public StructDeclarationList(List<StructDeclaration> declarations) {
        this.declarations = declarations;
    }
}
