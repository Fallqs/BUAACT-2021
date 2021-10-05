package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;

public class ConstInitVal extends Node {
    public ConstInitVal() {
        typ = NTyp.ConstInitVal;
    }

    /* ConstInitVal → ConstExp | '{' [ ConstInitVal { ',' ConstInitVal } ] '}' */
    @Override
    public boolean forward() {
        return false;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
