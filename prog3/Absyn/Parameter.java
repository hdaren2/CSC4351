package Absyn;
import java.util.ArrayList;

import Symbol.Symbol;
public class Parameter extends Decl {
    public Type type;
    public String name;
    public boolean escape = false; // For escape analysis
    
    public Parameter(int p, Type t, String n) {
        pos=p;
        name = n;
        type = t;
        escape = false; // Initialize to false
    }
}
