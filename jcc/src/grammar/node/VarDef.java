package grammar.node;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import meta.Meta;
import word.Typ;

public class VarDef extends Node {
    private LVal lval;
    private Node initval;

    public VarDef() {
        typ = NTyp.VarDef;
    }

    /**
     * VarDef → Ident { '[' ConstExp ']' } | Ident { '[' ConstExp ']' } '=' InitVal
     */
    @Override
    public boolean forward() {
        if (!(lval = new LVal(NTyp.NULL)).forward()) return false;
        if (!cs.isTyp(Typ.ASSIGN)) return true;
        cs.nex();
        (initval = New.typ(NTyp.InitVal)).fwd();
        return true;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
