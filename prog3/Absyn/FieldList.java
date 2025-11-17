package Absyn;
import Symbol.Symbol;
public class FieldList extends Absyn {
   public Symbol name;
   public Symbol typ;
   public FieldList tail;
   public boolean escape = false; // Initialize to false (was true for Tiger)
   public FieldList(int p, Symbol n, Symbol t, FieldList x) {pos=p; name=n; typ=t; tail=x;}
}
