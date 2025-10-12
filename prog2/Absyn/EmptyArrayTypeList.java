package Absyn;

import java.util.List;

public class EmptyArrayTypeList extends Absyn {
    public List<EmptyArrayType> arrayTypes;

    public EmptyArrayTypeList(EmptyArrayType arrayType) {
        this.arrayTypes = new java.util.ArrayList<>();
        this.arrayTypes.add(arrayType);
    }

    public void add(EmptyArrayType arrayType) {
        this.arrayTypes.add(arrayType);
    }
}
