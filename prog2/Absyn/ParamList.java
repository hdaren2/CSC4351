package Absyn;

import java.util.List;

public class ParamList extends Absyn {
    public List<Param> parameters;

    public ParamList(List<Param> parameters) {
        this.parameters = parameters;
    }
}
