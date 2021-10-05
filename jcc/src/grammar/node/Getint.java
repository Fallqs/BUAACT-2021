package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;
import word.Typ;

public class Getint extends Node {
    public Getint() {
    }

    /* 'getint' '(' ')' */
    @Override
    public boolean forward() {
        if (!cs.isTyp(Typ.GETINTTK)) return false;
        while (!cs.isTyp(Typ.LPARENT)) cs.nex();
        while (!cs.isTyp(Typ.RPARENT)) cs.nex();
        cs.nex();
        return true;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
