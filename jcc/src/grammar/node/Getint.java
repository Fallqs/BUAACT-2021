package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;
import meta.mcode.Cin;
import word.Typ;

public class Getint extends Node {
    public Getint() {
        autoDisplay = false;
    }

    /* 'getint' '(' ')' */
    @Override
    public boolean forward() {
        if (!cs.isTyp(Typ.GETINTTK)) return false;
        cs.chkTil(Typ.LPARENT).nex().chkErr(Typ.RPARENT).nex();
        return true;
    }

    @Override
    public Meta translate() {
        return new Cin();
    }
}
