package Absyn;

import java.util.List;

public class ParameterList extends Absyn {
    public List<Param> parameters;

    public ParameterList(List<Param> parameters) {
        this.parameters = parameters;
    }
}
