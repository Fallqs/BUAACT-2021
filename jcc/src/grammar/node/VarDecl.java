package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;

public class VarDecl extends Node {
    public VarDecl() {
        typ = NTyp.VarDecl;
    }

    /* VarDecl → BType VarDef { ',' VarDef } ';' */
    @Override
    public boolean forward() {

        return false;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
