package Absyn;

public class WhileStmt extends Stmt {
    public Exp condition;
    public Stmt body;

    public WhileStmt(int pos, Exp condition, Stmt body) {
        this.pos = pos;
        this.condition = condition;
        this.body = body;
    }
}
