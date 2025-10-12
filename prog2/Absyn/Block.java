package Absyn;

import java.util.List;

public class Block extends CompoundStmt {
    public List<Stmt> statements;

    public Block(int pos, List<Stmt> statements) {
        this.pos = pos;
        this.statements = statements;
    }
}
