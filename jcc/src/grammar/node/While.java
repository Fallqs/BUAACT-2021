package grammar.node;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import meta.Meta;
import word.Typ;

public class While extends Node {
    private Node cond, stmt;

    public While() {
        typ = NTyp.While;
        autoDisplay = false;
    }

    /* 'while' '(' Cond ')' Stmt */
    @Override
    public boolean forward() {
        if (!cs.isTyp(Typ.WHILETK)) return false;
        cs.chkTil(Typ.LPARENT).nex();
        (cond = New.typ(NTyp.Cond)).fwd();
        cs.chkErr(Typ.RPARENT).nex();
        (stmt = New.typ(NTyp.Stmt)).fwd();
        return true;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
