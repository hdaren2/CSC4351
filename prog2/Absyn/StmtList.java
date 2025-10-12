package Absyn;

import java.util.List;

public class StmtList extends Absyn {
    public List<Stmt> statements;

    public StmtList(List<Stmt> statements) {
        this.statements = statements;
    }
}
