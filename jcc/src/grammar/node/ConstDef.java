package grammar.node;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import meta.Meta;
import word.Typ;

public class ConstDef extends Node {
    private LVal lval;
    private Node constinitval;

    public ConstDef() {
        typ = NTyp.ConstDef;
    }

    /** ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal
     *  Ident: IDENTFR; '[': LBRACK; ']': RBRACK; '=': ASSIGN; */
    @Override
    public boolean forward() {
        if (!(lval = new LVal(NTyp.NULL)).forward()) return false;
        if (cs.isTyp(Typ.ASSIGN)) cs.nex();
        (constinitval = New.typ(NTyp.ConstInitVal)).fwd();
        return true;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
