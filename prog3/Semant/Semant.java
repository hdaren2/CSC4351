package Semant;

import Translate.Exp;
import Types.Type;
import java.util.Hashtable;
import Translate.Level;
import Symbol.Symbol;

public class Semant {
  Env env;
  Level level;
  boolean inFunction = false; // Track if we're inside a function

  public Semant(Frame.Frame frame, ErrorMsg.ErrorMsg err) {
    this(new Env(err), new Level(frame));
    inFunction = false; // Start at global scope
  }

  Semant(Env e, Level l) {
    env = e;
    level = l;
    inFunction = false;
  }

  public void transProg(Absyn.DeclarationList decls) {
    // First, perform escape analysis on all declarations
    if (decls != null && decls.list != null) {
      for (Absyn.Decl decl : decls.list) {
        FindEscape.FindEscape fe = new FindEscape.FindEscape(null);
        fe.traverseDecl(decl);
      }
    }
    // Allocate a new level for the program body
    level = new Level(level, null, null);
    // Process all declarations at global scope
    // First pass: process type declarations (struct/union)
    if (decls != null && decls.list != null) {
      for (Absyn.Decl decl : decls.list) {
        if (decl instanceof Absyn.StructDeclaration) {
          transDec(decl);
        }
      }
    }
    // Second pass: process function signatures first (for forward declarations)
    // Only register signatures, don't process bodies yet
    if (decls != null && decls.list != null) {
      for (Absyn.Decl decl : decls.list) {
        if (decl instanceof Absyn.FunctionDefinition) {
          // Register function signature only
          Absyn.FunctionDefinition fd = (Absyn.FunctionDefinition) decl;
          Types.RECORD fields = null;
          Types.RECORD fieldsTail = null;
          if (fd.params != null && fd.params.params != null) {
            for (Absyn.Parameter param : fd.params.params) {
              Type paramType = convertCType(param.type);
              Symbol paramName = Symbol.symbol(param.name);
              Types.NAME nameType = new Types.NAME(paramName);
              nameType.bind(paramType);
              Types.RECORD newField = new Types.RECORD(paramName, nameType, null);
              if (fields == null) {
                fields = newField;
                fieldsTail = newField;
              } else {
                fieldsTail.tail = newField;
                fieldsTail = newField;
              }
            }
          }
          Type resultType = fd.type != null ? convertCType(fd.type) : VOID;
          Symbol name = Symbol.symbol(fd.name);
          FunEntry entry = new FunEntry(fields, resultType);
          env.venv.put(name, entry);
        } else if (decl instanceof Absyn.FunctionDeclaration) {
          transDec(decl);
        }
      }
    }
    // Third pass: process function bodies (now all signatures are known)
    if (decls != null && decls.list != null) {
      for (Absyn.Decl decl : decls.list) {
        if (decl instanceof Absyn.FunctionDefinition) {
          // Now process the function body
          transDec(decl);
        }
      }
    }
    // Fourth pass: process variable declarations and other declarations
    if (decls != null && decls.list != null) {
      for (Absyn.Decl decl : decls.list) {
        if (!(decl instanceof Absyn.StructDeclaration) &&
            !(decl instanceof Absyn.FunctionDefinition) &&
            !(decl instanceof Absyn.FunctionDeclaration)) {
          transDec(decl);
        }
      }
    }
  }

  private void error(int pos, String msg) {
    env.errorMsg.error(pos, msg);
  }

  static final Types.VOID VOID = new Types.VOID();
  static final Types.INT INT = new Types.INT();
  static final Types.STRING STRING = new Types.STRING();

  private Exp checkInt(ExpTy et, int pos) {
    if (!INT.coerceTo(et.ty))
      error(pos, "integer required");
    return et.exp;
  }

  private Exp checkComparable(ExpTy et, int pos) {
    Type a = et.ty.actual();
    if (!(a instanceof Types.INT
        || a instanceof Types.STRING
        || a instanceof Types.RECORD
        || a instanceof Types.ARRAY))
      error(pos, "integer, string, record or array required");
    return et.exp;
  }

  private Exp checkOrderable(ExpTy et, int pos) {
    Type a = et.ty.actual();
    if (!(a instanceof Types.INT
        || a instanceof Types.STRING))
      error(pos, "integer or string required");
    return et.exp;
  }

