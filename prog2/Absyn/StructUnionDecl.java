package Absyn;

import java.util.List;

public class StructUnionDecl extends Dec {
    public List<BitMod> modifiers;
    public StructOrUnion structOrUnion;
    public Name name;
    public StructOrUnionDeclarator declarator;

    public StructUnionDecl(int pos, List<BitMod> modifiers, StructOrUnion structOrUnion, Name name,
            StructOrUnionDeclarator declarator) {
        this.pos = pos;
        this.modifiers = modifiers;
        this.structOrUnion = structOrUnion;
        this.name = name;
        this.declarator = declarator;
    }
}
