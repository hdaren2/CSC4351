package Absyn;

import java.util.List;

public class EnumeratorList extends Absyn {
    public List<Enumerator> enumerators;

    public EnumeratorList(List<Enumerator> enumerators) {
        this.enumerators = enumerators;
    }
}
