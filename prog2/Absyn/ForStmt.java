package Absyn;

public class ForStmt extends Stmt {
    public Exp initialization;
    public Exp condition;
    public Exp increment;
    public Stmt body;

    public ForStmt(int pos, Exp initialization, Exp condition, Exp increment, Stmt body) {
        this.pos = pos;
        this.initialization = initialization;
        this.condition = condition;
        this.increment = increment;
        this.body = body;
    }

    public static ForStmt from(int pos, Object init, Exp condition, Exp increment, Stmt body) {
        Exp initExp = null;
        if (init instanceof Exp) {
            initExp = (Exp) init;
        }
        return new ForStmt(pos, initExp, condition, increment, body);
    }
}
