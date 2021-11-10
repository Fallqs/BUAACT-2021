package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;
import word.Result;
import word.Typ;

public class BType extends Node {
//    private final Node fa;

    public BType(Node fa) {
        this.typ = NTyp.BType;
        this.fa = fa;
        autoDisplay = false;
    }

    @Override
    public boolean forward() {
        Result r = cs.cont();
        if (r.typ != Typ.INTTK && r.typ != Typ.VOIDTK) return false;
        fa.reward(r);
        return true;
    }

    @Override
    public Meta translate() {
        return null;
    }
}
