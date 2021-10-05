package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;

public class While extends Node {
    public While() {
        typ = NTyp.While;
    }

    /* 'while' '(' Cond ')' Stmt */
    @Override
    public boolean forward() {

        return false;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
