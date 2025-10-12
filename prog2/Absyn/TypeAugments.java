package Absyn;

public class TypeAugments extends Absyn {
    public PointerList pointerList;
    public BracketsList bracketsList;

    public TypeAugments(PointerList pointerList, BracketsList bracketsList) {
        this.pointerList = pointerList;
        this.bracketsList = bracketsList;
    }
}
