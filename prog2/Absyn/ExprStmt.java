package Absyn;

public class ExprStmt extends Stmt {
    public Exp expression;

    public ExprStmt(int pos, Exp expression) {
        this.pos = pos;
        this.expression = expression;
    }
}
