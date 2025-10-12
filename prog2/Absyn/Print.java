package Absyn;

public class Print {

  java.io.PrintWriter out;

  public Print(java.io.PrintWriter o) {
    out = o;
  }

  void indent(int d) {
    for (int i = 0; i < d; i++)
      out.print(' ');
  }

  void say(String s) {
    out.print(s);
  }

  void say(int i) {
    out.print(i);
  }

  void say(boolean b) {
    out.print(b);
  }

  void sayln(String s) {
    out.println(s);
  }

  public void prProgram(Program p, int d) {
    indent(d);
    say("Program(");
    if (p != null && p.declarations != null) {
      sayln("");
      for (int i = 0; i < p.declarations.size(); i++) {
        prDec(p.declarations.get(i), d + 1);
        if (i < p.declarations.size() - 1) {
          sayln(",");
        }
      }
    }
    say(")");
  }

  void prVar(SimpleVar v, int d) {
    say("SimpleVar(");
    say(v.name.toString());
    say(")");
  }

  void prVar(FieldVar v, int d) {
    sayln("FieldVar(");
    prVar(v.var, d + 1);
    sayln(",");
    indent(d + 1);
    say(v.field.toString());
    say(")");
  }

  void prVar(SubscriptVar v, int d) {
    sayln("SubscriptVar(");
    prVar(v.var, d + 1);
    sayln(",");
    prExp(v.index, d + 1);
    say(")");
  }

  /* Print A_var types. Indent d spaces. */
  void prVar(Var v, int d) {
    indent(d);
    if (v instanceof SimpleVar)
      prVar((SimpleVar) v, d);
    else if (v instanceof FieldVar)
      prVar((FieldVar) v, d);
    else if (v instanceof SubscriptVar)
      prVar((SubscriptVar) v, d);
    else
      throw new Error("Print.prVar");
  }

  void prExp(OpExp e, int d) {
    sayln("OpExp(");
    indent(d + 1);
    switch (e.oper) {
      case OpExp.PLUS:
        say("PLUS");
        break;
      case OpExp.MINUS:
        say("MINUS");
        break;
      case OpExp.MUL:
        say("MUL");
        break;
      case OpExp.DIV:
        say("DIV");
        break;
      case OpExp.EQ:
        say("EQ");
        break;
      case OpExp.NE:
        say("NE");
        break;
      case OpExp.LT:
        say("LT");
        break;
      case OpExp.LE:
        say("LE");
        break;
      case OpExp.GT:
        say("GT");
        break;
      case OpExp.GE:
        say("GE");
        break;
      default:
        throw new Error("Print.prExp.OpExp");
    }
    sayln(",");
    prExp(e.left, d + 1);
    sayln(",");
    prExp(e.right, d + 1);
    say(")");
  }

  void prExp(NilExp e, int d) {
    say("NilExp()");
  }

  void prExp(IntExp e, int d) {
    say("IntExp(");
    say(e.value);
    say(")");
  }

  void prExp(StringExp e, int d) {
    say("StringExp(");
    say(e.value);
    say(")");
  }

  void prExp(RecordExp e, int d) {
    say("RecordExp(");
    say(e.typ.toString());
    sayln(",");
    prFieldExpList(e.fields, d + 1);
    say(")");
  }

  void prExp(SeqExp e, int d) {
    sayln("SeqExp(");
    prExplist(e.list, d + 1);
    say(")");
  }

  void prExp(IfExp e, int d) {
    sayln("IfExp(");
    prExp(e.test, d + 1);
    sayln(",");
    prExp(e.thenclause, d + 1);
    if (e.elseclause != null) { /* else is optional */
      sayln(",");
      prExp(e.elseclause, d + 1);
    }
    say(")");
  }

  void prExp(WhileExp e, int d) {
    sayln("WhileExp(");
    prExp(e.test, d + 1);
    sayln(",");
    prExp(e.body, d + 1);
    say(")");
  }

  void prExp(BreakExp e, int d) {
    say("BreakExp()");
  }

  void prExp(ArrayExp e, int d) {
    say("ArrayExp(");
    say(e.typ.toString());
    sayln(",");
    prExp(e.size, d + 1);
    sayln(",");
    prExp(e.init, d + 1);
    say(")");
  }

