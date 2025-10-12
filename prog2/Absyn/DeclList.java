package Absyn;

import java.util.List;

public class DeclList extends Absyn {
    public List<Dec> declarations;

    public DeclList(List<Dec> declarations) {
        this.declarations = declarations;
    }
}
