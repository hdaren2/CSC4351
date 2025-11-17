package FindEscape;

import Symbol.Symbol;
import Symbol.Table;

public class FindEscape {
    Table escEnv = new Table(); // escEnv maps Symbol to VarDeclaration/Parameter

    public FindEscape(Absyn.Exp e) {
        traverseExp(e);
    }

    // For C: variables escape if their address is taken or if declared volatile
    void traverseExp(Absyn.Exp e) {
        if (e == null)
            return;

        // Check for address-of operator (&expr)
        if (e instanceof Absyn.UnaryExpression) {
            Absyn.UnaryExpression ue = (Absyn.UnaryExpression) e;
            try {
                java.lang.reflect.Field prefixField = Absyn.UnaryExpression.class.getDeclaredField("prefix");
                java.lang.reflect.Field expField = Absyn.UnaryExpression.class.getDeclaredField("exp");
                prefixField.setAccessible(true);
                expField.setAccessible(true);
                int prefix = prefixField.getInt(ue);
                Absyn.Exp expVal = (Absyn.Exp) expField.get(ue);
                if (prefix == Absyn.UnaryExpression.AND && expVal != null) {
                    // Address-of operator: mark the variable as escaping
                    markVariableEscapes(expVal);
                }
                // Continue traversing the expression
                if (expVal != null) {
                    traverseExp(expVal);
                }
            } catch (Exception ex) {
                // If reflection fails, try to traverse anyway
                // This is a fallback
            }
        }
        // Handle other expression types
        else if (e instanceof Absyn.IdExp) {
            // Just a variable reference, no escape
        } else if (e instanceof Absyn.Id) {
            // Just an identifier, no escape
        } else if (e instanceof Absyn.BinOp) {
            Absyn.BinOp binOp = (Absyn.BinOp) e;
            traverseExp(binOp.left);
            traverseExp(binOp.right);
        } else if (e instanceof Absyn.ArrayExpression) {
            Absyn.ArrayExpression ae = (Absyn.ArrayExpression) e;
            traverseExp(ae.name);
            traverseExp(ae.index);
        } else if (e instanceof Absyn.AssignmentExpression) {
            Absyn.AssignmentExpression ae = (Absyn.AssignmentExpression) e;
            traverseExp(ae.left);
            traverseExp(ae.right);
        } else if (e instanceof Absyn.FuncExpression) {
            Absyn.FuncExpression fe = (Absyn.FuncExpression) e;
            traverseExp(fe.name);
            traverseExp(fe.args);
        } else if (e instanceof Absyn.ExpList) {
            Absyn.ExpList el = (Absyn.ExpList) e;
            if (el.exps != null) {
                for (Absyn.Exp arg : el.exps) {
                    traverseExp(arg);
                }
            }
        } else if (e instanceof Absyn.CommaExpression) {
            Absyn.CommaExpression ce = (Absyn.CommaExpression) e;
            traverseExp(ce.left);
            traverseExp(ce.right);
        } else if (e instanceof Absyn.ParenExpression) {
            Absyn.ParenExpression pe = (Absyn.ParenExpression) e;
            traverseExp(pe.exp);
        } else if (e instanceof Absyn.PlusPlusExpression) {
            Absyn.PlusPlusExpression ppe = (Absyn.PlusPlusExpression) e;
            traverseExp(ppe.postfix_expression);
        } else if (e instanceof Absyn.MinusMinusExpression) {
            Absyn.MinusMinusExpression mme = (Absyn.MinusMinusExpression) e;
            traverseExp(mme.postfix_expression);
        } else if (e instanceof Absyn.SizeofExpression) {
            Absyn.SizeofExpression se = (Absyn.SizeofExpression) e;
            // exp field is package-private, but we can try to access it
            // If it fails, we'll need to make the field public or add a getter
            try {
                java.lang.reflect.Field expField = Absyn.SizeofExpression.class.getDeclaredField("exp");
                expField.setAccessible(true);
                Absyn.Exp expVal = (Absyn.Exp) expField.get(se);
                if (expVal != null) {
                    traverseExp(expVal);
                }
            } catch (Exception ex) {
                // Ignore if we can't access the field
            }
        } else if (e instanceof Absyn.Postfix) {
            Absyn.Postfix pf = (Absyn.Postfix) e;
            traverseExp(pf.postfix_expression);
        }
        // Note: ExpressionStatement is a Statement, not an Exp, so this won't be
        // reached here
    }

