package Absyn;

import java.util.List;

public class ExpressionArrayTypeList extends Absyn {
    public List<ExpressionArrayType> arrayTypes;

    public ExpressionArrayTypeList(ExpressionArrayType arrayType) {
        this.arrayTypes = new java.util.ArrayList<>();
        this.arrayTypes.add(arrayType);
    }

    public void add(ExpressionArrayType arrayType) {
        this.arrayTypes.add(arrayType);
    }
}