  /* Print Exp class types. Indent d spaces. */
  public void prExp(Exp e, int d) {
    indent(d);
    if (e instanceof OpExp)
      prExp((OpExp) e, d);
    else if (e instanceof NilExp)
      prExp((NilExp) e, d);
    else if (e instanceof IntExp)
      prExp((IntExp) e, d);
    else if (e instanceof StringExp)
      prExp((StringExp) e, d);
    else if (e instanceof RecordExp)
      prExp((RecordExp) e, d);
    else if (e instanceof SeqExp)
      prExp((SeqExp) e, d);
    else if (e instanceof IfExp)
      prExp((IfExp) e, d);
    else if (e instanceof WhileExp)
      prExp((WhileExp) e, d);
    else if (e instanceof BreakExp)
      prExp((BreakExp) e, d);
    else if (e instanceof ArrayExp)
      prExp((ArrayExp) e, d);
    else if (e instanceof MemberAccess)
      prExp((MemberAccess) e, d);
    else if (e instanceof Cast)
      prExp((Cast) e, d);
    else if (e instanceof Comma)
      prExp((Comma) e, d);
    else if (e instanceof Binary)
      prExp((Binary) e, d);
    else if (e instanceof Unary)
      prExp((Unary) e, d);
    else if (e instanceof Postfix)
      prExp((Postfix) e, d);
    else if (e instanceof Index)
      prExp((Index) e, d);
    else if (e instanceof Call)
      prExp((Call) e, d);
    else if (e instanceof ParenExp)
      prExp((ParenExp) e, d);
    else if (e instanceof Var)
      prExp((Var) e, d);
    else if (e instanceof IntExp)
      prExp((IntExp) e, d);
    else if (e instanceof CharLit)
      prExp((CharLit) e, d);
    else if (e instanceof Assign)
      prExp((Assign) e, d);
    else if (e instanceof CondExp)
      prExp((CondExp) e, d);
    else
      throw new Error("Print.prExp");
  }

  void prDec(Dec d, int i) {
    indent(i);
    if (d instanceof FunDecl)
      prFunDecl((FunDecl) d, i);
    else if (d instanceof VarDecl)
      prVarDecl((VarDecl) d, i);
    else if (d instanceof TypeDecl)
      prTypeDecl((TypeDecl) d, i);
    else
      throw new Error("Print.prDec");
  }

  void prFunDecl(FunDecl d, int i) {
    say("FunDecl(");
    sayln("");
    indent(i + 1);
    say("\"" + d.name.name + "\"");
    sayln(",");
    indent(i + 1);
    say("Type(");
    prType(d.type, i + 2);
    say(")");
    if (d.parameters != null && !d.parameters.isEmpty()) {
      sayln(",");
      indent(i + 1);
      say("Parameters(");
      for (int j = 0; j < d.parameters.size(); j++) {
        sayln("");
        prParam(d.parameters.get(j), i + 2);
        if (j < d.parameters.size() - 1) {
          sayln(",");
        }
      }
      say(")");
    }
    if (d.body != null) {
      sayln(",");
      prCompoundStmt(d.body, i + 1);
    }
    sayln("");
    indent(i);
    say(")");
  }

  void prVarDecl(VarDecl d, int i) {
    say("VarDecl(");
    sayln("");
    indent(i + 1);
    say("\"" + d.name.name + "\"");
    sayln(",");
    indent(i + 1);
    say("Type(");
    prType(d.type, i + 2);
    say(")");
    if (d.initializer != null) {
      sayln(",");
      indent(i + 1);
      say("Init(");
      prExp(d.initializer, i + 2);
      say(")");
    }
    sayln("");
    indent(i);
    say(")");
  }

  void prTypeDecl(TypeDecl d, int i) {
    say("TypeDecl(");
    sayln("");
    indent(i + 1);
    say("\"" + d.name.name + "\"");
    sayln(",");
    indent(i + 1);
    say("Type(");
    prType(d.type, i + 2);
    say(")");
    sayln("");
    indent(i);
    say(")");
  }

  void prFieldlist(FieldList f, int d) {
    indent(d);
    say("FieldList(");
    if (f != null) {
      sayln("");
      indent(d + 1);
      say(f.name.toString());
      sayln(",");
      indent(d + 1);
      say(f.typ.toString());
      sayln(",");
      indent(d + 1);
      say(f.escape);
      sayln(",");
      prFieldlist(f.tail, d + 1);
    }
    say(")");
  }

  void prExplist(ExpList e, int d) {
    indent(d);
    say("ExpList(");
    if (e != null && e.expressions != null) {
      sayln("");
      for (int i = 0; i < e.expressions.size(); i++) {
        prExp(e.expressions.get(i), d + 1);
        if (i < e.expressions.size() - 1) {
          sayln(",");
        }
      }
    }
    say(")");
  }

