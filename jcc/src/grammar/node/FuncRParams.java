package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;

public class FuncRParams extends Node {
    public FuncRParams() {
        typ = NTyp.FuncRParams;
    }

    /* FuncRParams → Exp { ',' Exp } */
    @Override
    public boolean forward() {

        return false;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
