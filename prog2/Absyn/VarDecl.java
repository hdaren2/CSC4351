package Absyn;

import java.util.List;

public class VarDecl extends Dec {
    public List<BitMod> modifiers;
    public Type type;
    public Name name;
    public Exp initializer;

    public VarDecl(int pos, List<BitMod> modifiers, Type type, Name name, Exp initializer) {
        this.pos = pos;
        this.modifiers = modifiers;
        this.type = type;
        this.name = name;
        this.initializer = initializer;
    }
}
