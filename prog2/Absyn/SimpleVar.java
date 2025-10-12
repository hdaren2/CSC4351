package Absyn;

import Symbol.Symbol;

public class SimpleVar extends Var {
   public Symbol name;

   public SimpleVar(int p, Symbol n) {
      super(p, new Name(n.toString(), p));
      this.name = n;
   }
}
