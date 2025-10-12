package Absyn;

import java.util.List;

public class ParameterTypeList extends Absyn {
    public List<Type> types;

    public ParameterTypeList(List<Type> types) {
        this.types = types;
    }
}
