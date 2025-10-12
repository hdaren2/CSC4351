package Absyn;

import java.util.List;

public class InitializerList extends Absyn {
    public List<Initializer> initializers;

    public InitializerList(List<Initializer> initializers) {
        this.initializers = initializers;
    }
}
