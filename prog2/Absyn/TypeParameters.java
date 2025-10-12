package Absyn;

import java.util.List;

public class TypeParameters extends Absyn {
    public List<Type> types;
    public List<Param> parameters;
    public boolean hasVarArgs;

    public TypeParameters(List<Type> types) {
        this.types = types;
        this.parameters = new java.util.ArrayList<>();
        this.hasVarArgs = false;
    }

    public TypeParameters(List<Type> types, boolean hasVarArgs) {
        this.types = types;
        this.parameters = new java.util.ArrayList<>();
        this.hasVarArgs = hasVarArgs;
    }

    public TypeParameters(ParameterTypeList parameterTypeList) {
        this.types = parameterTypeList.types;
        this.parameters = new java.util.ArrayList<>();
        this.hasVarArgs = false;
    }

    public TypeParameters(ParameterTypeList parameterTypeList, boolean hasVarArgs) {
        this.types = parameterTypeList.types;
        this.parameters = new java.util.ArrayList<>();
        this.hasVarArgs = hasVarArgs;
    }
}
