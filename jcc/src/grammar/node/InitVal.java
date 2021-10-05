package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;

public class InitVal extends Node {
    public InitVal() {
        typ = NTyp.InitVal;
    }

    /* InitVal → Exp | '{' [ InitVal { ',' InitVal } ] '}' */
    @Override
    public boolean forward() {

        return false;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