    // Mark a variable as escaping when its address is taken
    void markVariableEscapes(Absyn.Exp e) {
        if (e == null)
            return;
        if (e instanceof Absyn.IdExp) {
            Absyn.IdExp idExp = (Absyn.IdExp) e;
            try {
                java.lang.reflect.Field idField = Absyn.IdExp.class.getDeclaredField("id");
                idField.setAccessible(true);
                Absyn.Id id = (Absyn.Id) idField.get(idExp);
                if (id != null) {
                    markIdEscapes(id.id);
                }
            } catch (Exception ex) {
                // Ignore if we can't access the field
            }
        } else if (e instanceof Absyn.Id) {
            Absyn.Id id = (Absyn.Id) e;
            markIdEscapes(id.id);
        } else if (e instanceof Absyn.ArrayExpression) {
            Absyn.ArrayExpression ae = (Absyn.ArrayExpression) e;
            // Address of array element - mark the array variable
            markVariableEscapes(ae.name);
        } else if (e instanceof Absyn.Postfix) {
            Absyn.Postfix pf = (Absyn.Postfix) e;
            if (pf instanceof Absyn.Access) {
                Absyn.Access acc = (Absyn.Access) pf;
                // Address of struct/union member - mark the base variable
                markVariableEscapes(acc.postfix_expression);
            } else {
                // Other postfix expressions
                markVariableEscapes(pf.postfix_expression);
            }
        }
        // Try to access exp field using reflection for UnaryExpression
        else if (e instanceof Absyn.UnaryExpression) {
            try {
                java.lang.reflect.Field expField = Absyn.UnaryExpression.class.getDeclaredField("exp");
                expField.setAccessible(true);
                Absyn.Exp expVal = (Absyn.Exp) expField.get(e);
                if (expVal != null) {
                    markVariableEscapes(expVal);
                }
            } catch (Exception ex) {
                // Ignore if we can't access the field
            }
        }
    }

    void markIdEscapes(String name) {
        Symbol sym = Symbol.symbol(name);
        Object entry = escEnv.get(sym);
        if (entry instanceof Absyn.VarDeclaration) {
            ((Absyn.VarDeclaration) entry).escape = true;
        } else if (entry instanceof Absyn.Parameter) {
            ((Absyn.Parameter) entry).escape = true;
        }
    }

    void traverseDec(Absyn.Decl d) {
        if (d == null)
            return;

        if (d instanceof Absyn.VarDeclaration) {
            Absyn.VarDeclaration vd = (Absyn.VarDeclaration) d;
            // Check if variable is volatile
            if (vd.bitfield != null && vd.bitfield.isVolatile) {
                vd.escape = true;
            }
            // Traverse initializer
            if (vd.init != null) {
                traverseExp(vd.init);
            }
            // Enter variable into escape environment
            Symbol sym = Symbol.symbol(vd.name);
            escEnv.put(sym, vd);
        } else if (d instanceof Absyn.FunctionDefinition) {
            Absyn.FunctionDefinition fd = (Absyn.FunctionDefinition) d;
            escEnv.beginScope();

            // Process formal parameters
            if (fd.params != null && fd.params.params != null) {
                for (Absyn.Parameter param : fd.params.params) {
                    // Check if parameter is volatile
                    // Note: Parameters typically don't have bitfield, but check anyway
                    param.escape = false; // Initialize
                    Symbol sym = Symbol.symbol(param.name);
                    escEnv.put(sym, param);
                }
            }

            // Traverse function body
            if (fd.body != null) {
                traverseStmt(fd.body);
            }

            escEnv.endScope();
        } else if (d instanceof Absyn.FunctionDeclaration) {
            // Function declaration (no body) - nothing to do
        }
        // Note: CompoundStatement is a Statement, not a Decl, so it won't be reached
        // here
        else if (d instanceof Absyn.DeclarationList) {
            Absyn.DeclarationList dl = (Absyn.DeclarationList) d;
            if (dl.list != null) {
                for (Absyn.Decl decl : dl.list) {
                    traverseDec(decl);
                }
            }
        }
    }