  ExpTy transExp(Absyn.Exp e) {
    ExpTy result;

    if (e == null)
      return new ExpTy(null, VOID);
    // C AST expression types
    else if (e instanceof Absyn.IdExp)
      result = transExp((Absyn.IdExp) e);
    else if (e instanceof Absyn.Id)
      result = transExp((Absyn.Id) e);
    else if (e instanceof Absyn.Int)
      result = transExp((Absyn.Int) e);
    else if (e instanceof Absyn.StringLit)
      result = transExp((Absyn.StringLit) e);
    else if (e instanceof Absyn.Char)
      result = transExp((Absyn.Char) e);
    else if (e instanceof Absyn.FuncExpression)
      result = transExp((Absyn.FuncExpression) e);
    else if (e instanceof Absyn.BinOp)
      result = transExp((Absyn.BinOp) e);
    else if (e instanceof Absyn.ArrayExpression)
      result = transExp((Absyn.ArrayExpression) e);
    else if (e instanceof Absyn.AssignmentExpression)
      result = transExp((Absyn.AssignmentExpression) e);
    else if (e instanceof Absyn.CommaExpression)
      result = transExp((Absyn.CommaExpression) e);
    else if (e instanceof Absyn.UnaryExpression)
      result = transExp((Absyn.UnaryExpression) e);
    else if (e instanceof Absyn.PlusPlusExpression)
      result = transExp((Absyn.PlusPlusExpression) e);
    else if (e instanceof Absyn.MinusMinusExpression)
      result = transExp((Absyn.MinusMinusExpression) e);
    else if (e instanceof Absyn.ParenExpression)
      result = transExp((Absyn.ParenExpression) e);
    else if (e instanceof Absyn.Postfix)
      result = transExp((Absyn.Postfix) e);
    else if (e instanceof Absyn.SizeofExpression)
      result = transExp((Absyn.SizeofExpression) e);
    else if (e instanceof Absyn.EmptyExpression)
      result = transExp((Absyn.EmptyExpression) e);
    else if (e instanceof Absyn.ExpList)
      result = transExp((Absyn.ExpList) e);
    else if (e instanceof Absyn.ArgumentList)
      result = transExp((Absyn.ArgumentList) e);
    else
      throw new Error("Semant.transExp: unknown expression type: " + e.getClass().getName());
    return result;
  }

  // C AST: Handle ArgumentList (function call arguments)
  ExpTy transExp(Absyn.ArgumentList e) {
    // ArgumentList is used for function call arguments
    // We don't need to type-check it here as it's handled in transArgs
    // For now, return a generic type
    return new ExpTy(null, INT); // Placeholder
  }

  // C AST: Handle ExpList (brace-enclosed list, used for array/struct
  // initializers)
  ExpTy transExp(Absyn.ExpList e) {
    // ExpList is used for initializers - we don't need to type-check it here
    // as it's handled in checkArrayInitializer
    // For now, return a generic type - the actual checking happens in transDec
    return new ExpTy(null, INT); // Placeholder - actual type checking in checkArrayInitializer
  }

  // C AST: Handle IdExp (variable reference)
  ExpTy transExp(Absyn.IdExp e) {
    try {
      java.lang.reflect.Field idField = Absyn.IdExp.class.getDeclaredField("id");
      idField.setAccessible(true);
      Absyn.Id id = (Absyn.Id) idField.get(e);
      return transExp(id);
    } catch (Exception ex) {
      error(e.pos, "error accessing IdExp.id");
      return new ExpTy(null, VOID);
    }
  }

  // C AST: Handle Id (identifier)
  ExpTy transExp(Absyn.Id e) {
    Symbol name = Symbol.symbol(e.id);
    Entry x = (Entry) env.venv.get(name);
    if (x instanceof VarEntry) {
      VarEntry ent = (VarEntry) x;
      return new ExpTy(null, ent.ty);
    }
    error(e.pos, "undeclared variable: " + e.id);
    return new ExpTy(null, VOID);
  }

  // C AST: Handle ArrayExpression (array subscript)
  ExpTy transExp(Absyn.ArrayExpression e) {
    ExpTy var = transExp(e.name);
    ExpTy index = transExp(e.index);
    checkInt(index, e.index.pos);
    Type actual = var.ty.actual();
    if (actual instanceof Types.ARRAY) {
      Types.ARRAY array = (Types.ARRAY) actual;
      return new ExpTy(null, array.element);
    }
    error(e.name.pos, "array required");
    return new ExpTy(null, VOID);
  }

  // C AST: Handle Postfix (struct/union member access)
  ExpTy transExp(Absyn.Postfix e) {
    if (e instanceof Absyn.Access) {
      Absyn.Access acc = (Absyn.Access) e;
      ExpTy var = transExp(acc.postfix_expression);
      Type actual = var.ty.actual();
      if (actual instanceof Types.RECORD) {
        try {
          java.lang.reflect.Field targetField = Absyn.Access.class.getDeclaredField("target");
          targetField.setAccessible(true);
          Absyn.Id target = (Absyn.Id) targetField.get(acc);
          Symbol fieldName = Symbol.symbol(target.id);
          for (Types.RECORD field = (Types.RECORD) actual; field != null; field = field.tail) {
            if (field.fieldName == fieldName)
              return new ExpTy(null, field.fieldType);
          }
          error(acc.pos, "undeclared field: " + target.id);
        } catch (Exception ex) {
          error(acc.pos, "error accessing field");
        }
      } else
        error(acc.postfix_expression.pos, "record required");
      return new ExpTy(null, VOID);
    }
    // Other postfix expressions
    return transExp(e.postfix_expression);
  }

  // C AST: Handle Int (integer literal)
  ExpTy transExp(Absyn.Int e) {
    return new ExpTy(null, INT);
  }

  // C AST: Handle StringLit (string literal)
  ExpTy transExp(Absyn.StringLit e) {
    return new ExpTy(null, STRING);
  }

  // C AST: Handle Char (character literal)
  ExpTy transExp(Absyn.Char e) {
    return new ExpTy(null, INT); // Characters are integers in C
  }

