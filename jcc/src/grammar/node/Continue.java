package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;
import word.Typ;

public class Continue extends Node {
    public Continue() {
        typ = NTyp.Continue;
    }

    /* 'continue' ';' */
    @Override
    public boolean forward() {
        if (cs.isTyp(Typ.CONTINUETK)) {
            while(!cs.nex().isTyp(Typ.SEMICN));
            cs.nex();
            return true;
        }
        return false;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
