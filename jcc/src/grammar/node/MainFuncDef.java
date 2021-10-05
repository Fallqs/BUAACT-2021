package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;

public class MainFuncDef extends Node {
    public MainFuncDef() {
        typ = NTyp.MainFuncDef;
    }

    /* MainFuncDef → 'int' 'main' '(' ')' Block */
    @Override
    public boolean forward() {

        return false;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
