package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;

public class FuncFParams extends Node {
    public FuncFParams() {
        typ = NTyp.FuncFParams;
    }

    /* FuncFParams → FuncFParam { ',' FuncFParam } */
    @Override
    public boolean forward() {

        return false;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
