package Absyn;

import java.util.List;

public class Parameters extends Absyn {
    public List<Param> parameters;
    public boolean hasVarArgs;

    public Parameters(List<Param> parameters) {
        this.parameters = parameters;
        this.hasVarArgs = false;
    }

    public Parameters(List<Param> parameters, boolean hasVarArgs) {
        this.parameters = parameters;
        this.hasVarArgs = hasVarArgs;
    }

    public Parameters(ParameterList parameterList) {
        this.parameters = parameterList.parameters;
        this.hasVarArgs = false;
    }

    public Parameters(ParameterList parameterList, boolean hasVarArgs) {
        this.parameters = parameterList.parameters;
        this.hasVarArgs = hasVarArgs;
    }
}