  // C AST: Handle FuncExpression (function call)
  ExpTy transExp(Absyn.FuncExpression e) {
    // e.name is the function identifier, e.args is the argument list
    // Look up the function directly without trying to resolve as a variable first
    if (e.name instanceof Absyn.Id) {
      Absyn.Id id = (Absyn.Id) e.name;
      Symbol funcName = Symbol.symbol(id.id);
      Entry x = (Entry) env.venv.get(funcName);
      if (x instanceof FunEntry) {
        FunEntry f = (FunEntry) x;
        transArgs(e.pos, f.formals, e.args);
        // Return the actual type (resolve NAME types)
        Type resultType = f.result.actual();
        return new ExpTy(null, resultType);
      }
      error(e.pos, "undeclared function: " + id.id);
    } else {
      error(e.pos, "invalid function call");
    }
    return new ExpTy(null, VOID);
  }

  // Helper to collect all arguments from ArgumentList into an ArrayList
  // ArgumentList structure: ArgumentList(args=rest, arg=current)
  // The last argument might be a single Exp (not ArgumentList)
  private java.util.ArrayList<Absyn.Exp> collectArguments(Absyn.Exp args) {
    java.util.ArrayList<Absyn.Exp> argList = new java.util.ArrayList<>();
    Absyn.Exp current = args;
    while (current instanceof Absyn.ArgumentList) {
      Absyn.ArgumentList currAl = (Absyn.ArgumentList) current;
      argList.add(currAl.arg);
      current = currAl.args;
    }
    // If there's a remaining expression that's not an ArgumentList, it's the last
    // argument
    if (current != null && !(current instanceof Absyn.ArgumentList)) {
      argList.add(current);
    }
    // The list is already in reverse order (last arg first), so reverse it
    java.util.Collections.reverse(argList);
    return argList;
  }

  // Helper to process arguments from ArrayList
  private void transArgsFromList(int epos, Types.RECORD formal, java.util.ArrayList<Absyn.Exp> argList, int index) {
    if (formal == null) {
      if (index < argList.size()) {
        error(epos, "too many arguments");
      }
      return;
    }
    if (index >= argList.size()) {
      error(epos, "missing argument for " + formal.fieldName);
      return;
    }

    ExpTy e = transExp(argList.get(index));
    Type argActual = e.ty.actual();
    Type formalActual = formal.fieldType.actual();

    boolean typeMatch = false;

    // Check if both are INT (after resolving NAME types)
    // This covers: int to int, int to char*, char* to int, char* to char*, etc.
    if (argActual instanceof Types.INT && formalActual instanceof Types.INT) {
      // Both are INT (including pointers which are simplified to INT), allow it
      typeMatch = true;
    } else if (argActual instanceof Types.RECORD && formalActual instanceof Types.RECORD) {
      // Both are struct/record types - allow if they're the same type
      // In our simplified system, we allow struct-to-struct if both are RECORD
      typeMatch = true;
    } else {
      // Try standard coercion
      typeMatch = e.ty.coerceTo(formal.fieldType);
      // Also try reverse coercion (in case types are compatible but coercion is
      // one-way)
      if (!typeMatch) {
        typeMatch = formal.fieldType.coerceTo(e.ty);
      }
      // As a last resort, if both are INT-based, allow it
      // (this handles edge cases where types aren't exactly matching)
      if (!typeMatch && (argActual instanceof Types.INT) && (formalActual instanceof Types.INT)) {
        typeMatch = true;
      }
    }

    if (!typeMatch) {
      error(argList.get(index).pos, "argument type mismatch");
    }
    transArgsFromList(epos, formal.tail, argList, index + 1);
  }

  private void transArgs(int epos, Types.RECORD formal, Absyn.Exp args) {
    // In C, args can be Exp (for single arg) or ExpList (for multiple args) or
    // ArgumentList
    // For ArgumentList, collect all arguments first and process them in order
    if (args instanceof Absyn.ArgumentList) {
      java.util.ArrayList<Absyn.Exp> argList = collectArguments(args);
      transArgsFromList(epos, formal, argList, 0);
      return;
    }

    // For ExpList or single Exp, use the original recursive approach
    if (formal == null) {
      if (args != null) {
        if (args instanceof Absyn.ExpList) {
          Absyn.ExpList el = (Absyn.ExpList) args;
          if (el.exps != null && !el.exps.isEmpty())
            error(el.exps.get(0).pos, "too many arguments");
        } else {
          error(args.pos, "too many arguments");
        }
      }
      return;
    }
    if (args == null) {
      error(epos, "missing argument for " + formal.fieldName);
      return;
    }

    ExpTy e;
    Absyn.Exp remainingArgs = null;

    if (args instanceof Absyn.ExpList) {
      Absyn.ExpList el = (Absyn.ExpList) args;
      if (el.exps == null || el.exps.isEmpty()) {
        error(epos, "missing argument for " + formal.fieldName);
        return;
      }
      e = transExp(el.exps.get(0));
      if (el.exps.size() > 1) {
        remainingArgs = new Absyn.ExpList(0);
        for (int i = 1; i < el.exps.size(); i++) {
          ((Absyn.ExpList) remainingArgs).add(el.exps.get(i));
        }
      }
    } else {
      e = transExp(args);
    }

    // Check type compatibility - allow INT to INT (including pointers which are
    // INT)
    // In our simplified system, pointers, int, and char all resolve to INT
    Type argActual = e.ty.actual();
    Type formalActual = formal.fieldType.actual();

    boolean typeMatch = false;

    // Check if both are INT (after resolving NAME types)
    // This covers: int to int, int to char*, char* to int, char* to char*, etc.
    if (argActual instanceof Types.INT && formalActual instanceof Types.INT) {
      // Both are INT (including pointers which are simplified to INT), allow it
      typeMatch = true;
    } else if (argActual instanceof Types.RECORD && formalActual instanceof Types.RECORD) {
      // Both are struct/record types - allow if they're the same type
      // In our simplified system, we allow struct-to-struct if both are RECORD
      typeMatch = true;
    } else {
      // Try standard coercion
      typeMatch = e.ty.coerceTo(formal.fieldType);
      // Also try reverse coercion (in case types are compatible but coercion is
      // one-way)
      if (!typeMatch) {
        typeMatch = formal.fieldType.coerceTo(e.ty);
      }
      // As a last resort, if both are INT-based, allow it
      // (this handles edge cases where types aren't exactly matching)
      if (!typeMatch && (argActual instanceof Types.INT) && (formalActual instanceof Types.INT)) {
        typeMatch = true;
      }
    }

    if (!typeMatch) {
      error(args.pos, "argument type mismatch");
    }
    transArgs(epos, formal.tail, remainingArgs);
  }

