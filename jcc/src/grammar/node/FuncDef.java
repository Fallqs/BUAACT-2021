package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;

public class FuncDef extends Node {
    public FuncDef() {
        typ = NTyp.FuncDef;
    }

    /* FuncDef → FuncType Ident '(' [FuncFParams] ')' Block */
    @Override
    public boolean forward() {

        return false;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
