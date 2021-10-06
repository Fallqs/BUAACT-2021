package grammar.node;

import grammar.NTyp;
import grammar.Node;
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
        while (!cs.isTyp(Typ.SEMICN)) cs.nex();
        cs.nex();
        return true;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