  // C AST: Handle BinOp (binary operators)
  ExpTy transExp(Absyn.BinOp e) {
    ExpTy left = transExp(e.left);
    ExpTy right = transExp(e.right);

    switch (e.oper) {
      case Absyn.BinOp.ADD:
      case Absyn.BinOp.SUB:
      case Absyn.BinOp.MULT:
      case Absyn.BinOp.DIV:
      case Absyn.BinOp.MOD:
        checkInt(left, e.left.pos);
        checkInt(right, e.right.pos);
        return new ExpTy(null, INT);
      case Absyn.BinOp.EQ:
      case Absyn.BinOp.NEQ:
        checkComparable(left, e.left.pos);
        checkComparable(right, e.right.pos);
        if (!left.ty.coerceTo(right.ty) && !right.ty.coerceTo(left.ty))
          error(e.pos, "incompatible operands to equality operator");
        return new ExpTy(null, INT);
      case Absyn.BinOp.LT:
      case Absyn.BinOp.LE:
      case Absyn.BinOp.GT:
      case Absyn.BinOp.GE:
        checkOrderable(left, e.left.pos);
        checkOrderable(right, e.right.pos);
        if (!left.ty.coerceTo(right.ty) && !right.ty.coerceTo(left.ty))
          error(e.pos, "incompatible operands to inequality operator");
        return new ExpTy(null, INT);
      case Absyn.BinOp.LOGICAL_AND:
      case Absyn.BinOp.LOGICAL_OR:
        // Logical operators - both operands should be scalar
        return new ExpTy(null, INT);
      case Absyn.BinOp.ASSIGN:
        // Assignment is handled separately in AssignmentExpression
        error(e.pos, "assignment should use AssignmentExpression");
        return new ExpTy(null, VOID);
      default:
        return new ExpTy(null, INT); // Default to int for other operators
    }
  }

  // C AST: Handle AssignmentExpression
  ExpTy transExp(Absyn.AssignmentExpression e) {
    ExpTy left = transExp(e.left);
    ExpTy right = transExp(e.right);
    if (!right.ty.coerceTo(left.ty))
      error(e.pos, "assignment type mismatch");
    return new ExpTy(null, left.ty);
  }

  // C AST: Handle CommaExpression
  ExpTy transExp(Absyn.CommaExpression e) {
    transExp(e.left); // Evaluate left, discard result
    return transExp(e.right); // Return right result
  }

  // C AST: Handle UnaryExpression
  ExpTy transExp(Absyn.UnaryExpression e) {
    try {
      java.lang.reflect.Field expField = Absyn.UnaryExpression.class.getDeclaredField("exp");
      expField.setAccessible(true);
      Absyn.Exp expVal = (Absyn.Exp) expField.get(e);
      java.lang.reflect.Field prefixField = Absyn.UnaryExpression.class.getDeclaredField("prefix");
      prefixField.setAccessible(true);
      int prefix = prefixField.getInt(e);

      if (expVal == null)
        return new ExpTy(null, VOID);
      ExpTy operand = transExp(expVal);

      switch (prefix) {
        case Absyn.UnaryExpression.AND: // Address-of
          return new ExpTy(null, INT); // Pointer type - simplified
        case Absyn.UnaryExpression.TIMES: // Dereference
          return new ExpTy(null, INT); // Simplified
        case Absyn.UnaryExpression.PLUS:
        case Absyn.UnaryExpression.MINUS:
          checkInt(operand, expVal.pos);
          return new ExpTy(null, INT);
        case Absyn.UnaryExpression.NOT:
        case Absyn.UnaryExpression.TILDE:
          checkInt(operand, expVal.pos);
          return new ExpTy(null, INT);
        default:
          return new ExpTy(null, INT);
      }
    } catch (Exception ex) {
      error(e.pos, "error processing unary expression");
      return new ExpTy(null, VOID);
    }
  }

  // C AST: Handle PlusPlusExpression (++x or x++)
  ExpTy transExp(Absyn.PlusPlusExpression e) {
    ExpTy operand = transExp(e.postfix_expression);
    checkInt(operand, e.postfix_expression.pos);
    return new ExpTy(null, INT);
  }

