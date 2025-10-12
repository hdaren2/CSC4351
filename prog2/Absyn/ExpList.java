package Absyn;

import Symbol.Symbol;
import java.util.List;

public class ExpList {
   public List<Exp> expressions;

   public ExpList(List<Exp> expressions) {
      this.expressions = expressions;
   }
}