    void traverseStmt(Absyn.Statement s) {
        if (s == null)
            return;

        if (s instanceof Absyn.ExpressionStatement) {
            Absyn.ExpressionStatement es = (Absyn.ExpressionStatement) s;
            traverseExp(es.expression);
        } else if (s instanceof Absyn.CompoundStatement) {
            Absyn.CompoundStatement cs = (Absyn.CompoundStatement) s;
            escEnv.beginScope();
            if (cs.decl_list != null) {
                traverseDec(cs.decl_list);
            }
            if (cs.stmt_list != null) {
                traverseStmt(cs.stmt_list);
            }
            escEnv.endScope();
        } else if (s instanceof Absyn.IfElseStatement) {
            Absyn.IfElseStatement ies = (Absyn.IfElseStatement) s;
            traverseExp(ies.expression);
            traverseStmt(ies.if_statement);
            if (ies.else_statement != null) {
                traverseStmt(ies.else_statement);
            }
        } else if (s instanceof Absyn.WhileStatement) {
            Absyn.WhileStatement ws = (Absyn.WhileStatement) s;
            traverseExp(ws.expression);
            traverseStmt(ws.statement);
        } else if (s instanceof Absyn.DoStatement) {
            Absyn.DoStatement ds = (Absyn.DoStatement) s;
            traverseStmt(ds.statement);
            traverseExp(ds.expression);
        } else if (s instanceof Absyn.ForStatement) {
            Absyn.ForStatement fs = (Absyn.ForStatement) s;
            if (fs.assignment != null)
                traverseExp(fs.assignment);
            if (fs.comparison != null)
                traverseExp(fs.comparison);
            if (fs.iteration != null)
                traverseExp(fs.iteration);
            traverseStmt(fs.statement);
        } else if (s instanceof Absyn.ReturnStatement) {
            Absyn.ReturnStatement rs = (Absyn.ReturnStatement) s;
            if (rs.expression != null) {
                traverseExp(rs.expression);
            }
        } else if (s instanceof Absyn.JumpStatement) {
            // No expressions to traverse
        } else if (s instanceof Absyn.Goto) {
            // No expressions to traverse
        } else if (s instanceof Absyn.LabelStatement) {
            Absyn.LabelStatement ls = (Absyn.LabelStatement) s;
            try {
                java.lang.reflect.Field stmtField = Absyn.LabelStatement.class.getDeclaredField("stmt");
                stmtField.setAccessible(true);
                Absyn.Statement stmt = (Absyn.Statement) stmtField.get(ls);
                if (stmt != null) {
                    traverseStmt(stmt);
                }
            } catch (Exception ex) {
                // Ignore if we can't access the field
            }
        } else if (s instanceof Absyn.EmptyStatement) {
            // No expressions to traverse
        } else if (s instanceof Absyn.StatementList) {
            Absyn.StatementList sl = (Absyn.StatementList) s;
            if (sl.list != null) {
                traverseStmt(sl.list);
            }
            if (sl.stmt != null) {
                traverseStmt(sl.stmt);
            }
        }
    }

    // Public method to traverse a program (top-level declarations)
    public void traverseProg(Absyn.Exp exp) {
        traverseExp(exp);
    }

    // Public method to traverse declarations
    public void traverseDecl(Absyn.Decl decl) {
        traverseDec(decl);
    }
}
