package lex.node;

import lex.NTyp;
import lex.Node;
import meta.Meta;
import word.Typ;

public class Break extends Node {
    public Break() {
        typ = NTyp.Break;
        autoDisplay = false;
    }

    /* 'break' ';' */
    @Override
    public boolean forward() {
        if (!cs.isTyp(Typ.BREAKTK)) return false;
        cs.chkErr(Typ.WHILETK).nex().chkErr(Typ.SEMICN).nex();
        return true;
    }

    @Override
    public Meta translate() {
        return null;
    }
}