  void prFieldExpList(FieldExpList f, int d) {
    indent(d);
    say("FieldExpList(");
    if (f != null) {
      sayln("");
      indent(d + 1);
      say(f.name.toString());
      sayln(",");
      prExp(f.init, d + 1);
      sayln(",");
      prFieldExpList(f.tail, d + 1);
    }
    say(")");
  }

  void prExp(MemberAccess e, int d) {
    indent(d);
    say("MemberAccess(");
    prExp(e.expression, d + 1);
    sayln(",");
    indent(d + 1);
    say("\"" + e.member + "\"");
    sayln(",");
    indent(d + 1);
    say(e.isArrow ? "true" : "false");
    say(")");
  }

  void prExp(Cast e, int d) {
    indent(d);
    say("Cast(");
    prType(e.type, d + 1);
    sayln(",");
    prExp(e.expression, d + 1);
    say(")");
  }

  void prExp(Comma e, int d) {
    indent(d);
    say("Comma(");
    prExp(e.left, d + 1);
    sayln(",");
    prExp(e.right, d + 1);
    say(")");
  }

  void prType(Type t, int d) {
    indent(d);
    if (t instanceof BaseType) {
      BaseType bt = (BaseType) t;
      say("BaseType(" + bt.kind + ")");
    } else if (t instanceof NamedType) {
      NamedType nt = (NamedType) t;
      say("NamedType(\"" + nt.name + "\")");
    } else if (t instanceof EnumType) {
      EnumType et = (EnumType) t;
      say("EnumType(\"" + et.name + "\")");
    } else {
      say("Type()");
    }
  }

  void prParam(Param p, int d) {
    indent(d);
    say("Param(");
    prType(p.type, d + 1);
    sayln(",");
    indent(d + 1);
    say("\"" + p.name.name + "\"");
    say(")");
  }

  void prCompoundStmt(CompoundStmt s, int d) {
    if (s instanceof Block) {
      prBlock((Block) s, d);
    } else {
      indent(d);
      say("CompoundStmt()");
    }
  }

  void prBlock(Block b, int d) {
    indent(d);
    say("Block(");
    if (b.statements != null && !b.statements.isEmpty()) {
      sayln("");
      for (int i = 0; i < b.statements.size(); i++) {
        prStmt(b.statements.get(i), d + 1);
        if (i < b.statements.size() - 1) {
          sayln(",");
        }
      }
    }
    sayln("");
    indent(d);
    say(")");
  }

  void prStmt(Stmt s, int d) {
    indent(d);
    if (s instanceof IfStmt) {
      prIfStmt((IfStmt) s, d);
    } else if (s instanceof WhileStmt) {
      prWhileStmt((WhileStmt) s, d);
    } else if (s instanceof DoWhileStmt) {
      prDoWhileStmt((DoWhileStmt) s, d);
    } else if (s instanceof ForStmt) {
      prForStmt((ForStmt) s, d);
    } else if (s instanceof ReturnStmt) {
      prReturnStmt((ReturnStmt) s, d);
    } else if (s instanceof BreakStmt) {
      prBreakStmt((BreakStmt) s, d);
    } else if (s instanceof ContinueStmt) {
      prContinueStmt((ContinueStmt) s, d);
    } else if (s instanceof ExprStmt) {
      prExprStmt((ExprStmt) s, d);
    } else if (s instanceof Block) {
      prBlock((Block) s, d);
    } else if (s instanceof VarDeclStmt) {
      prVarDeclStmt((VarDeclStmt) s, d);
    } else {
      say("Stmt()");
    }
  }

  void prIfStmt(IfStmt s, int d) {
    say("IfStmt(");
    sayln("");
    prExp(s.condition, d + 1);
    sayln(",");
    prStmt(s.thenStmt, d + 1);
    if (s.elseStmt != null) {
      sayln(",");
      prStmt(s.elseStmt, d + 1);
    }
    sayln("");
    indent(d);
    say(")");
  }

  void prWhileStmt(WhileStmt s, int d) {
    say("WhileStmt(");
    sayln("");
    prExp(s.condition, d + 1);
    sayln(",");
    prStmt(s.body, d + 1);
    sayln("");
    indent(d);
    say(")");
  }

  void prDoWhileStmt(DoWhileStmt s, int d) {
    say("DoWhileStmt(");
    sayln("");
    prStmt(s.body, d + 1);
    sayln(",");
    prExp(s.condition, d + 1);
    sayln("");
    indent(d);
    say(")");
  }

  void prForStmt(ForStmt s, int d) {
    say("ForStmt(");
    if (s.initialization != null) {
      sayln("");
      prExp(s.initialization, d + 1);
    }
    if (s.condition != null) {
      sayln(",");
      prExp(s.condition, d + 1);
    }
    if (s.increment != null) {
      sayln(",");
      prExp(s.increment, d + 1);
    }
    sayln(",");
    prStmt(s.body, d + 1);
    sayln("");
    indent(d);
    say(")");
  }

