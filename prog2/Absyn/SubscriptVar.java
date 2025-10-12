package Absyn;

import Symbol.Symbol;

public class SubscriptVar extends Var {
   public Var var;
   public Exp index;

   public SubscriptVar(int p, Var v, Exp i) {
      super(p, new Name("", p)); // Placeholder name
      pos = p;
      var = v;
      index = i;
   }
}