  // C AST: Handle MinusMinusExpression (--x or x--)
  ExpTy transExp(Absyn.MinusMinusExpression e) {
    ExpTy operand = transExp(e.postfix_expression);
    checkInt(operand, e.postfix_expression.pos);
    return new ExpTy(null, INT);
  }

  // C AST: Handle ParenExpression
  ExpTy transExp(Absyn.ParenExpression e) {
    return transExp(e.exp);
  }

  // C AST: Handle SizeofExpression
  ExpTy transExp(Absyn.SizeofExpression e) {
    try {
      java.lang.reflect.Field expField = Absyn.SizeofExpression.class.getDeclaredField("exp");
      expField.setAccessible(true);
      Absyn.Exp expVal = (Absyn.Exp) expField.get(e);
      if (expVal != null) {
        transExp(expVal); // Type-check the expression
      }
    } catch (Exception ex) {
      // Ignore if we can't access the field
    }
    return new ExpTy(null, INT); // sizeof returns int
  }

  // C AST: Handle EmptyExpression
  ExpTy transExp(Absyn.EmptyExpression e) {
    return new ExpTy(null, VOID);
  }

  // C statements - statements don't return values but we use ExpTy for
  // consistency
  void transStmt(Absyn.Statement s) {
    if (s == null)
      return;

    if (s instanceof Absyn.ExpressionStatement) {
      Absyn.ExpressionStatement es = (Absyn.ExpressionStatement) s;
      transExp(es.expression);
    } else if (s instanceof Absyn.CompoundStatement) {
      Absyn.CompoundStatement cs = (Absyn.CompoundStatement) s;
      env.venv.beginScope();
      if (cs.decl_list != null) {
        transDec(cs.decl_list);
      }
      if (cs.stmt_list != null) {
        transStmt(cs.stmt_list);
      }
      env.venv.endScope();
    } else if (s instanceof Absyn.IfElseStatement) {
      Absyn.IfElseStatement ies = (Absyn.IfElseStatement) s;
      transExp(ies.expression); // Type-check the condition
      transStmt(ies.if_statement);
      if (ies.else_statement != null) {
        transStmt(ies.else_statement);
      }
    } else if (s instanceof Absyn.WhileStatement) {
      Absyn.WhileStatement ws = (Absyn.WhileStatement) s;
      transExp(ws.expression); // Type-check the condition
      Semant loop = new LoopSemant(env, level);
      loop.transStmt(ws.statement);
    } else if (s instanceof Absyn.DoStatement) {
      Absyn.DoStatement ds = (Absyn.DoStatement) s;
      Semant loop = new LoopSemant(env, level);
      loop.transStmt(ds.statement);
      transExp(ds.expression); // Type-check the condition
    } else if (s instanceof Absyn.ForStatement) {
      Absyn.ForStatement fs = (Absyn.ForStatement) s;
      env.venv.beginScope();
      if (fs.assignment != null)
        transExp(fs.assignment);
      if (fs.comparison != null) {
        transExp(fs.comparison); // Type-check the condition
      }
      Semant loop = new LoopSemant(env, level);
      loop.transStmt(fs.statement);
      if (fs.iteration != null)
        transExp(fs.iteration);
      env.venv.endScope();
    } else if (s instanceof Absyn.ReturnStatement) {
      Absyn.ReturnStatement rs = (Absyn.ReturnStatement) s;
      if (rs.expression != null) {
        transExp(rs.expression);
      }
    } else if (s instanceof Absyn.JumpStatement) {
      // break or continue - handled by LoopSemant
    } else if (s instanceof Absyn.Goto) {
      // goto statement - no type checking needed
    } else if (s instanceof Absyn.LabelStatement) {
      Absyn.LabelStatement ls = (Absyn.LabelStatement) s;
      try {
        java.lang.reflect.Field stmtField = Absyn.LabelStatement.class.getDeclaredField("stmt");
        stmtField.setAccessible(true);
        Absyn.Statement stmt = (Absyn.Statement) stmtField.get(ls);
        if (stmt != null) {
          transStmt(stmt);
        }
      } catch (Exception ex) {
        // Ignore if we can't access the field
      }
    } else if (s instanceof Absyn.EmptyStatement) {
      // Empty statement - nothing to do
    } else if (s instanceof Absyn.StatementList) {
      Absyn.StatementList sl = (Absyn.StatementList) s;
      if (sl.list != null) {
        transStmt(sl.list);
      }
      if (sl.stmt != null) {
        transStmt(sl.stmt);
      }
    }
  }

  // Handle C AST types (Decl instead of Dec)
  Exp transDec(Absyn.Decl d) {
    if (d instanceof Absyn.VarDeclaration)
      return transDec((Absyn.VarDeclaration) d);
    if (d instanceof Absyn.FunctionDefinition)
      return transDec((Absyn.FunctionDefinition) d);
    if (d instanceof Absyn.FunctionDeclaration)
      return transDec((Absyn.FunctionDeclaration) d);
    if (d instanceof Absyn.StructDeclaration)
      return transDec((Absyn.StructDeclaration) d);
    if (d instanceof Absyn.DeclarationList)
      return transDec((Absyn.DeclarationList) d);
    // Other declaration types (typedef, etc.) - skip for now
    return null;
  }

