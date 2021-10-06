package grammar.node;

import grammar.NTyp;
import grammar.Node;
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
        while (!cs.isTyp(Typ.SEMICN)) cs.nex();
        cs.nex();
        return true;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
