package Semant;

class LoopVarEntry extends VarEntry {
  LoopVarEntry(Types.Type t) {
    this(null, t);
  }
  LoopVarEntry(Translate.Access acc, Types.Type t) {
    super(acc, t);
  }
}
