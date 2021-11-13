package lex.node;

import lex.NTyp;
import lex.Node;
import meta.Meta;
import word.Typ;

public class Continue extends Node {
    public Continue() {
        typ = NTyp.Continue;
        autoDisplay = false;
    }

    /* 'continue' ';' */
    @Override
    public boolean forward() {
        if (!cs.isTyp(Typ.CONTINUETK)) return false;
        cs.chkErr(Typ.WHILETK).nex().chkErr(Typ.SEMICN).nex();
        return true;
    }

    @Override
    public Meta translate() {
        return null;
    }
}
