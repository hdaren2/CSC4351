package Absyn;

import java.util.List;

public class BitFieldList extends Absyn {
    public List<BitMod> modifiers;

    public BitFieldList(List<BitMod> modifiers) {
        this.modifiers = modifiers;
    }
}
