package lex.node;

import lex.NTyp;
import lex.New;
import lex.Node;
import lex.nd.Ref;
import meta.Meta;
import word.Typ;

/**
 * Wasted Class
 */
public class PrimaryExp extends Node {
    private Node cont;

    public PrimaryExp() {
        typ = NTyp.PrimaryExp;
    }

    /* PrimaryExp → '(' Exp ')' | LVal | Number || Ref(func) */
    @Override
    public boolean forward() {
        if (cs.isTyp(Typ.INTCON)) {
            (cont = New.typ(NTyp.Number)).fwd();
        } else if (cs.isTyp(Typ.LPARENT)) {
            cs.nex();
            (cont = New.typ(NTyp.Exp)).fwd();
            cs.chkErr(Typ.RPARENT).nex();
        } else if ((cont = new Ref(false, false)).forward()) {
            (cont = new Ref(false, false)).forward();
            if (cont.gettyp() == NTyp.LVal) cont.dump(NTyp.LVal);
        } else return false;
        return true;
    }

    @Override
    public void logIdt() {
        cont.logIdt();
    }

    @Override
    public Meta translate() {
        return null;
    }
}
