package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;

public class LVal extends Node {
    public LVal() {
        typ = NTyp.LVal;
    }

    /* LVal → Ident {'[' Exp ']'} */
    @Override
    public boolean forward() {

        return false;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
