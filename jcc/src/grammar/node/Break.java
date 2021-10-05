package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;
import word.Typ;

public class Break extends Node {
    public Break() {
        typ = NTyp.Break;
    }

    /* 'break' ';' */
    @Override
    public boolean forward() {
        if (cs.isTyp(Typ.BREAKTK)) {
            while(!cs.nex().isTyp(Typ.SEMICN));
            return true;
        }
        cs.nex();
        return false;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
