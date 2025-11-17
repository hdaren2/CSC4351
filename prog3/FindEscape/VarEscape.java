package FindEscape;

class VarEscape extends Escape {
  Absyn.VarDeclaration vd;

  VarEscape(int d, Absyn.VarDeclaration v) {
    depth = d;
    vd = v;
    vd.escape = false;
  }

  void setEscape() {
    vd.escape = true;
  }
}