  // Handle C AST DeclarationList (list of declarations)
  Exp transDec(Absyn.DeclarationList dl) {
    if (dl != null && dl.list != null) {
      for (Absyn.Decl decl : dl.list) {
        transDec(decl);
      }
    }
    return null;
  }

  // Handle C AST StructDeclaration (struct/union)
  Exp transDec(Absyn.StructDeclaration d) {
    // Process struct/union type declaration
    Symbol name = Symbol.symbol(d.name);
    Types.NAME nameType = new Types.NAME(name);

    // Build RECORD type from struct members
    Types.RECORD fields = null;
    Types.RECORD fieldsTail = null;
    if (d.body != null) {
      for (Absyn.StructMember member : d.body) {
        Type memberType = convertCType(member.type);
        Symbol memberName = Symbol.symbol(member.name);
        Types.RECORD newField = new Types.RECORD(memberName, memberType, null);
        if (fields == null) {
          fields = newField;
          fieldsTail = newField;
        } else {
          fieldsTail.tail = newField;
          fieldsTail = newField;
        }
      }
    }

    nameType.bind(fields != null ? fields : new Types.RECORD(null, VOID, null));
    env.tenv.put(name, nameType);

    // Store whether it's a union in a separate table for checking
    // Use a hashtable to track union types
    if (d.kind == Absyn.StructDeclaration.UNION) {
      if (env.unionTypes == null) {
        env.unionTypes = new Hashtable<Symbol, Symbol>();
      }
      env.unionTypes.put(name, name);
    }

    return null;
  }

  // Helper to convert C Type to internal Type representation
  Type convertCType(Absyn.Type t) {
    if (t == null)
      return VOID;

    // Get base type from name
    Type baseType = INT; // Default
    if (t.name != null) {
      Symbol typeName = Symbol.symbol(t.name);
      Types.NAME nameType = (Types.NAME) env.tenv.get(typeName);
      if (nameType != null) {
        // Found a user-defined type (struct/union) - return the NAME type directly
        // so that .actual() can resolve it to RECORD later
        baseType = nameType;
      } else {
        // Check if it's a built-in type
        if (t.name.equals("int"))
          baseType = INT;
        else if (t.name.equals("char"))
          baseType = INT; // char is int in our system
        else if (t.name.equals("void"))
          baseType = VOID;
        // Could add more types here
      }
    }

    // Handle pointers
    for (int i = 0; i < t.pointerCount; i++) {
      baseType = INT; // Simplified - pointers as int for now
    }

    // Handle arrays
    if (t.brackets != null && !t.brackets.isEmpty()) {
      // For arrays, we need the element type
      Type elemType = baseType;
      if (baseType instanceof Types.NAME) {
        elemType = baseType.actual();
      }
      for (int i = t.brackets.size() - 1; i >= 0; i--) {
        Types.NAME elemName = new Types.NAME(Symbol.symbol(""));
        elemName.bind(elemType);
        baseType = new Types.ARRAY(elemName);
      }
    }

    return baseType;
  }

  // Handle C AST FunctionDefinition
  Exp transDec(Absyn.FunctionDefinition d) {
    // Build parameter list from ParameterList
    Types.RECORD fields = null;
    Types.RECORD fieldsTail = null;
    Hashtable<Symbol, Symbol> paramHash = new Hashtable<Symbol, Symbol>();

    if (d.params != null && d.params.params != null) {
      for (Absyn.Parameter param : d.params.params) {
        Type paramType = convertCType(param.type);
        // For struct types, convertCType returns a NAME that resolves to RECORD
        // We need to wrap it in a NAME for the parameter, but keep the original type
        Symbol paramName = Symbol.symbol(param.name);
        Types.NAME nameType = new Types.NAME(paramName);
        // Bind the parameter name to the actual type (which might be a NAME for
        // structs)
        // If paramType is already a NAME, we should use it directly instead of wrapping
        // But we need to create a new NAME for the parameter name, so bind to paramType
        nameType.bind(paramType);

        if (paramHash.put(paramName, paramName) != null) {
          error(param.pos, "duplicate parameter: " + param.name);
        }

        Types.RECORD newField = new Types.RECORD(paramName, nameType, null);
        if (fields == null) {
          fields = newField;
          fieldsTail = newField;
        } else {
          fieldsTail.tail = newField;
          fieldsTail = newField;
        }
      }
    }

    // Convert C Type to internal Type
    Type resultType = d.type != null ? convertCType(d.type) : VOID;

    Symbol name = Symbol.symbol(d.name);
    FunEntry entry = new FunEntry(fields, resultType);
    env.venv.put(name, entry);

    // Process function body if present
    if (d.body != null) {
      env.venv.beginScope();

      // Build escape list for parameters
      Util.BoolList formalsEscape = null;
      Util.BoolList formalsEscapeTail = null;
      if (d.params != null && d.params.params != null) {
        for (Absyn.Parameter param : d.params.params) {
          Util.BoolList newList = new Util.BoolList(param.escape, null);
          if (formalsEscape == null) {
            formalsEscape = newList;
            formalsEscapeTail = newList;
          } else {
            formalsEscapeTail.tail = newList;
            formalsEscapeTail = newList;
          }
        }
      }

      // Create new level for this function
      Translate.Level funLevel = new Translate.Level(level, name, formalsEscape, true);
      entry.level = funLevel;

      // Put parameters into environment with their access
      putTypeFields(fields, funLevel.formals);

      // Process function body as Statement
      Semant fun = new Semant(env, funLevel);
      fun.inFunction = true; // Mark that we're now in a function
      fun.transStmt(d.body);

      env.venv.endScope();
    }

    return null;
  }

