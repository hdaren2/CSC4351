package Absyn;
import Symbol.Symbol;
public class CommaExpression extends Exp {
   public Exp left, right;
   public CommaExpression(int p, Exp l, Exp r) {pos=p; left=l; right=r;}
}
