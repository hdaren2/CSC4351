package Absyn;
import Symbol.Symbol;
public class AssignmentExpression extends Exp {
    public int op;
    public Exp left;
    public Exp right;
    public AssignmentExpression(int p, Exp l, Exp r, int o) {
        pos=p;
        left=l;
        right=r;
        op=o;
    }

    public final static int MULASS=0, DIVASS=1, MODASS=2, ADDASS=3, SUBASS=4, LSHIFTASS=5,
        RSHIFTASS=6, BWISEANDASS=7, BWISEXORASS=8, BWISEORASS=9, ASSIGN=10;

}