  // Handle C AST FunctionDeclaration (forward declaration, no body)
  Exp transDec(Absyn.FunctionDeclaration d) {
    // Just register the function signature
    Types.RECORD fields = null;
    // Note: ParameterTypeList structure may differ from ParameterList
    // For now, we'll create an empty parameter list
    Type resultType = d.type != null ? convertCType(d.type) : VOID;
    Symbol name = Symbol.symbol(d.name);
    FunEntry entry = new FunEntry(fields, resultType);
    env.venv.put(name, entry);
    return null;
  }

  // Handle C AST VarDeclaration
  Exp transDec(Absyn.VarDeclaration d) {
    // Check if this is a top-level variable (not inside a function)
    // Top-level variables are not allowed in C (only in C++)
    if (!inFunction) {
      error(d.pos, "top-level variable declaration not allowed");
    }

    ExpTy init = null;
    if (d.init != null) {
      init = transExp(d.init);
    }
    Type type;
    if (d.type == null) {
      if (init != null) {
        type = init.ty;
      } else {
        error(d.pos, "type required for variable declaration");
        type = VOID;
      }
    } else {
      type = convertCType(d.type);

      // Check if type is a union - unions cannot be initialized
      if (init != null && isUnionType(d.type)) {
        error(d.pos, "union cannot be initialized");
      }

      // Only check type compatibility if there's a real initializer (not
      // EmptyExpression)
      if (init != null && d.init != null && !(d.init instanceof Absyn.EmptyExpression) && !init.ty.coerceTo(VOID)) {
        // Check for character literal assignment to int - not allowed without cast
        if (d.type != null && d.type.name != null && d.type.name.equals("int") && d.init instanceof Absyn.Char) {
          error(d.pos, "character literal cannot be assigned to int");
        }

        // Check if type is a struct/record - struct initializers use ExpList
        // convertCType returns a NAME type for structs, which resolves to RECORD via
        // .actual()
        Type actualType = type.actual();
        boolean isStructType = (actualType instanceof Types.RECORD);
        // Also check by name if it's not a built-in type (might be a struct)
        boolean mightBeStruct = (d.type != null && d.type.name != null &&
            !d.type.name.equals("int") &&
            !d.type.name.equals("char") &&
            !d.type.name.equals("void") &&
            d.type.pointerCount == 0 &&
            (d.type.brackets == null || d.type.brackets.isEmpty()) &&
            env.tenv.get(Symbol.symbol(d.type.name)) != null);

        // For struct initializers, skip type checking (ExpList returns INT placeholder)
        if ((isStructType || mightBeStruct) && d.init instanceof Absyn.ExpList) {
          // Struct initializer - skip type coercion check as ExpList returns INT
          // placeholder
          // The structure is validated by checkArrayInitializer (which returns true for
          // non-arrays)
          // No type check needed for struct initializers
        } else {
          // Check array initializer dimensions if type is an array
          if (!checkArrayInitializer(d.init, type, d.type)) {
            error(d.pos, "array initializer dimension mismatch");
          } else if (d.type != null && d.type.brackets != null && !d.type.brackets.isEmpty()
              && d.init instanceof Absyn.ExpList) {
            // Array initializer - ExpList returns INT placeholder, but we've validated
            // dimensions
            // Allow it if dimensions match
          } else {
            // Check type compatibility - allow INT to INT (including pointers which are
            // INT)
            // In our simplified system, pointers, int, and char all resolve to INT
            Type initActual = init.ty.actual();
            Type typeActual = type.actual();

            // Check if both are INT (after resolving NAME types)
            // This covers: int to int, int to char*, char* to int, char* to char*, etc.
            boolean bothInt = (initActual instanceof Types.INT) && (typeActual instanceof Types.INT);

            // Also check if both are RECORD (struct types)
            boolean bothRecord = (initActual instanceof Types.RECORD) && (typeActual instanceof Types.RECORD);

            // In our simplified type system, all INT-based types (int, char, pointers) are
            // compatible
            // Also allow RECORD to RECORD (struct types)
            // The key insight: if both resolve to INT or both resolve to RECORD, they're
            // compatible
            boolean compatible = bothInt || bothRecord;

            // If not obviously compatible, try coercion in both directions
            if (!compatible) {
              compatible = init.ty.coerceTo(type) || type.coerceTo(init.ty);
            }

            // As a last resort, if either is INT and target has pointerCount > 0, allow it
            // (pointers are INT in our simplified system)
            if (!compatible && (initActual instanceof Types.INT) &&
                d.type != null && d.type.pointerCount > 0) {
              compatible = true;
            }

            if (!compatible) {
              error(d.pos, "initializer type mismatch");
            }
          }
        }
      }
    }
    // Allocate the variable based on escape information
    boolean escape = d.escape;
    Translate.Access access = level.allocLocal(escape);
    Symbol name = Symbol.symbol(d.name);
    VarEntry entry = new VarEntry(access, type);
    env.venv.put(name, entry);
    return null;
  }

