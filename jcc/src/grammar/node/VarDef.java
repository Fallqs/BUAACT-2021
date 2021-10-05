package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;

public class VarDef extends Node {
    public VarDef() {
        typ = NTyp.VarDef;
    }

    /* VarDef → Ident { '[' ConstExp ']' } | Ident { '[' ConstExp ']' } '=' InitVal */
    @Override
    public boolean forward() {
        return false;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
