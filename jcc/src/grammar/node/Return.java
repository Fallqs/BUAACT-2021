package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;

public class Return extends Node {
    public Return() {
        typ = NTyp.Return;
    }

    /* 'return' [Exp] ';' */
    @Override
    public boolean forward() {

        return false;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
