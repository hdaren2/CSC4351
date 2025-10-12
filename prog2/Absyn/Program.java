package Absyn;

import java.util.List;

public class Program extends Absyn {
    public List<Dec> declarations;

    public Program(List<Dec> declarations) {
        this.declarations = declarations;
    }
}
