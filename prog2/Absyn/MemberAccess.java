package Absyn;

public class MemberAccess extends Exp {
    public Exp expression;
    public String member;
    public boolean isArrow; // true for ->, false for .

    public MemberAccess(int p, Exp expression, String member) {
        this(p, expression, member, false);
    }

    public MemberAccess(int p, Exp expression, String member, boolean isArrow) {
        this.pos = p;
        this.expression = expression;
        this.member = member;
        this.isArrow = isArrow;
    }
}
