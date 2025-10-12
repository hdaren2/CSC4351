package Absyn;

import Symbol.Symbol;

abstract public class Type extends Absyn {

    public static Type combine(Type typeName, TypeAugments typeAugments) {
        // For now, just return the base type
        // In a more complete implementation, this would create a composite type
        // with pointers and arrays
        return typeName;
    }
}