  // Check if a type is a union
  boolean isUnionType(Absyn.Type t) {
    if (t == null || t.name == null)
      return false;
    Symbol typeName = Symbol.symbol(t.name);
    if (env.unionTypes != null && env.unionTypes.get(typeName) != null) {
      return true;
    }
    return false;
  }

  // Check if array initializer matches array type dimensions
  boolean checkArrayInitializer(Absyn.Exp init, Type expectedType, Absyn.Type cType) {
    if (cType == null || cType.brackets == null || cType.brackets.isEmpty()) {
      // Not an array type, use regular type checking
      return true;
    }

    // Count expected dimensions
    int expectedDims = cType.brackets.size();

    // Check if initializer is an ExpList (brace-enclosed list)
    if (init instanceof Absyn.ExpList) {
      Absyn.ExpList el = (Absyn.ExpList) init;
      if (el.exps == null || el.exps.isEmpty()) {
        return true; // Empty initializer is valid
      }

      // Get expected size for first dimension
      int expectedCount = getArrayDimension(cType, 0);
      // If dimension is unspecified (expectedCount == -1), allow any size
      if (expectedCount > 0 && el.exps.size() != expectedCount) {
        return false; // Wrong number of elements in first dimension
      }

      // For multi-dimensional arrays, check nested structure
      if (expectedDims > 1) {
        // Check each sub-initializer
        Absyn.Type subType = getSubArrayType(cType);
        for (Absyn.Exp subInit : el.exps) {
          // Each sub-initializer should be an ExpList for the next dimension
          if (subInit instanceof Absyn.ExpList) {
            Absyn.ExpList subEl = (Absyn.ExpList) subInit;
            int subExpectedCount = getArrayDimension(subType, 0);
            // If dimension is unspecified (subExpectedCount == -1), allow any size
            if (subExpectedCount > 0 && subEl.exps.size() != subExpectedCount) {
              return false; // Wrong number of elements in sub-array
            }
            // Recursively check if there are more dimensions
            if (subType != null && subType.brackets != null && subType.brackets.size() > 1) {
              if (!checkArrayInitializer(subInit, getElementType(expectedType), subType)) {
                return false;
              }
            }
          } else {
            // Sub-initializer should be an ExpList for multi-dimensional arrays
            return false;
          }
        }
      } else {
        // Single dimension - check element types match
        // (This is handled by the regular type checking)
      }
    } else {
      // Non-ExpList initializer for array type - might be valid for single element
      // But for multi-dimensional, we need ExpList
      if (expectedDims > 1) {
        return false;
      }
    }

    return true;
  }

  // Helper to get array dimension from C Type
  int getArrayDimension(Absyn.Type t, int level) {
    if (t == null || t.brackets == null || level >= t.brackets.size()) {
      return -1; // Invalid
    }
    Absyn.ArrayType at = t.brackets.get(level);
    if (at.size != null) {
      // Evaluate the size expression - simplified, assume it's an Int
      if (at.size instanceof Absyn.Int) {
        Absyn.Int intExp = (Absyn.Int) at.size;
        return intExp.val;
      }
    }
    return -1; // Unknown size
  }

  // Helper to get element type from array type
  Type getElementType(Type arrayType) {
    if (arrayType instanceof Types.ARRAY) {
      return ((Types.ARRAY) arrayType).element;
    }
    return arrayType;
  }

  // Helper to get sub-array type (remove one dimension)
  Absyn.Type getSubArrayType(Absyn.Type t) {
    if (t == null || t.brackets == null || t.brackets.isEmpty()) {
      return null;
    }
    // Create a new Type with one less dimension
    java.util.ArrayList<Absyn.ArrayType> newBrackets = new java.util.ArrayList<>();
    for (int i = 1; i < t.brackets.size(); i++) {
      newBrackets.add(t.brackets.get(i));
    }
    return new Absyn.Type(t.pos, t.name, t.pointerCount, newBrackets);
  }

  private void putTypeFields(Types.RECORD f, Translate.AccessList formalAccesses) {
    if (f == null)
      return;
    // For formal parameters, we need to get the access from the level's formals
    // The formals are already allocated in the level, so we get them from
    // frameFormals
    Translate.Access paramAccess = null;
    if (formalAccesses != null) {
      paramAccess = formalAccesses.head;
      formalAccesses = formalAccesses.tail;
    }
    env.venv.put(f.fieldName, new VarEntry(paramAccess, f.fieldType));
    putTypeFields(f.tail, formalAccesses);
  }
}

class LoopSemant extends Semant {
  LoopSemant(Env e, Level l) {
    super(e, l);
  }

  void transStmt(Absyn.Statement s) {
    if (s instanceof Absyn.JumpStatement) {
      Absyn.JumpStatement js = (Absyn.JumpStatement) s;
      try {
        java.lang.reflect.Field typeField = Absyn.JumpStatement.class.getDeclaredField("type");
        typeField.setAccessible(true);
        int jumpType = typeField.getInt(js);
        if (jumpType == Absyn.JumpStatement.BREAK || jumpType == Absyn.JumpStatement.CONTINUE) {
          // Valid break/continue - no error
          return;
        }
      } catch (Exception ex) {
        // Ignore if we can't access the field
      }
    }
    // Call parent for other statements
    super.transStmt(s);
  }
}
