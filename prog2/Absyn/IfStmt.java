package Absyn;

public class IfStmt extends Stmt {
    public Exp condition;
    public Stmt thenStmt;
    public Stmt elseStmt;

    public IfStmt(int pos, Exp condition, Stmt thenStmt, Stmt elseStmt) {
        this.pos = pos;
        this.condition = condition;
        this.thenStmt = thenStmt;
        this.elseStmt = elseStmt;
    }
}
