package grammar.node;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import meta.Meta;
import word.Typ;

public class If extends Node {
    private Node cond, then, els;

    public If() {
        typ = NTyp.If;
        autoDisplay = false;
    }

    /* 'if' '(' Cond ')' Stmt [ 'else' Stmt ] */
    @Override
    public boolean forward() {
        if (!cs.isTyp(Typ.IFTK)) return false;
        cs.chkTil(Typ.LPARENT).nex();
        (cond = New.typ(NTyp.Cond)).fwd();
        cs.chkErr(Typ.RPARENT).nex();
        (then = New.typ(NTyp.Stmt)).fwd();
        if (cs.isTyp(Typ.ELSETK)) {
            cs.nex();
            (els = New.typ(NTyp.Stmt)).fwd();
        }
        return true;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
