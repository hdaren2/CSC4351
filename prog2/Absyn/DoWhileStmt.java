package Absyn;

public class DoWhileStmt extends Stmt {
    public Stmt body;
    public Exp condition;

    public DoWhileStmt(int pos, Stmt body, Exp condition) {
        this.pos = pos;
        this.body = body;
        this.condition = condition;
    }
}
