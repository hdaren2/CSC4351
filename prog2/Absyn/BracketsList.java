package Absyn;

public class BracketsList extends Absyn {
    public Object arrayTypeList; // Can be EmptyArrayTypeList or ExpressionArrayTypeList

    public BracketsList(Object arrayTypeList) {
        this.arrayTypeList = arrayTypeList;
    }
}
