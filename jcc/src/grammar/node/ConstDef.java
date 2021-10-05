package grammar.node;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import meta.Meta;
import word.Typ;

public class ConstDef extends Node {
    private Node lval;
    private Node constinitval;

    public ConstDef() {
        typ = NTyp.ConstDef;
    }

    /* ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal */
    @Override
    public boolean forward() {
        lval = New.typ(NTyp.LVal);
        if (!lval.fwd()) return false;
        if (cs.isTyp(Typ.ASSIGN)) cs.nex();

        return false;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
