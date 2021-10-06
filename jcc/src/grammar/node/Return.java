package grammar.node;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import meta.Meta;
import word.Typ;

public class Return extends Node {
    private Node val;

    public Return() {
        typ = NTyp.Return;
        autoDisplay = false;
    }

    /* 'return' [Exp] ';' */
    @Override
    public boolean forward() {
        if (!cs.isTyp(Typ.RETURNTK)) return false;
        cs.nex();
        while (!cs.isTyp(Typ.SEMICN)) if (!(val = New.typ(NTyp.Exp)).fwd()) {
            val = null;
            cs.nex();
        }
        cs.nex();
        return true;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
