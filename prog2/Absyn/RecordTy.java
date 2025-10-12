package Absyn;
import Symbol.Symbol;
public class RecordTy extends Type {
   public FieldList fields;
   public RecordTy(int p, FieldList f) {pos=p; fields=f;}
}   
