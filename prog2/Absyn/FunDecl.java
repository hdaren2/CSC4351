package Absyn;

import java.util.List;

public class FunDecl extends Dec {
    public List<BitMod> modifiers;
    public Type type;
    public Name name;
    public List<Param> parameters;
    public CompoundStmt body;

    public FunDecl(int pos, List<BitMod> modifiers, Type type, Name name, List<Param> parameters, CompoundStmt body) {
        this.pos = pos;
        this.modifiers = modifiers;
        this.type = type;
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }
}
