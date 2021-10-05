package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;

public class If extends Node {
    public If() {
        typ = NTyp.If;
    }

    /* 'if' '(' Cond ')' Stmt [ 'else' Stmt ] */
    @Override
    public boolean forward() {

        return false;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
