package Absyn;

public class VarDeclStmt extends Stmt {
    public VarDecl declaration;

    public VarDeclStmt(int pos, VarDecl declaration) {
        this.pos = pos;
        this.declaration = declaration;
    }
}