  void prReturnStmt(ReturnStmt s, int d) {
    say("ReturnStmt(");
    if (s.expression != null) {
      sayln("");
      prExp(s.expression, d + 1);
    }
    say(")");
  }

  void prBreakStmt(BreakStmt s, int d) {
    say("BreakStmt()");
  }

  void prContinueStmt(ContinueStmt s, int d) {
    say("ContinueStmt()");
  }

  void prExprStmt(ExprStmt s, int d) {
    say("ExprStmt(");
    sayln("");
    prExp(s.expression, d + 1);
    sayln("");
    indent(d);
    say(")");
  }

  void prVarDeclStmt(VarDeclStmt s, int d) {
    say("VarDeclStmt(");
    sayln("");
    prVarDecl(s.declaration, d + 1);
    sayln("");
    indent(d);
    say(")");
  }

  void prExp(Binary e, int d) {
    indent(d);
    say("Binary(");
    sayln("");
    indent(d + 1);
    say("Left(");
    sayln("");
    prExp(e.left, d + 2);
    sayln("");
    indent(d + 1);
    say(")");
    sayln(",");
    indent(d + 1);
    say("Op(\"" + e.op + "\")");
    sayln(",");
    indent(d + 1);
    say("Right(");
    sayln("");
    prExp(e.right, d + 2);
    sayln("");
    indent(d + 1);
    say(")");
    sayln("");
    indent(d);
    say(")");
  }

  void prExp(Unary e, int d) {
    indent(d);
    say("Unary(");
    sayln("");
    indent(d + 1);
    say("Op(\"" + e.op + "\")");
    sayln(",");
    indent(d + 1);
    say("Operand(");
    sayln("");
    prExp(e.operand, d + 2);
    sayln("");
    indent(d + 1);
    say(")");
    sayln("");
    indent(d);
    say(")");
  }

  void prExp(Postfix e, int d) {
    indent(d);
    say("Postfix(");
    sayln("");
    prExp(e.operand, d + 1);
    sayln(",");
    indent(d + 1);
    say("\"" + e.op + "\"");
    sayln("");
    indent(d);
    say(")");
  }

  void prExp(Index e, int d) {
    indent(d);
    say("Index(");
    sayln("");
    prExp(e.array, d + 1);
    sayln(",");
    prExp(e.index, d + 1);
    sayln("");
    indent(d);
    say(")");
  }

  void prExp(Call e, int d) {
    indent(d);
    say("Call(");
    sayln("");
    prExp(e.function, d + 1);
    if (e.arguments != null && !e.arguments.isEmpty()) {
      sayln(",");
      indent(d + 1);
      say("Arguments(");
      for (int i = 0; i < e.arguments.size(); i++) {
        sayln("");
        prExp(e.arguments.get(i), d + 2);
        if (i < e.arguments.size() - 1) {
          sayln(",");
        }
      }
      say(")");
    }
    sayln("");
    indent(d);
    say(")");
  }

  void prExp(ParenExp e, int d) {
    indent(d);
    say("ParenExp(");
    sayln("");
    prExp(e.expression, d + 1);
    sayln("");
    indent(d);
    say(")");
  }

  void prExp(Var e, int d) {
    indent(d);
    say("Var(");
    sayln("");
    indent(d + 1);
    say("\"" + e.name.name + "\"");
    sayln("");
    indent(d);
    say(")");
  }

  void prExp(CharLit e, int d) {
    indent(d);
    say("CharLit('" + e.value + "')");
  }

  void prExp(Assign e, int d) {
    indent(d);
    say("Assign(");
    sayln("");
    indent(d + 1);
    say("Op(\"" + e.op + "\")");
    sayln(",");
    indent(d + 1);
    say("Left(");
    sayln("");
    prExp(e.left, d + 2);
    sayln("");
    indent(d + 1);
    say(")");
    sayln(",");
    indent(d + 1);
    say("Right(");
    sayln("");
    prExp(e.right, d + 2);
    sayln("");
    indent(d + 1);
    say(")");
    sayln("");
    indent(d);
    say(")");
  }

  void prExp(CondExp e, int d) {
    indent(d);
    say("CondExp(");
    sayln("");
    prExp(e.condition, d + 1);
    sayln(",");
    prExp(e.trueExp, d + 1);
    sayln(",");
    prExp(e.falseExp, d + 1);
    sayln("");
    indent(d);
    say(")");
  }
}
