package Absyn;

import Symbol.Symbol;

public class FieldVar extends Var {
   public Var var;
   public Symbol field;

   public FieldVar(int p, Var v, Symbol f) {
      super(p, new Name("", p)); // Placeholder name
      pos = p;
      var = v;
      field = f;
   }
}
